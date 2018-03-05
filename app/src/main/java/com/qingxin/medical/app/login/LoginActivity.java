package com.qingxin.medical.app.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.qingxin.medical.R;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.mine.login.LoginContract;
import com.qingxin.medical.mine.login.LoginPresenter;
import com.qingxin.medical.user.SessionModel;
import com.qingxin.medical.user.UserModel;
import com.qingxin.medical.user.UserTokenBean;
/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class LoginActivity extends QingXinActivity implements View.OnClickListener, LoginContract.LoginView {

    public boolean isLogin;
    public LoginPresenter mLoginPresenter;
    public Button mLoginTv;

    public static final String LOGIN_ACTION = "com.archie.action.LOGIN_ACTION";
    private boolean homeFlag;
    private int currentFgPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dealIntent();

        setContentView(R.layout.activity_login);

        mLoginTv = findViewById(R.id.loginTv);
        mLoginTv.setOnClickListener(this);

        mLoginPresenter = new LoginPresenter(this);
        initLoginStatus();

    }

    private void dealIntent() {

        homeFlag = getIntent().getBooleanExtra("home", false);
        currentFgPosition = getIntent().getIntExtra("position", 0);

    }

    private void initLoginStatus() {
        isLogin = QingXinApplication.getInstance().getLoginUser() != null;
        mLoginTv.setText(isLogin ? "退出登陆" : "登陆");
    }

    private void sendBroadCast(int flag) {
        Intent intent = new Intent(LOGIN_ACTION);
        if (flag == 1) {
            intent.putExtra("refresh", true);
            intent.putExtra("position", 1);
        } else {
            intent.putExtra("position", currentFgPosition);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(
                intent
        );
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginTv:
                if (isLogin) {
                    getModel(UserModel.class).onLogout();
                    getModel(SessionModel.class).onLogout();
                    initLoginStatus();
                    showToast("退出登陆");
                } else {//登陆
                    showView(R.layout.layout_loading);
                    mLoginPresenter.login("13803396510", "1111");
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
        hideView(R.layout.layout_loading);
        Log.i("登录成功的bean", userTokenBean.toString());
        userTokenBean.getMem().setToken(userTokenBean.getToken());
        getModel(UserModel.class).onLoginSuccess(userTokenBean.getMem());
        initLoginStatus();
        showToast("登陆成功");
        mLoginPresenter.getSession();

    }

    @Override
    public void onSuccess(MemBean memBean) {
        if(memBean != null){
            // 获取到了session的bean
            getModel(SessionModel.class).onLoginSuccess(memBean);
            sendBroadCast(1);
        }
    }

    @Override
    public void onError(String errorCode, String message) {
        hideView(R.layout.layout_loading);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginPresenter.unsubscribe();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (homeFlag) {
                sendBroadCast(0);
            } else {
                finish();
            }

        }
        return true;
    }

}
