package com.mooc.ppjoke.ui.binding_adpter;

import android.graphics.SurfaceTexture;
import android.view.TextureView;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;

import com.mooc.ppjoke.view.RecordView;

public class CaptureBindingAdapter {

	@BindingAdapter(value = {"onRecordListener"})
	public static void setImageUrl(RecordView view, RecordView.onRecordListener listener) {
		view.setOnRecordListener(listener);
	}

	@BindingAdapter("updatePreview")
	public static void updateView(TextureView view, SurfaceTexture surfaceTexture) {
		if (surfaceTexture != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
			parent.addView(view, 0);
			view.setSurfaceTexture(surfaceTexture);
		}
	}
}
