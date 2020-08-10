package com.mooc.ppjoke.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseFragment;
import com.mooc.libarchitecture.utils.Utils;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.databinding.LayoutRefreshViewBinding;
import com.mooc.ppjoke.exoplayer.PageListPlayDetector;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class AbsListFragment<T, M extends AbsViewModel<T>> extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
	protected PageListPlayDetector playDetector;

	protected PagedListAdapter<T, RecyclerView.ViewHolder> adapter;
	protected M mViewModel;

	@Override
	protected void initViewModel() {
		genericViewModel();
	}

	@Override
	protected DataBindingConfig getDataBindingConfig() {
		return new DataBindingConfig(R.layout.layout_refresh_view, BR.vm, mViewModel)
				.addBindingParam(BR.loadmorelistener, this)
				.addBindingParam(BR.refreshlistener, this);
	}


	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		adapter = getAdapter();
		//mViewModel.adapter.set(adapter);
		RecyclerView recyclerView = ((LayoutRefreshViewBinding) getBinding()).recyclerView;
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(Utils.getApp().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
		recyclerView.setItemAnimator(null);

		//默认给列表中的Item 一个 10dp的ItemDecoration
		DividerItemDecoration decoration = new DividerItemDecoration(Utils.getApp().getApplicationContext(), LinearLayoutManager.VERTICAL);
		decoration.setDrawable(ContextCompat.getDrawable(Utils.getApp().getApplicationContext(), R.drawable.list_divider));
		recyclerView.addItemDecoration(decoration);
		playDetector = new PageListPlayDetector(this, ((LayoutRefreshViewBinding) getBinding()).recyclerView);

		//触发页面初始化数据加载的逻辑
		mViewModel.getPageData().observe(getViewLifecycleOwner(), this::submitList);

		//监听分页时有无更多数据,以决定是否关闭上拉加载的动画
		mViewModel.getBoundaryPageData().observe(getViewLifecycleOwner(), this::finishRefresh);
	}


	private void genericViewModel() {
		//利用 子类传递的 泛型参数实例化出absViewModel 对象。
		ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
		Type[] arguments = type.getActualTypeArguments();
		if (arguments.length > 1) {
			Type argument = arguments[1];
			Class modelClaz = ((Class) argument).asSubclass(AbsViewModel.class);
			mViewModel = (M) getFragmentViewModel(modelClaz);
		}
	}

	public void submitList(PagedList<T> result) {
		//只有当新数据集合大于0 的时候，才调用adapter.submitList
		//否则可能会出现 页面----有数据----->被清空-----空布局
		if (result.size() > 0) {
			adapter.submitList(result);
		}
		finishRefresh(result.size() > 0);
	}

	public void finishRefresh(boolean hasData) {
		PagedList<T> currentList = adapter.getCurrentList();
		hasData = hasData || currentList != null && currentList.size() > 0;

		mViewModel.hasData.set(hasData);
		mViewModel.reload.setValue(new Object());
	}

	/**
	 * 因而 我们在 onCreateView的时候 创建了 PagedListAdapter
	 * 所以，如果arguments 有参数需要传递到Adapter 中，那么需要在getAdapter()方法中取出参数。
	 *
	 * @return PagedListAdapter
	 */
	public abstract PagedListAdapter<T, RecyclerView.ViewHolder> getAdapter();
}
