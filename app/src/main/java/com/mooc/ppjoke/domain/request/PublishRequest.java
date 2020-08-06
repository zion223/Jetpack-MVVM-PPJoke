package com.mooc.ppjoke.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.libarchitecture.domain.request.BaseRequest;
import com.mooc.ppjoke.data.bean.TagList;
import com.mooc.ppjoke.data.repository.DataRepository;

public class PublishRequest extends BaseRequest {


	public MutableLiveData<Boolean> publishStatus;
	public MutableLiveData<String> publishMessage;

	public LiveData<Boolean> getPublishStatus() {
		if (publishStatus == null) {
			publishStatus = new MutableLiveData<>();
		}
		return publishStatus;
	}

	public LiveData<String> getPublishMessage() {
		if (publishMessage == null) {
			publishMessage = new MutableLiveData<>();
		}
		return publishMessage;
	}

	public void requestPublish(String coverUploadUrl, String fileUploadUrl, int width, int height, TagList taglist, String inputText, boolean isVideo) {
		DataRepository.getInstance().publish(coverUploadUrl, fileUploadUrl, width, height, taglist, inputText, isVideo, new DataResult<>(((jsonObject, netState, message) -> {
			publishStatus.setValue(jsonObject != null);
			publishMessage.setValue(message);
		})));
	}
}
