package com.qingxin.medical.common;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018/4/8
 *
 * @author zhikuo
 */
public interface DistrictService {

    @GET("/global/region")
    Observable<ContentBean<ListBean<DistrictItemData>>> getDistrictData(@Query("parent") String parent, @Query("level") String level);

    @GET("/global/region")
    Observable<ContentBean<ListBean<DistrictItemData>>> getDistrictData(@Query("level") String level);
}
