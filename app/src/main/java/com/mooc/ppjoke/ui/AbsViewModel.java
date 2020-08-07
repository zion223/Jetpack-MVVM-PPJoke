package com.mooc.ppjoke.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

public abstract class AbsViewModel<T> extends ViewModel {


	protected PagedList.Config config;

	private DataSource dataSource;
	private LiveData<PagedList<T>> pageData;
	private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

	//SmartRefreshLayout data
	public MutableLiveData<Boolean> enableRefresh = new MutableLiveData<>();
	public MutableLiveData<Object> reload = new MutableLiveData<>();

	//RecyclerView data
	public ObservableInt removedItemDecoration = new ObservableInt();
	public ObservableInt scrollToPosition = new ObservableInt();
	public ObservableField<PagedListAdapter> adapter = new ObservableField<>();

	//EmptyView data
	public ObservableBoolean hasData = new ObservableBoolean();
	public ObservableField<String> emptyViewTitle = new ObservableField<>();
	public ObservableField<String> emptyViewButtonTitle = new ObservableField<>();
	public ObservableField<View.OnClickListener> emptyViewButtonListener = new ObservableField<>();

	public AbsViewModel() {

		config = new PagedList.Config.Builder()
				//Defines the number of items loaded at once from the DataSource.
				.setPageSize(10)
				//Defines how many items to load when first load occurs.
				.setInitialLoadSizeHint(12)
				// .setMaxSize(100)；
				// .setEnablePlaceholders(false)
				// .setPrefetchDistance()
				.build();

		pageData = new LivePagedListBuilder(factory, config)
				.setInitialLoadKey(0)
				//.setFetchExecutor()
				.setBoundaryCallback(callback)
				.build();

		hasData.set(false);
		enableRefresh.setValue(true);
	}


	public LiveData<PagedList<T>> getPageData() {
		return pageData;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public LiveData<Boolean> getBoundaryPageData() {
		return boundaryPageData;
	}

	//PagedList数据被加载 情况的边界回调callback
	//但 不是每一次分页 都会回调这里，具体请看 ContiguousPagedList#mReceiver#onPageResult
	//deferBoundaryCallbacks
	PagedList.BoundaryCallback<T> callback = new PagedList.BoundaryCallback<T>() {
		@Override
		public void onZeroItemsLoaded() {
			//新提交的PagedList中没有数据
			boundaryPageData.postValue(false);
		}

		@Override
		public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
			//新提交的PagedList中第一条数据被加载到列表上
			boundaryPageData.postValue(true);
		}

		@Override
		public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
			//新提交的PagedList中最后一条数据被加载到列表上
		}
	};

	DataSource.Factory factory = new DataSource.Factory() {
		@NonNull
		@Override
		public DataSource create() {
			if (dataSource == null || dataSource.isInvalid()) {
				dataSource = createDataSource();
			}
			return dataSource;
		}
	};

	public abstract DataSource createDataSource();


	//可以在这个方法里 做一些清理 的工作
	@Override
	protected void onCleared() {

	}
}
