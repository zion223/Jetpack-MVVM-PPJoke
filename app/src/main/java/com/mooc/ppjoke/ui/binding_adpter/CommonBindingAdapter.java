package com.mooc.ppjoke.ui.binding_adpter;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class CommonBindingAdapter {

	@BindingAdapter(value = {"imageUrl"}, requireAll = false)
	public static void setImageUrl(PhotoView photoView, String imageUrl) {
		Glide.with(photoView.getContext()).load(imageUrl).into(photoView);
	}

	@BindingAdapter(value = {"player"}, requireAll = false)
	public static void setPlayer(PlayerView playerview, SimpleExoPlayer player) {
		playerview.setPlayer(player);
	}
}
