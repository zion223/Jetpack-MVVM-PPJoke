package com.mooc.ppjoke.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.mooc.ppjoke.exoplayer.PageListPlayDetector;
import com.mooc.ppjoke.exoplayer.PageListPlayManager;
import com.mooc.ppjoke.model.Feed;
import com.mooc.ppjoke.ui.AbsListFragment;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class ProfileListFragment extends AbsListFragment<Feed, ProfileViewModel> {
    private String tabType;
    private PageListPlayDetector playDetector;
    private boolean shouldPause = true;

    public static ProfileListFragment newInstance(String tabType) {

        Bundle args = new Bundle();
        args.putString(ProfileActivity.KEY_TAB_TYPE, tabType);
        ProfileListFragment fragment = new ProfileListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playDetector = new PageListPlayDetector(this, mRecyclerView);
        mViewModel.setProfileType(tabType);
        mRefreshLayout.setEnableRefresh(false);
    }

    @Override
    public PagedListAdapter getAdapter() {
        tabType = getArguments().getString(ProfileActivity.KEY_TAB_TYPE);
        return new ProfileListAdapter(getContext(), tabType) {
            @Override
            public void onViewDetachedFromWindow2(ViewHolder holder) {
                if (holder.isVideoItem()) {
                    playDetector.removeTarget(holder.listPlayerView);
                }
            }

            @Override
            public void onViewAttachedToWindow2(ViewHolder holder) {
                if (holder.isVideoItem()) {
                    playDetector.addTarget(holder.listPlayerView);
                }
            }

            @Override
            public void onStartFeedDetailActivity(Feed feed) {
                shouldPause = false;
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        if (shouldPause) {
            playDetector.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldPause = true;
        //从评论tab页跳转到 详情页之后再返回回来，咱们需要暂停视频播放。因为评论和tab页是没有视频的
        if (TextUtils.equals(tabType, ProfileActivity.TAB_TYPE_COMMENT)) {
            playDetector.onPause();
        } else {
            playDetector.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        PageListPlayManager.release(tabType);
        super.onDestroyView();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<Feed> currentList = adapter.getCurrentList();
        finishRefresh(currentList != null && currentList.size() > 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
