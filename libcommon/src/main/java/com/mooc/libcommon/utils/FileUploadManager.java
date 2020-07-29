package com.mooc.libcommon.utils;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.mooc.libcommon.global.AppGlobals;

/**
 * 阿里云oss 文件上传
 */
public class FileUploadManager {
    private static OSSClient oss = null;
    private static final String ALIYUN_BUCKET_URL = "https://pipijoke.oss-cn-hangzhou.aliyuncs.com/";
    private static final String BUCKET_NAME = "pipijoke";
    private static final String END_POINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String AUTH_SERVER_URL = "http://123.56.232.18:7080/";

    static {

        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(AUTH_SERVER_URL);
        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.disableLog(); //这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv

        oss = new OSSClient(AppGlobals.getApplication(), END_POINT, credentialProvider, conf);
    }

    //同步
    public static String upload(byte[] bytes) throws ClientException, ServiceException {
        String objectKey = String.valueOf(System.currentTimeMillis());
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, bytes);
        PutObjectResult result = oss.putObject(request);
        if (result.getStatusCode() == 200) {
            return ALIYUN_BUCKET_URL + objectKey;
        } else {
            return null;
        }
    }

    //异步
    public static void upload(byte[] bytes, UploadCallback callback) {
        String objectKey = String.valueOf(System.currentTimeMillis());
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, bytes);
        upload(request, callback);
    }

    //同步
    public static String upload(String filePath) {
        String objectKey = filePath.substring(filePath.lastIndexOf("/") + 1);
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, filePath);
        PutObjectResult result = null;
        try {
            result = oss.putObject(request);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if (result != null && result.getStatusCode() == 200) {
            return ALIYUN_BUCKET_URL + objectKey;
        } else {
            return null;
        }
    }

    //异步
    public static void upload(String filePath, UploadCallback callback) {
        String objectKey = filePath.substring(filePath.lastIndexOf("/") + 1);
        PutObjectRequest request = new PutObjectRequest(BUCKET_NAME, objectKey, filePath);
        upload(request, callback);
    }

    private static void upload(final PutObjectRequest put, final UploadCallback callback) {
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                String eTag = result.getETag();
                String serverCallbackReturnBody = result.getServerCallbackReturnBody();
                Log.e("PutObject", "UploadSuccess" + eTag + "--" + serverCallbackReturnBody);
                if (callback != null && result.getStatusCode() == 200) {
                    callback.onUpload(ALIYUN_BUCKET_URL + put.getObjectKey());
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                printError(clientExcepion, serviceException);
                if (callback != null) {
                    callback.onError(serviceException.getRawMessage());
                }
            }
        });
    }

    public void download(String url, final String filePath, final DownloadCallback callback) {
        // 构造下载文件请求
        GetObjectRequest get = new GetObjectRequest(BUCKET_NAME, url);
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                Log.d("Content-Length", "" + result.getContentLength());
                //FileUtil.SaveFile(filePath, result.getObjectContent());
                if (callback != null) {
                    callback.onDownloadSuccess(filePath);
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                printError(clientExcepion, serviceException);
                if (callback != null) {
                    callback.onError(serviceException.getRawMessage());
                }
            }
        });

    }

    private static void printError(ClientException clientExcepion, ServiceException serviceException) {
        // 请求异常
        if (clientExcepion != null) {
            // 本地异常如网络异常等
            clientExcepion.printStackTrace();
        }
        if (serviceException != null) {
            // 服务异常
            Log.e("ErrorCode", serviceException.getErrorCode());
            Log.e("RequestId", serviceException.getRequestId());
            Log.e("HostId", serviceException.getHostId());
            Log.e("RawMessage", serviceException.getRawMessage());
        }
    }

    public interface UploadCallback {
        void onUpload(String result);

        void onError(String error);
    }

    public interface DownloadCallback {
        void onDownloadSuccess(String fileUrl);

        void onError(String error);
    }
}
