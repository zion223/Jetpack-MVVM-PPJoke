package com.mooc.ppjoke.ui.publish;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseActivity;
import com.mooc.libcommon.dialog.LoadingDialog;
import com.mooc.libcommon.utils.FileUtils;
import com.mooc.libcommon.utils.StatusBar;
import com.mooc.libnavannotation.ActivityDestination;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.data.bean.TagList;
import com.mooc.ppjoke.ui.state.PublishViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ActivityDestination(pageUrl = "main/tabs/publish", needLogin = true)
public class PublishActivity extends BaseActivity{


	private int width, height;
	private UUID coverUploadUUID, fileUploadUUID;
	private String coverUploadUrl, fileUploadUrl;

	private TagList mTagList;
	private PublishViewModel mPublishViewModel;

	@Override
	protected void initViewModel() {
		mPublishViewModel = getActivityViewModel(PublishViewModel.class);
	}

	@Override
	protected DataBindingConfig getDataBindingConfig() {
		return new DataBindingConfig(R.layout.activity_layout_publish, BR.vm, mPublishViewModel)
				.addBindingParam(BR.proxy, new ClickProxy());
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		StatusBar.fitSystemBar(this);
		super.onCreate(savedInstanceState);
		//发布状态
		mPublishViewModel.publishRequest.getPublishMessage().observe(this, this::showShortToast);
		mPublishViewModel.publishRequest.getPublishStatus().observe(this, status -> {
			dismissLoading();
			if (status) {
				finish();
			}
		});
	}


	public class ClickProxy {
		//关闭
		public void actionClose() {
			showExitDialog();
		}

		//发布帖子
		public void actionPublish() {
			publish();
		}

		//删除文件
		public void actionDeleteFile() {
			mPublishViewModel.filePath.set(null);
			mPublishViewModel.isVideo.set(false);
			mPublishViewModel.addFile.set(false);
			width = 0;
			height = 0;
		}
		//添加标签
		public void actionAddTag() {
			TagBottomSheetDialogFragment fragment = new TagBottomSheetDialogFragment();
			fragment.setOnTagItemSelectedListener(tagList -> {
				//设置  标签 标题
				mTagList = tagList;
				mPublishViewModel.addTagText.set(tagList.title);
			});
			fragment.show(getSupportFragmentManager(), "tag_dialog");
		}

		//添加文件 图片或视频
		public void actionAddFile() {
			CaptureActivity.startActivityForResult(PublishActivity.this);
		}

		public void previewCover(){
            PreviewActivity.startActivityForResult(PublishActivity.this, mPublishViewModel.filePath.get(), mPublishViewModel.isVideo.get(), null);
        }
	}


	private void publish() {
		showLoading();
		List<OneTimeWorkRequest> workRequests = new ArrayList<>();
		if (!TextUtils.isEmpty(mPublishViewModel.filePath.get())) {
			if (mPublishViewModel.isVideo.get()) {
				//生成视频封面文件
				FileUtils.generateVideoCover(mPublishViewModel.filePath.get()).observe(this, new Observer<String>() {
					@SuppressLint("RestrictedApi")
					@Override
					public void onChanged(String coverPath) {

						OneTimeWorkRequest request = getOneTimeWorkRequest(coverPath);
						coverUploadUUID = request.getId();
						workRequests.add(request);

						enqueue(workRequests);
					}
				});
			}
			OneTimeWorkRequest request = getOneTimeWorkRequest(mPublishViewModel.filePath.get());
			fileUploadUUID = request.getId();
			workRequests.add(request);
			//如果是视频文件则需要等待封面文件生成完毕后再一同提交到任务队列
			//否则 可以直接提交了
			if (!mPublishViewModel.isVideo.get()) {
				enqueue(workRequests);
			}
		} else {
			mPublishViewModel.publishRequest.requestPublish(coverUploadUrl, fileUploadUrl, width, height, mTagList, mPublishViewModel.inputText.get(), mPublishViewModel.isVideo.get());
		}
	}

	private void enqueue(List<OneTimeWorkRequest> workRequests) {
		WorkContinuation workContinuation = WorkManager.getInstance(PublishActivity.this).beginWith(workRequests);
		workContinuation.enqueue();

		workContinuation.getWorkInfosLiveData().observe(PublishActivity.this, new Observer<List<WorkInfo>>() {
			@Override
			public void onChanged(List<WorkInfo> workInfos) {
				//block runing enuqued failed susscess finish
				int completedCount = 0;
				int failedCount = 0;
				for (WorkInfo workInfo : workInfos) {
					WorkInfo.State state = workInfo.getState();
					Data outputData = workInfo.getOutputData();
					UUID uuid = workInfo.getId();
					if (state == WorkInfo.State.FAILED) {
						// if (uuid==coverUploadUUID)是错的
						if (uuid.equals(coverUploadUUID)) {
							showShortToast(getString(R.string.file_upload_cover_message));
						} else if (uuid.equals(fileUploadUUID)) {
							showShortToast(getString(R.string.file_upload_original_message));
						}
						failedCount++;
					} else if (state == WorkInfo.State.SUCCEEDED) {
						String fileUrl = outputData.getString("fileUrl");
						if (uuid.equals(coverUploadUUID)) {
							coverUploadUrl = fileUrl;
						} else if (uuid.equals(fileUploadUUID)) {
							fileUploadUrl = fileUrl;
						}
						completedCount++;
					}
				}

				if (completedCount >= workInfos.size()) {
					mPublishViewModel.publishRequest.requestPublish(coverUploadUrl, fileUploadUrl, width, height, mTagList, mPublishViewModel.inputText.get(), mPublishViewModel.isVideo.get());
				} else if (failedCount > 0) {
					dismissLoading();
				}
			}
		});
	}

	private LoadingDialog mLoadingDialog = null;

	private void showLoading() {
		if (mLoadingDialog == null) {
			mLoadingDialog = new LoadingDialog(this);
			mLoadingDialog.setLoadingText(getString(R.string.feed_publish_ing));
		}
		mLoadingDialog.show();
	}

	private void dismissLoading() {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			if (mLoadingDialog != null) {
				mLoadingDialog.dismiss();
			}
		} else {
			runOnUiThread(() -> {
				if (mLoadingDialog != null) {
					mLoadingDialog.dismiss();
				}
			});
		}
	}


	@SuppressLint("RestrictedApi")
	private OneTimeWorkRequest getOneTimeWorkRequest(String filePath) {
		Data inputData = new Data.Builder()
				.putString("file", filePath)
				.build();

//        @SuppressLint("RestrictedApi") Constraints constraints = new Constraints();
//        //设备存储空间充足的时候 才能执行 ,>15%
//        constraints.setRequiresStorageNotLow(true);
//        //必须在执行的网络条件下才能好执行,不计流量 ,wifi
//        constraints.setRequiredNetworkType(NetworkType.UNMETERED);
//        //设备的充电量充足的才能执行 >15%
//        constraints.setRequiresBatteryNotLow(true);
//        //只有设备在充电的情况下 才能允许执行
//        constraints.setRequiresCharging(true);
//        //只有设备在空闲的情况下才能被执行 比如息屏，cpu利用率不高
//        constraints.setRequiresDeviceIdle(true);
//        //workmanager利用contentObserver监控传递进来的这个uri对应的内容是否发生变化,当且仅当它发生变化了
//        //我们的任务才会被触发执行，以下三个api是关联的
//        constraints.setContentUriTriggers(null);
//        //设置从content变化到被执行中间的延迟时间，如果在这期间。content发生了变化，延迟时间会被重新计算
		//这个content就是指 我们设置的setContentUriTriggers uri对应的内容
//        constraints.setTriggerContentUpdateDelay(0);
//        //设置从content变化到被执行中间的最大延迟时间
		//这个content就是指 我们设置的setContentUriTriggers uri对应的内容
//        constraints.setTriggerMaxContentDelay(0);
		OneTimeWorkRequest request = new OneTimeWorkRequest
				.Builder(UploadFileWorker.class)
				.setInputData(inputData)
//                .setConstraints(constraints)
//                //设置一个拦截器，在任务执行之前 可以做一次拦截，去修改入参的数据然后返回新的数据交由worker使用
//                .setInputMerger(null)
//                //当一个任务被调度失败后，所要采取的重试策略，可以通过BackoffPolicy来执行具体的策略
//                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
//                //任务被调度执行的延迟时间
//                .setInitialDelay(10, TimeUnit.SECONDS)
//                //设置该任务尝试执行的最大次数
//                .setInitialRunAttemptCount(2)
//                //设置这个任务开始执行的时间
//                //System.currentTimeMillis()
//                .setPeriodStartTime(0, TimeUnit.SECONDS)
//                //指定该任务被调度的时间
//                .setScheduleRequestedAt(0, TimeUnit.SECONDS)
//                //当一个任务执行状态编程finish时，又没有后续的观察者来消费这个结果，难么workamnager会在
//                //内存中保留一段时间的该任务的结果。超过这个时间，这个结果就会被存储到数据库中
//                //下次想要查询该任务的结果时，会触发workmanager的数据库查询操作，可以通过uuid来查询任务的状态
//                .keepResultsForAtLeast(10, TimeUnit.SECONDS)
				.build();
		return request;
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CaptureActivity.REQ_CAPTURE && data != null) {
			width = data.getIntExtra(CaptureActivity.RESULT_FILE_WIDTH, 0);
			height = data.getIntExtra(CaptureActivity.RESULT_FILE_HEIGHT, 0);
			String filePath = data.getStringExtra(CaptureActivity.RESULT_FILE_PATH);
			boolean isVideo = data.getBooleanExtra(CaptureActivity.RESULT_FILE_TYPE, false);

			mPublishViewModel.addFile.set(true);
			mPublishViewModel.isVideo.set(isVideo);
			mPublishViewModel.filePath.set(filePath);
		}
	}

	//退出编辑
	private void showExitDialog() {

		new AlertDialog.Builder(this)
				.setMessage(getString(R.string.publish_exit_message))
				.setNegativeButton(getString(R.string.publish_exit_action_cancel), null)
				.setPositiveButton(getString(R.string.publish_exit_action_ok), (dialog, which) -> {
					dialog.dismiss();
					PublishActivity.this.finish();
				}).create().show();
	}
}
