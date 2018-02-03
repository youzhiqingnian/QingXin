package com.qingxin.medical.user;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.base.QingXinModel;

/**
 * 运营官用户Model, 包括登录等
 *
 * @author zhikuo
 */
public class UserModel extends QingXinModel {

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    /**
     * @param user 用户
     */
    public void onLoginSuccess(@NonNull User user) {
        QingXinApplication.getInstance().saveLoginUser(user);
    }

    /**
     * 注销
     */
    public void onLogout() {
        QingXinApplication.getInstance().removeLoginUser();
    }
}
