package com.mooc.ppjoke.ui.state;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.mooc.ppjoke.domain.request.CommentRequest;

public class CommentViewModel extends ViewModel {


	public final ObservableBoolean isVideo = new ObservableBoolean();
	public final ObservableBoolean showSoftInput = new ObservableBoolean();
	public final ObservableInt commentVideoImageAlpha = new ObservableInt();
	public final ObservableField<String> commentText = new ObservableField<>();
	public final ObservableField<String> filePath = new ObservableField<>();

	public final CommentRequest commentRequest = new CommentRequest();

	{
		commentVideoImageAlpha.set(255);
		showSoftInput.set(false);
		filePath.set(null);
		isVideo.set(false);
	}
}
