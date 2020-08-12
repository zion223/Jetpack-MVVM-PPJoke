package com.mooc.ppjoke.ui.binding_adpter;

import androidx.databinding.BindingAdapter;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

public class AbsBindingAdapter {


	@BindingAdapter(value = {"reloadRefreshState"})
	public static void reloadRefreshLayoutState(SmartRefreshLayout refreshLayout, Object reload) {
		RefreshState state = refreshLayout.getState();
		if (state.isFooter && state.isOpening) {
			refreshLayout.finishLoadMore();
		} else if (state.isHeader && state.isOpening) {
			refreshLayout.finishRefresh();
		}
	}

	@BindingAdapter(value = {"enableRefresh"})
	public static void setEnableRefresh(SmartRefreshLayout refreshLayout, Boolean enable) {
		refreshLayout.setEnableRefresh(enable);
	}
}
