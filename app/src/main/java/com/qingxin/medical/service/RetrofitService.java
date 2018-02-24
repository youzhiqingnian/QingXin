package com.qingxin.medical.service;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailBean;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.app.vip.AmountBean;
import com.qingxin.medical.app.vip.VipDetailBean;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;

import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018-01-22
 *
 * @author zhikuo
 */
public interface RetrofitService {

    /**
     * @param banner_size  banner图的尺寸
     * @param product_size 产品图的尺寸
     * @param diary_size   女神日记图的尺寸
     */
    @GET("/home")
    Observable<ContentBean<HomeBean>> getHomeData(@Query("banner_size") String banner_size, @Query("product_size") String product_size, @Query("diary_size") String diary_size);

    /**
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     */
    @GET("/diary")
    Observable<ContentBean<ListBean<DiaryItemBean>>> getGoddessDiaryList(@Query("limit") int limit, @Query("skip") int skip);

    /**
     * 获取女神日记详情
     *
     * @param id 日记的id
     */
    @GET("/diary/{id}")
    Observable<ContentBean<GoddessDiaryDetailBean>> getGoddessDiaryDetail(@Path("id") String id);

    /**
     * 收藏女神日记
     *
     * @param id 日记的id
     */
    @PUT("/diary/{id}/collect")
    Observable<ContentBean<CollectBean>> collectDiary(@Path("id") String id);


    /**
     * 获取歆人专享列表
     */
    @GET("/product")
    Observable<ContentBean<VipListBean>> getVipList(@Query("limit") int limit, @Query("skip") int skip, @Query("isvip") String isvip, @Query("order") String order);

    /**
     * 获取歆人专享详情
     */
    @GET("/product/{id}")
    Observable<ContentBean<VipDetailBean>> getVipDetail(@Path("id") String id);


    /**
     * 收藏歆人专享
     *
     * @param id 日记的id
     */
    @PUT("/product/{id}/collect")
    Observable<ContentBean<CollectBean>> collectVip(@Path("id") String id);

    /**
     * 预定歆人专享
     *
     * @param id 日记的id
     */
    @PUT("/product/{id}/book")
    Observable<ContentBean<AmountBean>> bookVip(@Path("id") String id);


    /**
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     */
    @GET("/escort")
    Observable<ContentBean<ListBean<ServiceBean>>> getExclusiveService(@Query("limit") int limit, @Query("skip") int skip);

    /**
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @param actions 类型（增加的（+） 减少的（-）
     * @param use 用途 管理员操作的（admin）签到（checkin）
     * @return
     */
    @GET("/mem/coinlogs")
    Observable<ContentBean<ListBean<CoinLogBean>>> getCoinLogList(@Query("limit") int limit, @Query("skip") int skip, @Query("actions") String actions, @Query("use") String use);

    /**
     * 每日签到
     *
     * @return
     */
    @GET("/mem/checkin")
    Observable<ContentBean<CheckInBean>> checkIn();


}