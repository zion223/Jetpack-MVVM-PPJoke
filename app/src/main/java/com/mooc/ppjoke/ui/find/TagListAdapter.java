package com.mooc.ppjoke.ui.find;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mooc.libcommon.extention.AbsPagedListAdapter;
import com.mooc.ppjoke.databinding.LayoutTagListItemBinding;
import com.mooc.ppjoke.model.TagList;
import com.mooc.ppjoke.ui.InteractionPresenter;

public class TagListAdapter extends AbsPagedListAdapter<TagList, TagListAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private Context mContext;

    protected TagListAdapter(Context context) {
        super(new DiffUtil.ItemCallback<TagList>() {
            @Override
            public boolean areItemsTheSame(@NonNull TagList oldItem, @NonNull TagList newItem) {
                return oldItem.tagId == newItem.tagId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull TagList oldItem, @NonNull TagList newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        LayoutTagListItemBinding itemBinding = LayoutTagListItemBinding.inflate(mInflater, parent, false);
        return new ViewHolder(itemBinding.getRoot(), itemBinding);
    }

    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final TagList item = getItem(position);
        holder.bindData(item);
        holder.mItemBinding.actionFollow.setOnClickListener(v -> InteractionPresenter.toggleTagLike(((LifecycleOwner) mContext), item));
        holder.itemView.setOnClickListener(v -> TagFeedListActivity.startActivity(mContext, item));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutTagListItemBinding mItemBinding;

        public ViewHolder(@NonNull View itemView, LayoutTagListItemBinding itemBinding) {
            super(itemView);
            mItemBinding = itemBinding;
        }

        public void bindData(TagList item) {
            mItemBinding.setTagList(item);
        }
    }
}
