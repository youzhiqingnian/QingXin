package com.qingxin.medical.service;

import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.service.entity.Book;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
/**
 * Created by user on 2018-01-22.
 */

public interface RetrofitService {

    @GET("book/search")
    Observable<Book> getSearchBooks(@Query("q") String name,
                                    @Query("tag") String tag, @Query("start") int start,
                                    @Query("count") int count);

    /**
     * @param banner_size  banner图的尺寸
     * @param product_size 产品图的尺寸
     * @param diary_size   女神日记图的尺寸
     * @return
     */
    @GET("/home")
    Observable<HomeBean> getHomeData(@Query("banner_size") String banner_size, @Query("product_size") String product_size, @Query("diary_size") String diary_size);

    /**
     * @param limit 查询条数 非必填   默认值 10
     * @param skip  跳过第几条数据 非必填   默认值 0
     * @return
     */
    @GET("/diary")
    Observable<GoddessDiaryBean> getGoddessDiaryList(@Query("limit") String limit, @Query("skip") String skip);

}
