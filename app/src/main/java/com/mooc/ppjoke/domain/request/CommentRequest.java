package com.mooc.ppjoke.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mooc.libarchitecture.domain.request.BaseRequest;
import com.mooc.libnetwork.ApiResponse;
import com.mooc.libnetwork.ApiService;
import com.mooc.libnetwork.JsonCallback;
import com.mooc.ppjoke.model.Comment;
import com.mooc.ppjoke.ui.login.UserManager;

public class CommentRequest extends BaseRequest {

	public MutableLiveData<Comment> commentStatus;
	public MutableLiveData<String> commentMessage;

	public LiveData<Comment> getCommentStatus() {
		if (commentStatus == null) {
			commentStatus = new MutableLiveData<>();
		}
		return commentStatus;
	}

	public LiveData<String> getCommentMessage() {
		if (commentMessage == null) {
			commentMessage = new MutableLiveData<>();
		}
		return commentMessage;
	}

	public void requestComment(long itemId, String commentText, boolean isVideo, int width, int height, String coverUrl, String fileUrl) {
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
						commentStatus.setValue(response.body);
					}

					@Override
					public void onError(ApiResponse<Comment> response) {
						commentMessage.setValue("评论失败:" + response.message);
						commentStatus.setValue(null);
					}
				});
	}
}
