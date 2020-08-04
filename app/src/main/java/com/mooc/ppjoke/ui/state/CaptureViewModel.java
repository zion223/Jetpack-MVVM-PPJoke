package com.mooc.ppjoke.ui.state;

import android.graphics.SurfaceTexture;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;


public class CaptureViewModel extends ViewModel {


	public final ObservableBoolean tipVisibility = new ObservableBoolean();

	public final ObservableField<SurfaceTexture> surfaceTexture = new ObservableField<>();

	{
		tipVisibility.set(true);
	}
}
