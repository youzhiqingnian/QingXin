package com.qingxin.medical.app.goddessdiary.publish;

import com.qingxin.medical.base.ContentBean;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Date 2018/3/4
 *
 * @author zhikuo
 */
public interface DiaryPublishService {

    @FormUrlEncoded
    @POST("/diary")
    Observable<ContentBean<DiaryPublishResult>> diaryPublish(@Field("wiki_id") String wikiId, @Field("oper_before_photo") String beforePhoto, @Field("oper_after_photo") String afterPhoto, @Field("words") String words);
}
