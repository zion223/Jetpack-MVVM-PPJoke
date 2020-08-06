package com.mooc.ppjoke.ui.binding_adpter;

import androidx.databinding.BindingAdapter;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

public class AbsBindingAdapter {


	@BindingAdapter(value = {"adapter"})
	public static void initAbsRecyclerView(RecyclerView recyclerView, PagedListAdapter adapter) {
//		//recyclerView.setAdapter(adapter);
//		recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getApp().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//		recyclerView.setItemAnimator(null);
//
//		//默认给列表中的Item 一个 10dp的ItemDecoration
//		DividerItemDecoration decoration = new DividerItemDecoration(Utils.getApp().getApplicationContext(), LinearLayoutManager.VERTICAL);
//		decoration.setDrawable(ContextCompat.getDrawable(Utils.getApp().getApplicationContext(), R.drawable.list_divider));
//		recyclerView.addItemDecoration(decoration);
	}

	@BindingAdapter(value = {"reloadRefreshState"})
	public static void reloadRefreshLayoutState(SmartRefreshLayout refreshLayout, Object obj) {
		RefreshState state = refreshLayout.getState();
		if (state.isFooter && state.isOpening) {
			refreshLayout.finishLoadMore();
		} else if (state.isHeader && state.isOpening) {
			refreshLayout.finishRefresh();
		}
	}
}
