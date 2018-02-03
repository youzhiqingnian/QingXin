package com.qingxin.medical.mine.login;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.user.UserTokenBean;

/**
 * @author zhikuo
 */
public class LoginContract {

    public interface LoginView extends BaseView<LoginPresenter> {

        void onSuccess(UserTokenBean userTokenBean);

        void onError(String errorCode, String message);

    }

    public interface LoginPresenter extends BasePresenter {
        void login(String mobile, String vcode);
    }
}
