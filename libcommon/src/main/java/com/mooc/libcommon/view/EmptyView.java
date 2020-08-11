package com.mooc.libcommon.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.mooc.libcommon.R;

public class EmptyView extends LinearLayout {
    private ImageView mIvIcon;
    private TextView mTvTitle;
    private Button mBtAction;

    public EmptyView(@NonNull Context context) {
        this(context, null);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int style) {
        super(context, attrs, defStyleAttr, style);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater.from(context).inflate(R.layout.layout_empty_view, this, true);

        mIvIcon = findViewById(R.id.empty_icon);
        mTvTitle = findViewById(R.id.empty_text);
        mBtAction = findViewById(R.id.empty_action);
    }


    public void setEmptyIcon(@DrawableRes int iconRes) {
        mIvIcon.setImageResource(iconRes);
    }

    public void setTitle(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvTitle.setVisibility(GONE);
        } else {
            mTvTitle.setText(text);
            mTvTitle.setVisibility(VISIBLE);
        }
    }


    @BindingAdapter(value = {"emptyViewTitle", "emptyViewButtonTitle", "emptyViewButtonListener"})
    public static void setEmptyViewTitle(EmptyView view, String text, String title, OnClickListener listener) {
        view.setTitle(text);
        view.initButton(title, listener);
    }

    public void initButton(String text, View.OnClickListener listener) {
        if (TextUtils.isEmpty(text)) {
            mBtAction.setVisibility(GONE);
        } else {
            mBtAction.setText(text);
            mBtAction.setVisibility(VISIBLE);
            mBtAction.setOnClickListener(listener);
        }

    }
}
