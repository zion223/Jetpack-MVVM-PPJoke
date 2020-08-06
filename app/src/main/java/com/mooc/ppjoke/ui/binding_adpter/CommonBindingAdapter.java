package com.mooc.ppjoke.ui.binding_adpter;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.BindingAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.material.appbar.AppBarLayout;
import com.mooc.libarchitecture.utils.ClickUtils;
import com.mooc.libarchitecture.utils.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class CommonBindingAdapter {


	@BindingAdapter(value = {"imageUrl"}, requireAll = false)
	public static void setImageUrl(PhotoView photoView, String imageUrl) {
		Glide.with(photoView.getContext()).load(imageUrl).into(photoView);
	}

	@BindingAdapter(value = {"player"}, requireAll = false)
	public static void setPlayer(PlayerView playerview, SimpleExoPlayer player) {
		playerview.setPlayer(player);
	}

	@BindingAdapter(value = {"offsetChangeListener"}, requireAll = false)
	public static void setOffsetChangeListener(AppBarLayout appBarLayout, AppBarLayout.OnOffsetChangedListener offsetChangeListener) {
		appBarLayout.addOnOffsetChangedListener(offsetChangeListener);
	}

	@BindingAdapter(value = {"onRefreshListener"}, requireAll = false)
	public static void setOffsetChangeListener(SmartRefreshLayout refreshLayout, OnRefreshListener listener) {
		refreshLayout.setOnRefreshListener(listener);
	}

	@BindingAdapter(value = {"onLoadMoreListener"}, requireAll = false)
	public static void setOnLoadMoreListener(SmartRefreshLayout refreshLayout, OnLoadMoreListener listener) {
		refreshLayout.setOnLoadMoreListener(listener);
	}

	@BindingAdapter(value = {"currentItem"}, requireAll = false)
	public static void bindCurrentItem(ViewPager2 viewPager2, int currentItem) {
		viewPager2.setCurrentItem(currentItem, true);
	}

	@BindingAdapter(value = {"onClickWithDebouncing"}, requireAll = false)
	public static void onClickWithDebouncing(View view, View.OnClickListener clickListener) {
		ClickUtils.applySingleDebouncing(view, clickListener);
	}

	@BindingAdapter(value = "showSoftInputMethod")
	public static void showSoftInputMethod(AppCompatEditText editText, boolean show) {
		if (show) {
			editText.setFocusable(true);
			editText.setFocusableInTouchMode(true);
			//请求获得焦点
			editText.requestFocus();
			InputMethodManager manager = (InputMethodManager) Utils.getApp().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.showSoftInput(editText, 0);
		}
	}

	@BindingAdapter(value = {"alpha"}, requireAll = false)
	public static void setImageRes(ImageView imageView, int alpha) {
		imageView.setAlpha(alpha);
	}

	@BindingAdapter(value = {"enabled"}, requireAll = false)
	public static void setImageRes(ImageView imageView, boolean enabled) {
		imageView.setEnabled(enabled);
	}
}
