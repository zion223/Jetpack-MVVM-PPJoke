package com.mooc.ppjoke.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSONObject;
import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.libarchitecture.domain.manager.NetState;
import com.mooc.libarchitecture.utils.Utils;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.data.bean.Comment;
import com.mooc.ppjoke.data.bean.Feed;
import com.mooc.ppjoke.data.bean.TagList;
import com.mooc.ppjoke.ui.login.UserManager;

public class DataRepository implements ILocalRequest, IRemoteRequest {

	private static final DataRepository S_REQUEST_MANAGER = new DataRepository();
	private MutableLiveData<String> responseCodeLiveData;

	private DataRepository() {
	}

	public static DataRepository getInstance() {
		return S_REQUEST_MANAGER;
	}

	@Override
	public void addComment(long itemId, String commentText, boolean isVideo, int width, int height, String coverUrl, String fileUrl, DataResult<Comment> result) {
		ApiService.post("/comment/addComment")
				.addParam("userId", UserManager.get().getUserId())
				.addParam("itemId", itemId)
				.addParam("commentText", commentText)
				.addParam("image_url", isVideo ? coverUrl : fileUrl)
				.addParam("video_url", isVideo ? fileUrl : null)
				.addParam("width", width)
				.addParam("height", height)
				.execute(new JsonCallback<Comment>() {
					@Override
					public void onSuccess(ApiResponse<Comment> response) {
						result.setResult(response.body, new NetState(), response.message);
					}

					@Override
					public void onError(ApiResponse<Comment> response) {
						result.setResult(null, new NetState(), response.message);
					}
				});
	}

	@Override
	public void publish(String coverUploadUrl, String fileUploadUrl, int width, int height, TagList taglist, String inputText, boolean isVideo, DataResult<JSONObject> result) {
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
						result.setResult(response.body, new NetState(), Utils.getApp().getResources().getString(R.string.feed_publisj_success));
					}

					@Override
					public void onError(ApiResponse<JSONObject> response) {
						result.setResult(null, new NetState(), response.message);
					}
				});
	}
}
