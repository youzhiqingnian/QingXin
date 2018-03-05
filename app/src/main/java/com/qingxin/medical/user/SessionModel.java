package com.qingxin.medical.user;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.base.QingXinModel;
/**
 * Date 2018-03-04
 *
 * @author zhikuo1
 */

public class SessionModel extends QingXinModel {

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    /**
     * @param session 用户
     */
    public void onLoginSuccess(@NonNull MemBean session) {
        QingXinApplication.getInstance().saveMemberBean(session);
    }

    /**
     * 注销
     */
    public void onLogout() {
        QingXinApplication.getInstance().removeLoginSession();
    }
}
