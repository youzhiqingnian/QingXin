package com.qingxin.medical.mine;

import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import rx.Observable;
/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public interface ModifyPersonalInfoService {

    @FormUrlEncoded
    @PUT("/mem")
    Observable<ContentBean<MemBean>> modifyPersonalInfo(@Field("name") String name, @Field("cover") String cover, @Field("gender") String gender, @Field("birthday") String birthday, @Field("province_id") String province_id, @Field("city_id") String city_id);

}
