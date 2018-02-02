package com.qingxin.medical.service.manager;

import android.content.Context;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.service.RetrofitHelper;
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
     * 获取首页数据
     * @param context
     * @return
     */
    public static Observable<HomeBean> getHomeData(Context context, String banner_size, String product_size, String diary_size){
        return RetrofitHelper.getInstance(context).getServer().getHomeData(banner_size,product_size,diary_size);
    }

    /**
     * 获取女神日记列表
     * @param context
     * @param limit  查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    public static Observable<GoddessDiaryBean> getGoddessDiary(Context context, String limit, String skip){
        return RetrofitHelper.getInstance(context).getServer().getGoddessDiaryList(limit,skip);
    }

}
