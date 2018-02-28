package com.qingxin.medical.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.mine.login.LoginContract;
import com.qingxin.medical.mine.login.LoginPresenter;
import com.qingxin.medical.user.UserModel;
import com.qingxin.medical.user.UserTokenBean;

/**
 * 首页我的界面
 *
 * @author zhikuo
 */
public class MineFragment extends QingXinFragment implements View.OnClickListener, LoginContract.LoginView {

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    public boolean isLogin;
    public LoginPresenter mLoginPresenter;
    public Button mLoginTv;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;
        mLoginTv = getView().findViewById(R.id.loginTv);
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
                    mLoginPresenter.login("18311370117", "1111");
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
        Log.i("登录成功的bean",userTokenBean.toString());
        userTokenBean.getMem().setToken(userTokenBean.getToken());
        getModel(UserModel.class).onLoginSuccess(userTokenBean.getMem());
        initLoginStatus();
        showToast("登陆成功");
    }

    @Override
    public void onError(String errorCode, String message) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLoginPresenter.unsubscribe();
    }

}
