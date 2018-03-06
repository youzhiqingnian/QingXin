package com.qingxin.medical.mine;

import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;

import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import rx.Observable;
/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public interface ModifyPersonalInfoService {

    @Multipart
    @PUT("/mem")
    Observable<ContentBean<MemBean>> modifyPersonalInfo(@Part("cover") String cover);

}
