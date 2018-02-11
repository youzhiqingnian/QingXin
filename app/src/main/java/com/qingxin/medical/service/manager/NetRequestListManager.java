package com.qingxin.medical.service.manager;

import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.app.vip.AmountBean;
import com.qingxin.medical.app.vip.VipDetailBean;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.base.ContentBean;
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

}
