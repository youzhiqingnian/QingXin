package com.qingxin.medical.user;

import com.qingxin.medical.base.ContentBean;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zhikuo
 */
public interface UserService {

    /**
     * 登陆
     *
     * @param mobile 手机号
     * @param vcode  验证码
     */
    @GET("/session/login")
    Observable<ContentBean<UserTokenBean>> login(@Query("mobile") String mobile, @Query("vcode") String vcode);

    /**
     * 获取用户数据
     */
    @GET("/session")
    Observable<ContentBean<User>> getUserInfo();

    @GET("/session/yzm")
    Observable<ContentBean> getMobileCode(@Query("mobile") String mobile);
}
