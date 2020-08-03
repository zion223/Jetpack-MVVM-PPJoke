package com.mooc.ppjoke.ui.state;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.mooc.libarchitecture.utils.Utils;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.domain.request.PublishRequest;

public class PublishViewModel extends ViewModel {

	public ObservableField<Boolean> addFile = new ObservableField<>();
	public ObservableField<Boolean> isVideo = new ObservableField<>();
	//上传文件路径
	public ObservableField<String> filePath = new ObservableField<>();

	//标签名
	public ObservableField<String> addTagText = new ObservableField<>();
	//发布内容 双向绑定
	public ObservableField<String> inputText = new ObservableField<>();

	public final PublishRequest publishRequest = new PublishRequest();

	{
		isVideo.set(false);
		addTagText.set(Utils.getApp().getResources().getString(R.string.publish_add_tag));
	}
}
