package com.qingxin.medical.mine.login;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.user.UserTokenBean;

/**
 * @author zhikuo
 */
public class LoginContract {

    public interface LoginView extends BaseView<LoginPresenter> {

        void onSuccess(UserTokenBean userTokenBean);

        void onSuccess(MemBean memBean);

        void onError(String errorCode, String message);

    }

    public interface LoginPresenter extends BasePresenter {
        void login(@NonNull String mobile, @NonNull String vcode);
        void getSession();
        void getMoblieCode(@NonNull String mobile);
    }
}
