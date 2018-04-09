package com.qingxin.medical.update;

import com.qingxin.medical.base.ContentBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018/4/9
 *
 * @author zhikuo
 */
public interface AppUpdateService {

    @GET("/global/apk")
    Observable<ContentBean<AppUpdateBean>> getReleaseVersion(@Query("version") String version);
}
