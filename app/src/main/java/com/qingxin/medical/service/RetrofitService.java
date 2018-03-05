package com.qingxin.medical.service;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailBean;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.app.vip.AmountBean;
import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.app.vip.VipDetailBean;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.home.ListBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

   /* *//**
     * 预定歆人专享
     *
     * @param id 日记的id
     *//*
    @PUT("/product/{id}/book")
    Observable<ContentBean<AmountBean>> bookVip(@Path("id") String id);*/

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
     * @return
     */
    @GET("/mem/coinlogs")
    Observable<ContentBean<ListBean<CoinLogBean>>> getCoinLogList(@Query("limit") int limit, @Query("skip") int skip);

    /**
     * 每日签到
     *
     * @return
     */
    @GET("/mem/checkin")
    Observable<ContentBean<CheckInBean>> checkIn();

    /**
     * 今日是否签到
     *
     * @return
     */
    @GET("/session")
    Observable<ContentBean<MemBean>> isChcekIn();

    /**
     * 推荐用户
     * @param name 用户姓名
     * @param mobile 用户手机号
     * @param product 意向产品
     * @param inthos 意向医院
     * @param remark 备注
     * @return
     */
    @FormUrlEncoded
    @POST("/mem/recomem")
    Observable<ContentBean<RecommendResultBean>> submitRecommendUser(@Field("name")String name, @Field("mobile")String mobile, @Field("product")String product, @Field("inthos")String inthos, @Field("remark")String remark);

    /**
     * 获取我的预定/收藏过的产品
     */
    @GET("/mem/act")
    Observable<ContentBean<ProductListBean>> getMyBookedProductList(@Query("limit") int limit, @Query("skip") int skip, @Query("type") String type, @Query("actyp") String actyp);

    /**
     * 获取我收藏过的日记
     */
    @GET("/mem/act")
    Observable<ContentBean<ListBean<DiaryItemBean>>> getMyCollectDiaryList(@Query("limit") int limit, @Query("skip") int skip, @Query("type") String type, @Query("actyp") String actyp);

    /**
     * 获取我发布过的日记
     */
    @GET("/diary")
    Observable<ContentBean<ListBean<DiaryItemBean>>> getMyPublishedDiaryList(@Query("author") String author, @Query("limit") int limit, @Query("skip") int skip);


}