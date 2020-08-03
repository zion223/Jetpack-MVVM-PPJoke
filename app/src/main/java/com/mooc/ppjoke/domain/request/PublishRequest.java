package com.mooc.ppjoke.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSONObject;
import com.mooc.libarchitecture.domain.request.BaseRequest;
import com.mooc.libarchitecture.utils.Utils;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.model.Feed;
import com.mooc.ppjoke.model.TagList;
import com.mooc.ppjoke.ui.login.UserManager;

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
		ApiService.post("/feeds/publish")
				.addParam("coverUrl", coverUploadUrl)
				.addParam("fileUrl", fileUploadUrl)
				.addParam("fileWidth", width)
				.addParam("fileHeight", height)
				.addParam("userId", UserManager.get().getUserId())
				.addParam("tagId", taglist == null ? 0 : taglist.tagId)
				.addParam("tagTitle", taglist == null ? "" : taglist.title)
				.addParam("feedText", inputText)
				.addParam("feedType", isVideo ? Feed.TYPE_VIDEO : Feed.TYPE_IMAGE_TEXT)
				.execute(new JsonCallback<JSONObject>() {
					@Override
					public void onSuccess(ApiResponse<JSONObject> response) {
						publishMessage.setValue(Utils.getApp().getResources().getString(R.string.feed_publisj_success));
						publishStatus.postValue(true);
					}

					@Override
					public void onError(ApiResponse<JSONObject> response) {
						publishStatus.postValue(false);
						publishMessage.setValue((response.message));
					}
				});
	}
}
