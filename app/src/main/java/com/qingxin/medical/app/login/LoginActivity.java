package com.qingxin.medical.app.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.mine.login.LoginContract;
import com.qingxin.medical.mine.login.LoginPresenter;
import com.qingxin.medical.user.UserModel;
import com.qingxin.medical.user.UserTokenBean;

/**
 * Created by zhikuo1 on 2018-02-03.
 */
public class LoginActivity extends QingXinActivity implements View.OnClickListener, LoginContract.LoginView{

    public boolean isLogin;
    public LoginPresenter mLoginPresenter;
    public Button mLoginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mLoginTv = findViewById(R.id.loginTv);
        mLoginTv.setOnClickListener(this);

        mLoginPresenter = new LoginPresenter(this);
        initLoginStatus();

    }

    private void initLoginStatus(){
        isLogin = QingXinApplication.getInstance().getLoginUser() != null;
        mLoginTv.setText(isLogin ? "退出登陆" : "登陆");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTv:
                if (isLogin) {
                    getModel(UserModel.class).onLogout();
                    initLoginStatus();
                    showToast("退出登陆");
                } else {//登陆
                    mLoginPresenter.login("17090117341", "1111");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void setPresenter(LoginContract.LoginPresenter presenter) {

    }

    @Override
    public void onSuccess(UserTokenBean userTokenBean) {
        userTokenBean.getMem().setToken(userTokenBean.getToken());
        getModel(UserModel.class).onLoginSuccess(userTokenBean.getMem());
        initLoginStatus();
        showToast("登陆成功");
        finish();
    }

    @Override
    public void onError(String errorCode, String message) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginPresenter.unsubscribe();
    }

}
