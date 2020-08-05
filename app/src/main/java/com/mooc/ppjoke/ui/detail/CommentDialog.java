package com.mooc.ppjoke.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libcommon.dialog.LoadingDialog;
import com.mooc.libcommon.global.AppGlobals;
import com.mooc.libcommon.utils.FileUploadManager;
import com.mooc.libcommon.utils.FileUtils;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.data.bean.Comment;
import com.mooc.ppjoke.ui.detail.base.DialogDataBindingFragment;
import com.mooc.ppjoke.ui.publish.CaptureActivity;
import com.mooc.ppjoke.ui.state.CommentViewModel;

import java.util.concurrent.atomic.AtomicInteger;

public class CommentDialog extends DialogDataBindingFragment {
	private long itemId;
	private CommentAddListener mListener;
	private static final String KEY_ITEM_ID = "key_item_id";
	private String filePath;
	private int width, height;
	private boolean isVideo;
	private String coverUrl;
	private String fileUrl;
	private LoadingDialog loadingDialog;

	private CommentViewModel mCommentViewModel;

	public static CommentDialog newInstance(long itemId) {

		Bundle args = new Bundle();
		args.putLong(KEY_ITEM_ID, itemId);
		CommentDialog fragment = new CommentDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected void initViewModel() {
		mCommentViewModel = getFragmentViewModel(CommentViewModel.class);
	}

	@Override
	protected DataBindingConfig getDataBindingConfig() {
		return new DataBindingConfig(R.layout.layout_comment_dialog, BR.vm, mCommentViewModel)
				.addBindingParam(BR.proxy, new ClickProxy());
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.itemId = getArguments().getLong(KEY_ITEM_ID);

		mCommentViewModel.commentRequest.getCommentMessage().observe(this, this::showShortToast);

		mCommentViewModel.commentRequest.getCommentStatus().observe(this, comment -> {
			if (comment != null) {
				onCommentSuccess(comment);
			}
			dismissLoadingDialog();
		});
	}


	@Override
	protected void postMethod() {
		mCommentViewModel.showSoftInput.set(true);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CaptureActivity.REQ_CAPTURE && resultCode == Activity.RESULT_OK) {
			filePath = data.getStringExtra(CaptureActivity.RESULT_FILE_PATH);
			width = data.getIntExtra(CaptureActivity.RESULT_FILE_WIDTH, 0);
			height = data.getIntExtra(CaptureActivity.RESULT_FILE_HEIGHT, 0);
			isVideo = data.getBooleanExtra(CaptureActivity.RESULT_FILE_TYPE, false);

			mCommentViewModel.filePath.set(filePath);
			mCommentViewModel.isVideo.set(isVideo);
			mCommentViewModel.commentVideoImageAlpha.set(80);
		}
	}

	private void publishComment() {

		if (TextUtils.isEmpty(mCommentViewModel.commentText.get())) {
			return;
		}

		if (isVideo && !TextUtils.isEmpty(filePath)) {
			FileUtils.generateVideoCover(filePath).observe(this, coverPath -> uploadFile(coverPath, filePath));
		} else if (!TextUtils.isEmpty(filePath)) {
			uploadFile(null, filePath);
		} else {
			mCommentViewModel.commentRequest.requestComment(itemId, mCommentViewModel.commentText.get(), isVideo, width, height, coverUrl, fileUrl);
		}
	}

	private void uploadFile(String coverPath, String filePath) {
		//AtomicInteger, CountDownLatch, CyclicBarrier
		showLoadingDialog();
		AtomicInteger count = new AtomicInteger(1);
		if (!TextUtils.isEmpty(coverPath)) {
			count.set(2);
			ArchTaskExecutor.getIOThreadExecutor().execute(() -> {
				int remain = count.decrementAndGet();
				coverUrl = FileUploadManager.upload(coverPath);
				if (remain <= 0) {
					if (!TextUtils.isEmpty(fileUrl) && !TextUtils.isEmpty(coverUrl)) {
						mCommentViewModel.commentRequest.requestComment(itemId, mCommentViewModel.commentText.get(), isVideo, width, height, coverUrl, fileUrl);
					} else {
						dismissLoadingDialog();
						showToast(getString(R.string.file_upload_failed));
					}
				}
			});
		}
		ArchTaskExecutor.getIOThreadExecutor().execute(() -> {
			int remain = count.decrementAndGet();
			fileUrl = FileUploadManager.upload(filePath);
			if (remain <= 0) {
				if (!TextUtils.isEmpty(fileUrl) || !TextUtils.isEmpty(coverPath) && !TextUtils.isEmpty(coverUrl)) {
					mCommentViewModel.commentRequest.requestComment(itemId, mCommentViewModel.commentText.get(), isVideo, width, height, coverUrl, fileUrl);
				} else {
					dismissLoadingDialog();
					showToast(getString(R.string.file_upload_failed));
				}
			}
		});

	}


	private void showLoadingDialog() {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(getContext());
			loadingDialog.setLoadingText(getString(R.string.upload_text));
			loadingDialog.setCanceledOnTouchOutside(false);
			loadingDialog.setCancelable(false);
		}
		if (!loadingDialog.isShowing()) {
			loadingDialog.show();
		}
	}

	private void dismissLoadingDialog() {
		if (loadingDialog != null) {
			//dismissLoadingDialog  的调用可能会出现在异步线程调用
			if (Looper.myLooper() == Looper.getMainLooper()) {
				ArchTaskExecutor.getMainThreadExecutor().execute(() -> {
					if (loadingDialog != null && loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				});
			} else if (loadingDialog.isShowing()) {
				loadingDialog.dismiss();
			}
		}
	}

	private void onCommentSuccess(Comment body) {
		showToast("评论发布成功");
		ArchTaskExecutor.getMainThreadExecutor().execute(() -> {
			if (mListener != null) {
				mListener.onAddComment(body);
			}
			dismiss();
		});
	}

	@Override
	public void dismiss() {
		super.dismiss();
		dismissLoadingDialog();
		filePath = null;
		fileUrl = null;
		coverUrl = null;
		isVideo = false;
		width = 0;
		height = 0;
	}

	private void showToast(String s) {
		//showToast几个可能会出现在异步线程调用
		if (Looper.myLooper() == Looper.getMainLooper()) {
			Toast.makeText(AppGlobals.getApplication(), s, Toast.LENGTH_SHORT).show();
		} else {
			ArchTaskExecutor.getMainThreadExecutor().execute(() -> Toast.makeText(AppGlobals.getApplication(), s, Toast.LENGTH_SHORT).show());
		}
	}

	public interface CommentAddListener {
		void onAddComment(Comment comment);
	}

	public void setCommentAddListener(CommentAddListener listener) {
		mListener = listener;
	}


	public class ClickProxy {

		public void send() {
			publishComment();
		}

		public void video() {
			CaptureActivity.startActivityForResult(getActivity());
		}

		public void delete() {
			filePath = null;
			isVideo = false;
			width = 0;
			height = 0;
			mCommentViewModel.filePath.set(null);
			mCommentViewModel.commentVideoImageAlpha.set(255);
		}

	}
}
