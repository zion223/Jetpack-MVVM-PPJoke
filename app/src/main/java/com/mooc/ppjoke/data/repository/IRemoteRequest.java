package com.mooc.ppjoke.data.repository;

import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.ppjoke.data.bean.Comment;

public interface IRemoteRequest {

	void addComment(long itemId, String commentText, boolean isVideo, int width, int height, String coverUrl, String fileUrl, DataResult<Comment> result);
}
