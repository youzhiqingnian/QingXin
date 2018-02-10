package com.qingxin.medical.service;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailBean;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.vip.AmountBean;
import com.qingxin.medical.app.vip.VipDetailBean;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.base.ContentBean;

import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by user on 2018-01-22.
 */

public interface RetrofitService {

    /**
     * @param banner_size  banner图的尺寸
     * @param product_size 产品图的尺寸
     * @param diary_size   女神日记图的尺寸
     * @return
     */
    @GET("/home")
    Observable<ContentBean<HomeBean>> getHomeData(@Query("banner_size") String banner_size, @Query("product_size") String product_size, @Query("diary_size") String diary_size);

    /**
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    @GET("/diary")
    Observable<ContentBean<GoddessDiaryBean>> getGoddessDiaryList(@Query("limit") int limit, @Query("skip") int skip);

    /**
     * 获取女神日记详情
     *
     * @param id 日记的id
     * @return
     */
    @GET("/diary/{id}")
    Observable<ContentBean<GoddessDiaryDetailBean>> getGoddessDiaryDetail(@Path("id") String id);

    /**
     * 收藏女神日记
     *
     * @param id 日记的id
     * @return
     */
    @PUT("/diary/{id}/collect")
    Observable<ContentBean<CollectBean>> collectDiary(@Path("id") String id);


    /**
     * 获取歆人专享列表
     *
     * @param limit
     * @param skip
     * @param isvip
     * @param order
     * @return
     */
    @GET("/product")
    Observable<ContentBean<VipListBean>> getVipList(@Query("limit") int limit, @Query("skip") int skip, @Query("isvip") String isvip, @Query("order") String order);

    /**
     * 获取歆人专享详情
     *
     * @param id
     * @return
     */
    @GET("/product/{id}")
    Observable<ContentBean<VipDetailBean>> getVipDetail(@Path("id") String id);


    /**
     * 收藏歆人专享
     *
     * @param id 日记的id
     * @return
     */
    @PUT("/product/{id}/collect")
    Observable<ContentBean<CollectBean>> collectVip(@Path("id") String id);

    /**
     * 预定歆人专享
     *
     * @param id 日记的id
     * @return
     */
    @PUT("/product/{id}/book")
    Observable<ContentBean<AmountBean>> bookVip(@Path("id") String id);

}
