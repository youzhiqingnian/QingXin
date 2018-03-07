package com.qingxin.medical.config;

import com.qingxin.medical.base.ContentBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Date 2018/3/7
 *
 * @author zhikuo
 */
public interface ConfigService {

    @GET("/global/config")
    Observable<ContentBean<ConfigBean>> getConfig();
}
