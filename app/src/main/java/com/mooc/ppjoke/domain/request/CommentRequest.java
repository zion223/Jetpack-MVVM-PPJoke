package com.mooc.ppjoke.domain.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.libarchitecture.domain.request.BaseRequest;
import com.mooc.ppjoke.data.bean.Comment;
import com.mooc.ppjoke.data.repository.DataRepository;

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
		DataRepository.getInstance().addComment(itemId, commentText, isVideo, width, height, coverUrl, fileUrl, new DataResult<>((comment, netState, message) -> {
			commentStatus.setValue(comment);
			if (comment == null) {
				commentMessage.setValue("评论失败:" + message);
			}
		}));
	}
}
