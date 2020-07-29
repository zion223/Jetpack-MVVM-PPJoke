package com.mooc.ppjoke.ui.find;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.TagList;
import com.mooc.ppjoke.ui.AbsListFragment;
import com.mooc.ppjoke.ui.MutableItemKeyedDataSource;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.List;

public class TagListFragment extends AbsListFragment<TagList, TagListViewModel> {
    public static final String KEY_TAG_TYPE = "tag_type";
    private String tagType;

    public static TagListFragment newInstance(String tagType) {

        Bundle args = new Bundle();
        args.putString(KEY_TAG_TYPE, tagType);
        TagListFragment fragment = new TagListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (TextUtils.equals(tagType, "onlyFollow")) {
            mEmptyView.setTitle(getString(R.string.tag_list_no_follow));
            mEmptyView.setButton(getString(R.string.tag_list_no_follow_button), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.getSwitchTabLiveData().setValue(new Object());
                }
            });
        }
        mRecyclerView.removeItemDecorationAt(0);
        mViewModel.setTagType(tagType);
    }

    @Override
    public PagedListAdapter getAdapter() {
        tagType = getArguments().getString(KEY_TAG_TYPE);
        TagListAdapter tagListAdapter = new TagListAdapter(getContext());
        return tagListAdapter;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getDataSource().invalidate();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<TagList> currentList = getAdapter().getCurrentList();
        long tagId = currentList == null ? 0 : currentList.get(currentList.size() - 1).tagId;
        mViewModel.loadData(tagId, new ItemKeyedDataSource.LoadCallback() {

            @Override
            public void onResult(@NonNull List data) {
                if (data != null && data.size() > 0) {
                    MutableItemKeyedDataSource<Long, TagList> mutableItemKeyedDataSource = new MutableItemKeyedDataSource<Long, TagList>((ItemKeyedDataSource) mViewModel.getDataSource()) {
                        @NonNull
                        @Override
                        public Long getKey(@NonNull TagList item) {
                            return item.tagId;
                        }
                    };
                    mutableItemKeyedDataSource.data.addAll(currentList);
                    mutableItemKeyedDataSource.data.addAll(data);
                    PagedList<TagList> pagedList = mutableItemKeyedDataSource.buildNewPagedList(currentList.getConfig());
                    submitList(pagedList);
                } else {
                    finishRefresh(false);
                }
            }
        });
    }
}
