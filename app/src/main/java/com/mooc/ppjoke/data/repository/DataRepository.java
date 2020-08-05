package com.mooc.ppjoke.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.libarchitecture.domain.manager.NetState;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.data.bean.Comment;
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
}
