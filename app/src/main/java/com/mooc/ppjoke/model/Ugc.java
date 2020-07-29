package com.mooc.ppjoke.model;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import java.io.Serializable;

public class Ugc extends BaseObservable implements Serializable {
    /**
     * likeCount : 153
     * shareCount : 0
     * commentCount : 4454
     * hasFavorite : false
     * hasLiked : true
     * hasdiss:false
     */

    public int likeCount;

    @Bindable
    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
        notifyPropertyChanged(BR._all);
    }

    public int shareCount;
    public int commentCount;
    public boolean hasFavorite;
    public boolean hasdiss;

    @Bindable
    public boolean isHasdiss() {
        return hasdiss;
    }

    public void setHasdiss(boolean hasdiss) {
        if (this.hasdiss == hasdiss)
            return;
        if (hasdiss) {
            setHasLiked(false);
        }
        this.hasdiss = hasdiss;
        notifyPropertyChanged(BR._all);
    }

    public boolean hasLiked;

    @Bindable
    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        if (this.hasLiked == hasLiked)
            return;
        if (hasLiked) {
            likeCount = likeCount + 1;
            setHasdiss(false);
        } else {
            likeCount = likeCount - 1;
        }
        this.hasLiked = hasLiked;
        notifyPropertyChanged(BR._all);
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Ugc))
            return false;
        Ugc newUgc = (Ugc) obj;
        return likeCount == newUgc.likeCount
                && shareCount == newUgc.shareCount
                && commentCount == newUgc.commentCount
                && hasFavorite == newUgc.hasFavorite
                && hasLiked == newUgc.hasLiked
                && hasdiss == newUgc.hasdiss;
    }

    @Bindable
    public boolean isHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
        notifyPropertyChanged(BR._all);
    }
}
