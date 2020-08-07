package com.mooc.ppjoke.ui.publish;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.core.UseCase;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.VideoCaptureConfig;
import androidx.core.app.ActivityCompat;

import com.kunminx.architecture.ui.page.DataBindingConfig;
import com.mooc.libarchitecture.ui.page.BaseActivity;
import com.mooc.ppjoke.BR;
import com.mooc.ppjoke.R;
import com.mooc.ppjoke.ui.state.CaptureViewModel;
import com.mooc.ppjoke.view.RecordView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaptureActivity extends BaseActivity {
	public static final int REQ_CAPTURE = 10001;
	private static final String[] PERMISSIONS = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
	private static final int PERMISSION_CODE = 1000;
	private ArrayList<String> deniedPermission = new ArrayList<>();
	private CameraX.LensFacing mLensFacing = CameraX.LensFacing.BACK;
	private int rotation = Surface.ROTATION_0;
	private Size resolution = new Size(1280, 720);
	private Rational rational = new Rational(9, 16);
	private ImageCapture imageCapture;
	private VideoCapture videoCapture;
	private boolean takingPicture;
	private String outputFilePath;
	public static final String RESULT_FILE_PATH = "file_path";
	public static final String RESULT_FILE_WIDTH = "file_width";
	public static final String RESULT_FILE_HEIGHT = "file_height";
	public static final String RESULT_FILE_TYPE = "file_type";

	private CaptureViewModel mCaptureViewModel;

	public static void startActivityForResult(Activity activity) {
		Intent intent = new Intent(activity, CaptureActivity.class);
		activity.startActivityForResult(intent, REQ_CAPTURE);
	}

	@Override
	protected void initViewModel() {
		mCaptureViewModel = getActivityViewModel(CaptureViewModel.class);
	}

	@Override
	protected DataBindingConfig getDataBindingConfig() {
		return new DataBindingConfig(R.layout.activity_layout_capture, BR.vm, mCaptureViewModel)
				.addBindingParam(BR.listener, new RecordListener());
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CODE);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PreviewActivity.REQ_PREVIEW && resultCode == RESULT_OK) {
			Intent intent = new Intent();
			intent.putExtra(RESULT_FILE_PATH, outputFilePath);
			//当设备处于竖屏情况时，宽高的值 需要互换，横屏不需要
			intent.putExtra(RESULT_FILE_WIDTH, resolution.getHeight());
			intent.putExtra(RESULT_FILE_HEIGHT, resolution.getWidth());
			intent.putExtra(RESULT_FILE_TYPE, !takingPicture);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	private void onFileSaved(File file) {
		outputFilePath = file.getAbsolutePath();
		String mimeType = takingPicture ? "image/jpeg" : "video/mp4";
		MediaScannerConnection.scanFile(this, new String[]{outputFilePath}, new String[]{mimeType}, null);
		//拍照或者录像完成打开预览界面
		PreviewActivity.startActivityForResult(this, outputFilePath, !takingPicture, "完成");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_CODE) {
			deniedPermission.clear();
			for (int i = 0; i < permissions.length; i++) {
				String permission = permissions[i];
				int result = grantResults[i];
				if (result != PackageManager.PERMISSION_GRANTED) {
					deniedPermission.add(permission);
				}
			}

			if (deniedPermission.isEmpty()) {
				bindCameraX();
			} else {
				new AlertDialog.Builder(this)
						.setMessage(getString(R.string.capture_permission_message))
						.setNegativeButton(getString(R.string.capture_permission_no), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								CaptureActivity.this.finish();
							}
						})
						.setPositiveButton(getString(R.string.capture_permission_ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String[] denied = new String[deniedPermission.size()];
								ActivityCompat.requestPermissions(CaptureActivity.this, deniedPermission.toArray(denied), PERMISSION_CODE);
							}
						}).create().show();
			}
		}
	}

	@SuppressLint("RestrictedApi")
	private void bindCameraX() {
		CameraX.unbindAll();

		//查询一下当前要使用的设备摄像头(比如后置摄像头)是否存在
		boolean hasAvailableCameraId = false;
		try {
			hasAvailableCameraId = CameraX.hasCameraWithLensFacing(mLensFacing);
		} catch (CameraInfoUnavailableException e) {
			e.printStackTrace();
		}

		if (!hasAvailableCameraId) {
			showShortToast("无可用的设备cameraId!,请检查设备的相机是否被占用");
			finish();
			return;
		}

		//查询一下是否存在可用的cameraId.形式如：后置："0"，前置："1"
		String cameraIdForLensFacing = null;
		try {
			cameraIdForLensFacing = CameraX.getCameraFactory().cameraIdForLensFacing(mLensFacing);
		} catch (CameraInfoUnavailableException e) {
			e.printStackTrace();
		}
		if (TextUtils.isEmpty(cameraIdForLensFacing)) {
			showShortToast("无可用的设备cameraId!,请检查设备的相机是否被占用");
			finish();
			return;
		}

		PreviewConfig config = new PreviewConfig.Builder()
				//前后摄像头
				.setLensFacing(mLensFacing)
				//旋转角度
				.setTargetRotation(rotation)
				//分辨率
				.setTargetResolution(resolution)
				//宽高比
				.setTargetAspectRatio(rational)
				.build();
		Preview preview = new Preview(config);

		imageCapture = new ImageCapture(new ImageCaptureConfig.Builder()
				.setTargetAspectRatio(rational)
				.setTargetResolution(resolution)
				.setLensFacing(mLensFacing)
				.setTargetRotation(rotation).build());

		videoCapture = new VideoCapture(new VideoCaptureConfig.Builder()
				.setTargetRotation(rotation)
				.setLensFacing(mLensFacing)
				.setTargetResolution(resolution)
				.setTargetAspectRatio(rational)
				//视频帧率
				.setVideoFrameRate(25)
				//bit率
				.setBitRate(3 * 1024 * 1024).build());

		preview.setOnPreviewOutputUpdateListener(output -> mCaptureViewModel.surfaceTexture.set(output.getSurfaceTexture()));

		//上面配置的都是我们期望的分辨率
		List<UseCase> newUseList = new ArrayList<>();
		newUseList.add(preview);
		newUseList.add(imageCapture);
		newUseList.add(videoCapture);
		//下面我们要查询一下 当前设备它所支持的分辨率有哪些，然后再更新一下 所配置的几个usecase
		Map<UseCase, Size> resolutions = CameraX.getSurfaceManager().getSuggestedResolutions(cameraIdForLensFacing, null, newUseList);
		for (Map.Entry<UseCase, Size> next : resolutions.entrySet()) {
			UseCase useCase = next.getKey();
			Size value = next.getValue();
			Map<String, Size> update = new HashMap<>();
			update.put(cameraIdForLensFacing, value);
			useCase.updateSuggestedResolution(update);
		}
		CameraX.bindToLifecycle(this, preview, imageCapture, videoCapture);
	}

	@Override
	protected void onDestroy() {
		CameraX.unbindAll();
		super.onDestroy();
	}

	public class RecordListener implements RecordView.onRecordListener {

		@Override
		public void onClick() {
			takingPicture = true;
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".jpeg");
			mCaptureViewModel.tipVisibility.set(false);
			imageCapture.takePicture(file, new ImageCapture.OnImageSavedListener() {
				@Override
				public void onImageSaved(@NonNull File file) {
					onFileSaved(file);
				}

				@Override
				public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
					showShortToast(message);
				}
			});
		}

		@SuppressLint("RestrictedApi")
		@Override
		public void onLongClick() {
			takingPicture = false;
			File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".mp4");
			videoCapture.startRecording(file, new VideoCapture.OnVideoSavedListener() {
				@Override
				public void onVideoSaved(File file) {
					onFileSaved(file);
				}

				@Override
				public void onError(VideoCapture.UseCaseError useCaseError, String message, @Nullable Throwable cause) {
					showShortToast(message);
				}
			});
		}

		@SuppressLint("RestrictedApi")
		@Override
		public void onFinish() {
			videoCapture.stopRecording();
		}
	}
}

