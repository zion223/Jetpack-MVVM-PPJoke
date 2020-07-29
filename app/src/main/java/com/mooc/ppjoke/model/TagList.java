package com.mooc.ppjoke.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

public class TagList extends BaseObservable implements Serializable {

    /**
     * id : 8
     * icon : https://p3-dy.byteimg.com/img/tos-cn-v-0000/3b1dd95af2e94225ba23bbbadef22ce2~200x200.jpeg
     * background : https://p3-dy.byteimg.com/img/tos-cn-v-0000/2eee9728d10240108d027c5e74868c0a~750x340.jpeg
     * activityIcon : https://sf1-nhcdn-tos.pstatp.com/obj/tos-cn-i-0000/51f076f662ef40c99f056a41b130c516
     * title : 纹身技术哪家强
     * intro : “你怎么纹了一个电风扇？”
     * “那叫四叶草！”
     * feedNum : 1234
     * tagId : 126137
     * enterNum : 79788
     * followNum : 79151
     * hasFollow : false
     */

    public int id;
    public String icon;
    public String background;
    public String activityIcon;
    public String title;
    public String intro;
    public int feedNum;
    public long tagId;
    public int enterNum;
    public int followNum;
    public boolean hasFollow;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !(obj instanceof TagList))
            return false;
        TagList newOne = (TagList) obj;
        return id == newOne.id
                && TextUtils.equals(icon, newOne.icon)
                && TextUtils.equals(background, newOne.background)
                && TextUtils.equals(activityIcon, newOne.activityIcon)
                && TextUtils.equals(title, newOne.title)
                && TextUtils.equals(intro, newOne.intro)
                && feedNum == newOne.feedNum
                && tagId == newOne.tagId
                && enterNum == newOne.enterNum
                && followNum == newOne.followNum
                && hasFollow == newOne.hasFollow;
    }

    @Bindable
    public boolean isHasFollow() {
        return hasFollow;
    }

    public void setHasFollow(boolean follow) {
        this.hasFollow = follow;
        notifyPropertyChanged(com.mooc.ppjoke.BR._all);
    }
}
