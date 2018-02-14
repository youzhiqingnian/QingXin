package com.qingxin.medical.home.medicalbeauty;

import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ItemBean;
import com.qingxin.medical.home.ListBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date 2018/2/8
 *
 * @author zhikuo
 */

public interface MedicalStrictService {

    @GET("/wiki")
    Observable<ContentBean<ListBean<MedicalBeautyListBean>>> getMedicalBeautyList(@Query("parent") String parent);

    @GET("/wiki/{id}")
    Observable<ContentBean<ItemBean<MedicalBeautyRealDetailBean>>> getMedicalBeautyDetail(@Path("id") String id);

    @GET("/wiki")
    Observable<ContentBean<ListBean<MedicalBeautyDetailBean>>> getMedicalBeautySecondaryList(@Query("parent") String parent, @Query("with") String children);
}
