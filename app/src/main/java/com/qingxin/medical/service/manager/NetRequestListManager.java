package com.qingxin.medical.service.manager;

import android.content.Context;

import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.app.homepagetask.model.HomeBanner;
import com.qingxin.medical.app.homepagetask.model.HomeProduct;
import com.qingxin.medical.service.RetrofitHelper;
import com.qingxin.medical.service.RetrofitService;
import com.qingxin.medical.service.entity.Book;

import rx.Observable;

/**
 * Created by user on 2018-01-22.
 */

public class NetRequestListManager {

    public static Observable<Book> getSearchBooks(Context context,String name, String tag, int start, int count){
        return RetrofitHelper.getInstance(context).getServer().getSearchBooks(name,tag,start,count);
    }

    /**
     * 获取banner图
     * @param context
     * @param limit 查询条数 非必填   默认值 5
     * @return
     */
    public static Observable<HomeBanner> getBannerList(Context context,String limit){
        return RetrofitHelper.getInstance(context).getServer().getBannerList(limit);
    }

    /**
     * 获取女神日记列表
     * @param context
     * @param limit  查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    public static Observable<GoddessDiary> getGoddessDiary(Context context, String limit, String skip){
        return RetrofitHelper.getInstance(context).getServer().getGoddessDiaryList(limit,skip);
    }

    /**
     * 获取首页产品列表
     * @param limit 查询条数 非必填   默认值 10
     * @param skip 跳过第几条数据 非必填   默认值 0
     * @param isvip 是否专享（歆人专享 y 是 n 否）  非必填   默认值 无
     * @param order 排序规则（可选值：order 和 created_at）  非必填   默认值 按添加时间排序
     * @return
     */
    public static Observable<HomeProduct> getHomeProductList(Context context, String limit, String skip, String isvip, String order){
        return RetrofitHelper.getInstance(context).getServer().getHomeProductList(limit,skip,isvip,order);
    }

}
