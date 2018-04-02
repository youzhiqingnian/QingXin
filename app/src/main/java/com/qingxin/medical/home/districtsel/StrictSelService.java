package com.qingxin.medical.home.districtsel;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ItemBean;
import com.qingxin.medical.home.ListBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018/2/8
 *
 * @author zhikuo
 */

public interface StrictSelService {

    @GET("/preferr")
    Observable<ContentBean<ListBean<StrictSelBean>>> getStrictSelList(@Query("type") String type, @Query("limit") int limit, @Query("skip") int skip);

    /**
     * 获取严选详情
     *
     * @param id 严选id
     */
    @GET("/preferr/{id}")
    Observable<ContentBean<ItemBean<StrictSelBean>>> getStrictSelDetail(@Path("id") String id);
}
