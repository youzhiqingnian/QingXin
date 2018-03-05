package com.qingxin.medical.app.login;

import android.os.Bundle;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.mine.login.LoginFragment;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class LoginActivity extends QingXinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD

        dealIntent();

        setContentView(R.layout.activity_login);

        mLoginTv = findViewById(R.id.loginTv);
        mLoginTv.setOnClickListener(this);
        TextView codeTv = findViewById(R.id.codeTv);
        mCodeEt = findViewById(R.id.codeEt);
        mPhoneEt = findViewById(R.id.phoneEt);
        codeTv.setOnClickListener(this);
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
            intent.putExtra("loginrefresh", true);
        } else {
            intent.putExtra("position", currentFgPosition);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
                    ToastUtils.showToast("退出登陆");
                } else {//登陆
                    if (isChecked()) {
                        showView(R.layout.layout_loading);
                        mLoginPresenter.login(mPhoneEt.getText().toString().trim(), mCodeEt.getText().toString().trim());
                    }
                }
                break;
            case R.id.codeTv://获取验证码
                if (isCheckedCode()) {
                    mLoginPresenter.getMoblieCode(mPhoneEt.getText().toString().trim());
                }
                break;
            default:
                break;
        }
    }

    private boolean isCheckedCode() {
        if (VLUtils.stringIsEmpty(mPhoneEt.getText().toString().trim())) {
            ToastUtils.showToast("请输入手机号");
            return false;
        }
        if (!VLUtils.stringValidatePhoneNumber(mPhoneEt.getText().toString().trim())) {
            ToastUtils.showToast("请输入正确的手机号");
            return false;
        }
        return true;
    }

    private boolean isChecked() {
        if (VLUtils.stringValidatePhoneNumber(mCodeEt.getText().toString().trim())) {
            ToastUtils.showToast("请输入验证码");
            return false;
        }
        return true;
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
        ToastUtils.showToast("登陆成功");
        mLoginPresenter.getSession();

    }

    @Override
    public void onSuccess(MemBean memBean) {
        Log.i("session的bean", memBean.toString());
        if (memBean != null) {
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

=======
        setContentView(R.layout.activity_fragment);
        getSupportFragmentManager().beginTransaction().add(R.id.container, LoginFragment.newInstance(true)).commitAllowingStateLoss();
    }
>>>>>>> 398116c6002fdfbf8d1b270b2d799692cbb5844e
}
