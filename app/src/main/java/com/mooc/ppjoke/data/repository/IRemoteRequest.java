package com.mooc.ppjoke.data.repository;

import com.alibaba.fastjson.JSONObject;
import com.mooc.libarchitecture.data.repository.DataResult;
import com.mooc.ppjoke.data.bean.Comment;
import com.mooc.ppjoke.data.bean.TagList;

public interface IRemoteRequest {

	void addComment(long itemId, String commentText, boolean isVideo, int width, int height, String coverUrl, String fileUrl, DataResult<Comment> result);

	void publish(String coverUploadUrl, String fileUploadUrl, int width, int height, TagList taglist, String inputText, boolean isVideo, DataResult<JSONObject> result);
}
