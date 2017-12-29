package com.vlee78.android.vl;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.alibaba.sdk.android.oss.model.ResumableUploadRequest;
import com.alibaba.sdk.android.oss.model.ResumableUploadResult;

public class VLOSSModel extends VLModel {

    private OSS mOSS;

    @Override
    protected void onCreate() {
        super.onCreate();
        mOSS = null;
    }

    public boolean isConfigged() {
        return mOSS != null;
    }

    public void config(String ossEndPoint, final String accessKeyId, final String accessKeySecret) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        mOSS = new OSSClient(getConcretApplication(), ossEndPoint, credentialProvider, conf);

    }

    public String presignConstrainedObjectURL(String bucketName, String objectKey, long expiredTimeInSeconds) {
        try {
            return mOSS.presignConstrainedObjectURL(bucketName, objectKey, expiredTimeInSeconds);
        } catch (ClientException e) {
            VLDebug.logEx(Thread.currentThread(), e);
            return null;
        }
    }

    /**
     * 上传图片至阿里云
     *
     * @param bucketName       bucket名称
     * @param key              本地生成key
     * @param bytes            图片流
     * @param asyncHandler     回调
     */
    public void putBytes(String bucketName, String key, byte[] bytes, final VLAsyncHandler<String> asyncHandler) {
        putBytes(bucketName, key, bytes, null, asyncHandler);
    }

    /**
     * 上传图片至阿里云
     *
     * @param bucketName       bucket名称
     * @param key              本地生成key
     * @param bytes            图片流
     * @param progressCallback 进度条回调
     * @param asyncHandler     回调
     */
    public void putBytes(String bucketName, String key, byte[] bytes, final OSSProgressCallback<PutObjectRequest> progressCallback, final VLAsyncHandler<String> asyncHandler) {
        // 构造上传请求
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, bytes);
        if (!isConfigged()) {
            if (asyncHandler != null)
                asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, "oss client not config");
            return;
        }
        // 异步上传时可以设置进度回调
        if(progressCallback != null) {
            putObjectRequest.setProgressCallback(progressCallback);
        } else {
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                }
            });
        }

        mOSS.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (asyncHandler != null)
                    asyncHandler.handlerSuccess();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, serviceException.getMessage());
                }
            }
        });
        // task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); // 可以等待任务完成
    }

    public void putObject(String bucketName, String key, String uploadFilePath, final VLAsyncHandler<Object> asyncHandler) {
        putObject(bucketName, key, uploadFilePath, null, asyncHandler);
    }

    public void putObject(String bucketName, String key, String uploadFilePath, final OSSProgressCallback<PutObjectRequest> progressCallback, final VLAsyncHandler<Object> asyncHandler) {
        // 构造上传请求
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, uploadFilePath);
        if (!isConfigged()) {
            if (asyncHandler != null)
                asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, "oss client not config");
            return;
        }
        // 异步上传时可以设置进度回调
        if(progressCallback != null) {
            putObjectRequest.setProgressCallback(progressCallback);
        } else {
            putObjectRequest.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                }
            });
        }

        OSSAsyncTask task = mOSS.asyncPutObject(putObjectRequest, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (asyncHandler != null)
                    asyncHandler.handlerSuccess(result);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, serviceException.getMessage());
                }
            }
        });
        // task.cancel(); // 可以取消任务
        //task.waitUntilFinished(); // 可以等待任务完成
    }

    /**
     * 断点续传至阿里云
     *
     * @param bucketName     bucket名称
     * @param key            本地生成key
     * @param uploadFilePath 视频地址
     * @param asyncHandler   回调
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void putResumableObject(String bucketName, String key, String uploadFilePath, final OSSProgressCallback<ResumableUploadRequest> progressCallback, final VLAsyncHandler<Object> asyncHandler) {
        // 创建断点上传请求
        ResumableUploadRequest request = new ResumableUploadRequest(bucketName, key, uploadFilePath);
        // 设置上传过程回调
        if (progressCallback != null)
            request.setProgressCallback(progressCallback);
        // 异步调用断点上传
        OSSAsyncTask resumableTask = mOSS.asyncResumableUpload(request, new OSSCompletedCallback<ResumableUploadRequest, ResumableUploadResult>() {
            @Override
            public void onSuccess(ResumableUploadRequest request, ResumableUploadResult result) {
                if (asyncHandler != null)
                    asyncHandler.handlerSuccess(result);
            }

            @Override
            public void onFailure(ResumableUploadRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, clientExcepion.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResFailed, serviceException.getMessage());
                }
            }
        });
        //resumableTask.waitUntilFinished(); // 可以等待直到任务完成
    }
}
