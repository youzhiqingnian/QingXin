package com.qingxin.medical.mine;

import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;
import java.util.Map;
import retrofit2.http.FieldMap;
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
    Observable<ContentBean<MemBean>> modifyPersonalInfo(@FieldMap Map<String, String> map);

}
