package com.mooc.ppjoke;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mooc.libcommon.utils.StatusBar;
import com.mooc.ppjoke.model.Destination;
import com.mooc.ppjoke.ui.login.UserManager;
import com.mooc.ppjoke.utils.AppConfig;
import com.mooc.ppjoke.utils.NavGraphBuilder;
import com.mooc.ppjoke.view.AppBottomBar;

import java.util.HashMap;
import java.util.Map;

/**
 * App 主页 入口
 * <p>
 * 1.底部导航栏 使用AppBottomBar 承载
 * 2.内容区域 使用WindowInsetsNavHostFragment 承载
 * <p>
 * 3.底部导航栏 和 内容区域的 切换联动 使用NavController驱动
 * 4.底部导航栏 按钮个数和 内容区域destination个数。由注解处理器NavProcessor来收集,生成assetsdestination.json。而后我们解析它。
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private NavController navController;
	private AppBottomBar navView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//由于 启动时设置了 R.style.launcher 的windowBackground属性
		//势必要在进入主页后,把窗口背景清理掉
		setTheme(R.style.AppTheme);

		//启用沉浸式布局，白底黑字
		StatusBar.fitSystemBar(this);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		navView = findViewById(R.id.nav_view);

		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		if (fragment != null) {
			navController = NavHostFragment.findNavController(fragment);
			NavGraphBuilder.build(this, navController, fragment.getId());
		}

		navView.setOnNavigationItemSelectedListener(this);

	}


	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
		HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
		//遍历 target destination 是否需要登录拦截
		for (Map.Entry<String, Destination> entry : destConfig.entrySet()) {
			Destination value = entry.getValue();
			if (value != null && !UserManager.get().isLogin() && value.needLogin && value.id == menuItem.getItemId()) {

				UserManager.get().login(this).observe(this, user -> {
					if (user != null) {
						//即时移除observers  避免重复收到LiveData消息
						UserManager.get().getUserLiveData().removeObservers(MainActivity.this);
						navView.setSelectedItemId(menuItem.getItemId());
					}
				});
				return false;
			}

		}
		navController.navigate(menuItem.getItemId());
		return !TextUtils.isEmpty(menuItem.getTitle());
	}

	@Override
	public void onBackPressed() {
//        boolean shouldIntercept = false;
//        int homeDestinationId = 0;
//
//        Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();
//        String tag = fragment.getTag();
//        int currentPageDestId = Integer.parseInt(tag);
//
//        HashMap<String, Destination> config = AppConfig.getDestConfig();
//        Iterator<Map.Entry<String, Destination>> iterator = config.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Destination> next = iterator.next();
//            Destination destination = next.getValue();
//            if (!destination.asStarter && destination.id == currentPageDestId) {
//                shouldIntercept = true;
//            }
//
//            if (destination.asStarter) {
//                homeDestinationId = destination.id;
//            }
//        }
//
//        if (shouldIntercept && homeDestinationId > 0) {
//            navView.setSelectedItemId(homeDestinationId);
//            return;
//        }
//        super.onBackPressed();

		//当前正在显示的页面destinationId
		int currentPageId = navController.getCurrentDestination().getId();

		//APP页面路导航结构图  首页的destinationId
		int homeDestId = navController.getGraph().getStartDestination();

		//如果当前正在显示的页面不是首页，而我们点击了返回键，则拦截。
		if (currentPageId != homeDestId) {
			navView.setSelectedItemId(homeDestId);
			return;
		}

		//否则 finish，此处不宜调用onBackPressed。因为navigation会操作回退栈,切换到之前显示的页面。
		finish();
	}
}
