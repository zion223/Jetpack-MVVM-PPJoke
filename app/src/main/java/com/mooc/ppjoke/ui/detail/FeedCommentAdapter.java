package com.mooc.ppjoke.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mooc.libcommon.extention.AbsPagedListAdapter;
import com.mooc.libcommon.utils.PixUtils;
import com.mooc.ppjoke.databinding.LayoutFeedCommentListItemBinding;
import com.mooc.ppjoke.model.Comment;
import com.mooc.ppjoke.ui.InteractionPresenter;
import com.mooc.ppjoke.ui.MutableItemKeyedDataSource;
import com.mooc.ppjoke.ui.login.UserManager;
import com.mooc.ppjoke.ui.publish.PreviewActivity;

public class FeedCommentAdapter extends AbsPagedListAdapter<Comment, FeedCommentAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;

    protected FeedCommentAdapter(Context context) {
        super(new DiffUtil.ItemCallback<Comment>() {
            @Override
            public boolean areItemsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem.id == newItem.id;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Comment oldItem, @NonNull Comment newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        LayoutFeedCommentListItemBinding binding = LayoutFeedCommentListItemBinding.inflate(mInflater, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        Comment item = getItem(position);
        holder.bindData(item);
        holder.mBinding.commentDelete.setOnClickListener(v ->
                InteractionPresenter.deleteFeedComment(mContext, item.itemId, item.commentId)
                        .observe((LifecycleOwner) mContext, success -> {
                            if (success) {
                                deleteAndRefreshList(item);
                            }
                        }));
        holder.mBinding.commentCover.setOnClickListener(v -> {
            boolean isVideo = item.commentType == Comment.COMMENT_TYPE_VIDEO;
            PreviewActivity.startActivityForResult((Activity) mContext, isVideo ? item.videoUrl : item.imageUrl, isVideo, null);
        });

    }

    public void deleteAndRefreshList(Comment item) {
        MutableItemKeyedDataSource<Integer, Comment> dataSource = new MutableItemKeyedDataSource<Integer, Comment>((ItemKeyedDataSource) getCurrentList().getDataSource()) {
            @NonNull
            @Override
            public Integer getKey(@NonNull Comment item) {
                return item.id;
            }
        };
        PagedList<Comment> currentList = getCurrentList();
        for (Comment comment : currentList) {
            if (comment != item) {
                dataSource.data.add(comment);
            }
        }
        PagedList<Comment> pagedList = dataSource.buildNewPagedList(getCurrentList().getConfig());
        submitList(pagedList);
    }

    public void addAndRefreshList(Comment comment) {
        PagedList<Comment> currentList = getCurrentList();
        MutableItemKeyedDataSource<Integer, Comment> mutableItemKeyedDataSource = new MutableItemKeyedDataSource<Integer, Comment>((ItemKeyedDataSource) currentList.getDataSource()) {
            @NonNull
            @Override
            public Integer getKey(@NonNull Comment item) {
                return item.id;
            }
        };
        mutableItemKeyedDataSource.data.add(comment);
        mutableItemKeyedDataSource.data.addAll(currentList);
        PagedList<Comment> pagedList = mutableItemKeyedDataSource.buildNewPagedList(currentList.getConfig());
        submitList(pagedList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutFeedCommentListItemBinding mBinding;

        public ViewHolder(@NonNull View itemView, LayoutFeedCommentListItemBinding binding) {
            super(itemView);
            mBinding = binding;
        }

        public void bindData(Comment item) {
            mBinding.setComment(item);
            boolean self = item.author == null ? false : UserManager.get().getUserId() == item.author.userId;
            mBinding.labelAuthor.setVisibility(self ? View.VISIBLE : View.GONE);
            mBinding.commentDelete.setVisibility(self ? View.VISIBLE : View.GONE);
            if (!TextUtils.isEmpty(item.imageUrl)) {
                mBinding.commentExt.setVisibility(View.VISIBLE);
                mBinding.commentCover.setVisibility(View.VISIBLE);
                mBinding.commentCover.bindData(item.width, item.height, 0, PixUtils.dp2px(200), PixUtils.dp2px(200), item.imageUrl);
                if (!TextUtils.isEmpty(item.videoUrl)) {
                    mBinding.videoIcon.setVisibility(View.VISIBLE);
                } else {
                    mBinding.videoIcon.setVisibility(View.GONE);
                }
            } else {
                mBinding.commentCover.setVisibility(View.GONE);
                mBinding.videoIcon.setVisibility(View.GONE);
                mBinding.commentExt.setVisibility(View.GONE);
            }
        }
    }
}
