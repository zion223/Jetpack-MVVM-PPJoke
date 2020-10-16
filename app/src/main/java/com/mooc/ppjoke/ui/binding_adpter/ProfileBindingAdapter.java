package com.mooc.ppjoke.ui.binding_adpter;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mooc.libarchitecture.utils.Utils;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.ui.my.ProfileListFragment;

public class ProfileBindingAdapter {

	public static final String TAB_TYPE_ALL = "tab_all";
	public static final String TAB_TYPE_FEED = "tab_feed";
	public static final String TAB_TYPE_COMMENT = "tab_comment";

	@BindingAdapter(value = {"initProfileTabActivity", "initProfileTab"}, requireAll = false)
	public static void initProfileTabLayout(TabLayout tabLayout, FragmentActivity initProfileTabActivity, String initProfileTab) {

		ViewPager2 viewPager2 = tabLayout.getRootView().findViewById(R.id.view_pager);

		String[] tabs = Utils.getApp().getResources().getStringArray(R.array.profile_tabs);
		viewPager2.setAdapter(new FragmentStateAdapter(initProfileTabActivity) {
			@NonNull
			@Override
			public Fragment createFragment(int position) {
				String title = "";
				switch (position) {
					case 0:
						title = TAB_TYPE_ALL;
						break;
					case 1:
						title = TAB_TYPE_FEED;
						break;
					case 2:
						title = TAB_TYPE_COMMENT;
						break;
					default:
						break;
				}
				return ProfileListFragment.newInstance(title);
			}

			@Override
			public int getItemCount() {
				return tabs.length;
			}
		});
		//autoRefresh:当我们调用viewpager的adaper#notifychangged方法的时候，要不要主动的把tablayout的选项卡给移除掉重新配置
		//要在给viewpager设置adapter之后调用
		new TabLayoutMediator(tabLayout, viewPager2, false, new TabLayoutMediator.TabConfigurationStrategy() {
			@Override
			public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
				tab.setText(tabs[position]);
			}
		}).attach();

		if (!initProfileTab.equals("")) {
			viewPager2.post(() -> {
				int initTabPosition = 0;
				switch (initProfileTab) {
					case TAB_TYPE_ALL:
						initTabPosition = 0;
						break;
					case TAB_TYPE_FEED:
						initTabPosition = 1;
						break;
					case TAB_TYPE_COMMENT:
						initTabPosition = 2;
						break;
					default:
				}
				viewPager2.setCurrentItem(initTabPosition, false);
			});
		}
	}
}
