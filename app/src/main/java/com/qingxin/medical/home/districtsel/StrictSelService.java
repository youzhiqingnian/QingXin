package com.qingxin.medical.home.districtsel;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018/2/8
 *
 * @author zhikuo
 */

public interface StrictSelService {

    /*@GET("/preferr/{type}")
    Observable<ContentBean<ListBean<StrictSelBean>>> getStrictSelList(@Path("type") String type, int limit, int skip);*/

    @GET("/preferr")
    Observable<ContentBean<ListBean<StrictSelBean>>> getStrictSelList(@Query("limit") int limit, @Query("skip") int skip);
}
