package com.mooc.ppjoke.ui.binding_adpter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.SofaTab;
import com.mooc.ppjoke.ui.find.TagListFragment;
import com.mooc.ppjoke.ui.home.HomeFragment;

import java.util.ArrayList;

public class SofaBindingAdapter {

	@BindingAdapter(value = {"bindSofaFragment", "sofaTabConfig", "detach", "fragmentType"}, requireAll = false)
	public static void bindSofaTabLayout(TabLayout tabLayout, Fragment fragment, SofaTab tabConfig, Boolean detach, int type) {

		ArrayList<SofaTab.Tabs> tabs = new ArrayList<>();
		//解析TabConfig
		for (SofaTab.Tabs tab : tabConfig.tabs) {
			if (tab.enable) {
				tabs.add(tab);
			}
		}
		ViewPager2 viewPager2;
		// 这里要区分两个不同的布局!!!!!!
		if (type == 0) {
			viewPager2 = tabLayout.getRootView().findViewById(R.id.view_pager);
		} else {
			viewPager2 = tabLayout.getRootView().findViewById(R.id.view_pager_find);
		}
		viewPager2.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);
		viewPager2.setAdapter(new FragmentStateAdapter(fragment.getChildFragmentManager(), fragment.getLifecycle()) {
			@NonNull
			@Override
			public Fragment createFragment(int position) {
				//这里不需要自己保管了,FragmentStateAdapter内部自己会管理已实例化的fragment对象。
				if (type == 0) {
					return HomeFragment.newInstance(tabs.get(position).tag);
				} else if (type == 1) {
					return TagListFragment.newInstance(tabs.get(position).tag);
				} else {
					throw new IllegalArgumentException("Can not parse fragmentType: " + type);
				}
			}

			@Override
			public int getItemCount() {
				return tabs.size();
			}
		});
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
		if (detach) {
			//移除回调
			viewPager2.unregisterOnPageChangeCallback(mPageChangeCallback);
		}
		tabLayout.setTabGravity(tabConfig.tabGravity);
		new TabLayoutMediator(tabLayout, viewPager2, true, new TabLayoutMediator.TabConfigurationStrategy() {
			@Override
			public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
				TextView tabView = new TextView(fragment.getContext());
				int[][] states = new int[2][];
				states[0] = new int[]{android.R.attr.state_selected};
				states[1] = new int[]{};

				int[] colors = new int[]{Color.parseColor(tabConfig.activeColor), Color.parseColor(tabConfig.normalColor)};
				ColorStateList stateList = new ColorStateList(states, colors);
				tabView.setTextColor(stateList);
				tabView.setText(tabs.get(position).title);
				tabView.setTextSize(tabConfig.normalSize);
				tab.setCustomView(tabView);
			}
		}).attach();

		viewPager2.registerOnPageChangeCallback(mPageChangeCallback);
		//切换到默认选择项,那当然要等待初始化完成之后才有效
		viewPager2.post(() -> viewPager2.setCurrentItem(tabConfig.select, false));
	}
}
