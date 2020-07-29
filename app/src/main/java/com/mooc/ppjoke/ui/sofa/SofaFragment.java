package com.mooc.ppjoke.ui.sofa;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mooc.libnavannotation.FragmentDestination;
import com.mooc.ppjoke.databinding.FragmentSofaBinding;
import com.mooc.ppjoke.model.SofaTab;
import com.mooc.ppjoke.ui.home.HomeFragment;
import com.mooc.ppjoke.utils.AppConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@FragmentDestination(pageUrl = "main/tabs/sofa", asStarter = false)
public class SofaFragment extends Fragment {
    private FragmentSofaBinding binding;
    protected ViewPager2 viewPager2;
    protected TabLayout tabLayout;
    private SofaTab tabConfig;
    private ArrayList<SofaTab.Tabs> tabs;

    //private Map<Integer, Fragment> mFragmentMap = new HashMap<>();
    private TabLayoutMediator mediator;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSofaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager2 = binding.viewPager;
        tabLayout = binding.tabLayout;
        tabConfig = getTabConfig();
        tabs = new ArrayList<>();
        for (SofaTab.Tabs tab : tabConfig.tabs) {
            if (tab.enable) {
                tabs.add(tab);
            }
        }

        //限制页面预加载
        viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
        //viewPager2默认只有一种类型的Adapter。FragmentStateAdapter
        //并且在页面切换的时候 不会调用子Fragment的setUserVisibleHint ，取而代之的是onPause(),onResume()、
        viewPager2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), this.getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
//                Fragment fragment = mFragmentMap.get(position);
//                if (fragment == null) {
//                    fragment = getTabFragment(position);
//                    mFragmentMap.put(position, fragment);
//                }
                //这里不需要自己保管了,FragmentStateAdapter内部自己会管理已实例化的fragment对象。
                return getTabFragment(position);
            }

            @Override
            public int getItemCount() {
                return tabs.size();
            }
        });

        tabLayout.setTabGravity(tabConfig.tabGravity);
        //viewPager2 就不能和再用TabLayout.setUpWithViewPager()了
        //取而代之的是TabLayoutMediator。我们可以在onConfigureTab()方法的回调里面 做tab标签的配置

        //其中autoRefresh的意思是:如果viewPager2 中child的数量发生了变化，也即我们调用了adapter#notifyItemChanged()前后getItemCount不同。
        //要不要 重新刷野tabLayout的tab标签。视情况而定,像咱们sofaFragment的tab数量一旦固定了是不会变的，传true/false  都问题不大
        mediator = new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setCustomView(makeTabView(position));
            }
        });
        mediator.attach();

        viewPager2.registerOnPageChangeCallback(mPageChangeCallback);
        //切换到默认选择项,那当然要等待初始化完成之后才有效
        viewPager2.post(() -> viewPager2.setCurrentItem(tabConfig.select, false));
    }

    ViewPager2.OnPageChangeCallback mPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            int tabCount = tabLayout.getTabCount();
            for (int i = 0; i < tabCount; i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                TextView customView = (TextView) tab.getCustomView();
                if (tab.getPosition() == position) {

                    customView.setTextSize(tabConfig.activeSize);
                    customView.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    customView.setTextSize(tabConfig.normalSize);
                    customView.setTypeface(Typeface.DEFAULT);
                }
            }
        }
    };

    private View makeTabView(int position) {
        TextView tabView = new TextView(getContext());
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(tabConfig.activeColor), Color.parseColor(tabConfig.normalColor)};
        ColorStateList stateList = new ColorStateList(states, colors);
        tabView.setTextColor(stateList);
        tabView.setText(tabs.get(position).title);
        tabView.setTextSize(tabConfig.normalSize);
        return tabView;
    }

    public Fragment getTabFragment(int position) {
        return HomeFragment.newInstance(tabs.get(position).tag);
    }

    public SofaTab getTabConfig() {
        return AppConfig.getSofaTabConfig();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment.isAdded() && fragment.isVisible()) {
                fragment.onHiddenChanged(hidden);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        mediator.detach();
        viewPager2.unregisterOnPageChangeCallback(mPageChangeCallback);
        super.onDestroy();
    }
}