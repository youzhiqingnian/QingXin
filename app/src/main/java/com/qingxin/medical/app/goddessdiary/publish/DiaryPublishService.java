package com.qingxin.medical.app.goddessdiary.publish;

import com.qingxin.medical.base.ContentBean;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018/3/4
 *
 * @author zhikuo
 */
public interface DiaryPublishService {

    @POST("diary")
    Observable<ContentBean<DiaryPublishResult>> diaryPublish(@Query("product_id") String productId, @Query("oper_before_photo") String beforePhoto, @Query("oper_after_photo") String afterPhoto, @Query("words") String words);
}
