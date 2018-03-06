package com.qingxin.medical.mine;

import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public interface ModifyPersonalInfoService {
    @FormUrlEncoded
    @POST("/diary")
    Observable<ContentBean<MemBean>> modifyPersonalInfo(@Field("cover") String wikiId, @Field("oper_before_photo") String beforePhoto, @Field("oper_after_photo") String afterPhoto, @Field("words") String words);


}
