package com.mooc.ppjoke.ui.my;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.mooc.ppjoke.exoplayer.PageListPlayDetector;
import com.mooc.ppjoke.exoplayer.PageListPlayManager;
import com.mooc.ppjoke.model.Feed;
import com.mooc.ppjoke.ui.AbsListFragment;
import com.mooc.ppjoke.ui.home.FeedAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class UserBehaviorListFragment extends AbsListFragment<Feed, UserBehaviorViewModel> {
    private static final String CATEGORY = "user_behavior_list";
    private boolean shouldPause = true;
    private PageListPlayDetector playDetector;

    public static UserBehaviorListFragment newInstance(int behavior) {

        Bundle args = new Bundle();
        args.putInt(UserBehaviorListActivity.KEY_BEHAVIOR, behavior);
        UserBehaviorListFragment fragment = new UserBehaviorListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playDetector = new PageListPlayDetector(this, mRecyclerView);
        int behavior = getArguments().getInt(UserBehaviorListActivity.KEY_BEHAVIOR);
        mViewModel.setBehavior(behavior);
    }

    @Override
    public PagedListAdapter getAdapter() {
        return new FeedAdapter(getContext(), CATEGORY) {
            @Override
            public void onViewAttachedToWindow2(ViewHolder holder) {
                if (holder.isVideoItem()) {
                    playDetector.addTarget(holder.listPlayerView);
                }
            }

            @Override
            public void onViewDetachedFromWindow2(ViewHolder holder) {
                if (holder.isVideoItem()) {
                    playDetector.removeTarget(holder.listPlayerView);
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
        playDetector.onResume();
    }

    @Override
    public void onDestroyView() {
        PageListPlayManager.release(CATEGORY);
        super.onDestroyView();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        PagedList<Feed> currentList = adapter.getCurrentList();
        finishRefresh(currentList != null && currentList.size() > 0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewModel.getDataSource().invalidate();
    }
}
