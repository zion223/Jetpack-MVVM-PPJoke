package com.mooc.ppjoke.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.android.exoplayer2.SimpleExoPlayer;

public class PreviewViewModel extends ViewModel {

	public final ObservableField<String> btnText = new ObservableField<>();
	public final ObservableField<Boolean> isVideo = new ObservableField<>();
	//如果是图片文件
	public final ObservableField<String> previewUrl = new ObservableField<>();
	//视频播放器
	public final ObservableField<SimpleExoPlayer> player = new ObservableField<>();
}
