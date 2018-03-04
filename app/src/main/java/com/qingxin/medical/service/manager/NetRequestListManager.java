package com.qingxin.medical.service.manager;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailBean;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.app.vip.AmountBean;
import com.qingxin.medical.app.vip.VipDetailBean;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.service.RetrofitService;
import com.vlee78.android.vl.VLApplication;
import rx.Observable;

/**
 * Created by user on 2018-01-22.
 */

public class NetRequestListManager {

    /**
     * 获取首页数据
     *
     * @return
     */
    public static Observable<ContentBean<HomeBean>> getHomeData(String banner_size, String product_size, String diary_size) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getHomeData(banner_size, product_size, diary_size);
    }

    /**
     * 获取女神日记列表
     *
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    public static Observable<ContentBean<ListBean<DiaryItemBean>>> getGoddessDiary(int limit, int skip) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getGoddessDiaryList(limit, skip);
    }


    public static Observable<ContentBean<GoddessDiaryDetailBean>> getGoddessDiaryDetail(String id) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getGoddessDiaryDetail(id);
    }

    public static Observable<ContentBean<CollectBean>> collectDiary(String id) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).collectDiary(id);
    }

    public static Observable<ContentBean<VipListBean>> getVipList(int limit, int skip, String isvip, String order) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getVipList(limit, skip, isvip, order);
    }

    public static Observable<ContentBean<VipDetailBean>> getVipDetail(String id) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getVipDetail(id);
    }

    public static Observable<ContentBean<CollectBean>> collectVip(String id) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).collectVip(id);
    }

    public static Observable<ContentBean<AmountBean>> bookVip(String id) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).bookVip(id);
    }

    /**
     * 获取女神日记列表
     *
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    public static Observable<ContentBean<ListBean<ServiceBean>>> getExclusiveService(int limit, int skip) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getExclusiveService(limit, skip);
    }

    /**
     * 获取青歆币操作记录
     *
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    public static Observable<ContentBean<ListBean<CoinLogBean>>> getCoinLogList(int limit, int skip) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getCoinLogList(limit, skip);
    }

    /**
     * 每日签到
     *
     * @return
     */
    public static Observable<ContentBean<CheckInBean>> checkIn() {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).checkIn();
    }

    /**
     * 今日是否签到
     *
     * @return
     */
    public static Observable<ContentBean<MemBean>> isChcekIn() {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).isChcekIn();
    }

    /**
     * 推荐用户
     * @param name 用户姓名
     * @param mobile 用户手机号
     * @param product 意向产品
     * @param inthos 意向医院
     * @param remark 备注
     * @return
     */
    public static Observable<ContentBean<RecommendResultBean>> submitRecommendUser(String name, String mobile, String product, String inthos, String remark) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).submitRecommendUser(name, mobile, product, inthos, remark);
    }

    /**
     * 获取预定/收藏过的产品列表
     * @param limit
     * @param skip
     * @param type
     * @param actyp
     * @return
     */
    public static Observable<ContentBean<VipListBean>> getMyBookedProductList(int limit, int skip, String type, String actyp) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getMyBookedProductList(limit, skip, type, actyp);
    }

    /**
     * 获取收藏过的日记列表
     * @param limit
     * @param skip
     * @param type
     * @param actyp
     * @return
     */
    public static Observable<ContentBean<ListBean<DiaryItemBean>>> getMyCollectDiaryList(int limit, int skip, String type, String actyp) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getMyCollectDiaryList(limit, skip, type, actyp);
    }

    /**
     * 获取发布过的日记列表
     * @param author 发布者的id
     * @return
     */
    public static Observable<ContentBean<ListBean<DiaryItemBean>>> getMyPublishedDiaryList(String author, int limit, int skip) {
        return VLApplication.instance().getModel(RetrofitModel.class).getService(RetrofitService.class).getMyPublishedDiaryList(author,limit,skip);
    }

}
