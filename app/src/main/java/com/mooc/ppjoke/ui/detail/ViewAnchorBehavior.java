package com.mooc.ppjoke.ui.detail;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.mooc.libcommon.utils.PixUtils;
import com.mooc.ppjoke.R;

public class ViewAnchorBehavior extends CoordinatorLayout.Behavior<View> {
    private int extraUsed;
    private int anchorId;

    public ViewAnchorBehavior() {

    }

    public ViewAnchorBehavior(Context context, AttributeSet attributeSet) {
        TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.view_anchor_behavior, 0, 0);
        anchorId = array.getResourceId(R.styleable.view_anchor_behavior_anchorId, 0);
        array.recycle();
        extraUsed = PixUtils.dp2px(48);
    }

    public ViewAnchorBehavior(int anchorId) {
        this.anchorId = anchorId;
        extraUsed = PixUtils.dp2px(48);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return anchorId == dependency.getId();
    }

    //CoordinatorLayout 在测量每一个view的时候 会调用该方法，如果返回true
    //CoordinatorLayout 就不会再次测量child了。会使用咱们给的测量的值 去摆放view的位置
    @Override
    public boolean onMeasureChild(@NonNull CoordinatorLayout parent,
                                  @NonNull View child,
                                  int parentWidthMeasureSpec,
                                  int widthUsed,
                                  int parentHeightMeasureSpec,
                                  int heightUsed) {


        View anchorView = parent.findViewById(anchorId);
        if (anchorView == null) {
            return false;
        }
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int topMargin = layoutParams.topMargin;
        int bottom = anchorView.getBottom();

        //再测量子View时，需要告诉CoordinatorLayout。垂直方向上 已经有多少空间被占用了
        //如果heightUsed给0，那么评论列表这个view它测量出来的高度 将会大于它实际的高度。以至于会被底部互动区域给遮挡
        heightUsed = bottom + topMargin + extraUsed;
        parent.onMeasureChild(child, parentWidthMeasureSpec, 0, parentHeightMeasureSpec, heightUsed);
        return true;
    }

    //当CoordinatorLayout 在摆放 每一个子View 的时候 会调该方法
    //如果return true.CoordinatorLayout 就不会再次摆放这个view的位置了
    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent,
                                 @NonNull View child,
                                 int layoutDirection) {
        View anchorView = parent.findViewById(anchorId);
        if (anchorView == null) {
            return false;
        }

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int topMargin = params.topMargin;
        int bottom = anchorView.getBottom();
        parent.onLayoutChild(child, layoutDirection);
        //偏移量
        child.offsetTopAndBottom(bottom + topMargin);


        return true;
    }
}
