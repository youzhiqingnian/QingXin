package com.qingxin.medical.upload;

import com.qingxin.medical.base.ContentBean;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 上传文件Service
 *
 * Date 2018/3/4
 *
 * @author zhikuo
 */
public interface UploadService {

    @Multipart
    @POST("upload")
    Observable<ContentBean<UploadResult>> uploadFile(@Part MultipartBody.Part file);

}
