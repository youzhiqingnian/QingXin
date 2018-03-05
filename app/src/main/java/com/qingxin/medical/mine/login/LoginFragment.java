package com.qingxin.medical.mine.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingxin.medical.R;
import com.qingxin.medical.app.login.CountDownUtils;
import com.qingxin.medical.base.MemBean;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.user.SessionModel;
import com.qingxin.medical.user.UserModel;
import com.qingxin.medical.user.UserTokenBean;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Date 2018/3/5
 *
 * @author zhikuo
 */

public class LoginFragment extends QingXinFragment implements View.OnClickListener, LoginContract.LoginView {

    public static LoginFragment newInstance(boolean showLeftReturn) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_LEFT_RETURN, showLeftReturn);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    private static final String SHOW_LEFT_RETURN = "showLeftReturn";

    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    public LoginPresenter mLoginPresenter;
    private EditText mCodeEt, mPhoneEt;
    private CountDownUtils mCountDownUtils;
    public static final String LOGIN_ACTION = "com.archie.action.LOGIN_ACTION";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == getView()) return;
        TextView loginTv = getView().findViewById(R.id.loginTv);
        TextView codeTv = getView().findViewById(R.id.getCodeTv);
        mCodeEt = getView().findViewById(R.id.codeEt);
        mPhoneEt = getView().findViewById(R.id.phoneEt);
        mLoginPresenter = new LoginPresenter(this);
        mCountDownUtils = new CountDownUtils(getActivity(), codeTv);
        boolean showLeftReturn = getArguments().getBoolean(SHOW_LEFT_RETURN);
        ImageView backIv = getView().findViewById(R.id.backIv);
        if (showLeftReturn){
            backIv.setOnClickListener(this);
        }else {
            backIv.setVisibility(View.GONE);
        }
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(mPhoneEt);
        editTexts.add(mCodeEt);
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean allInputHasContent = true;
                    for (EditText editText1 : editTexts) {
                        allInputHasContent = editText1.getText().length() > 0;
                        if (!allInputHasContent) break;
                    }
                    loginTv.setBackgroundResource(allInputHasContent ? R.drawable.ripple_login_click_bg : R.drawable.ripple_login_no_click_bg);
                    loginTv.setEnabled(allInputHasContent);
                    loginTv.setTextColor(allInputHasContent ? 0xff3bc5e8 : 0x4dffffff);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
        codeTv.setOnClickListener(this);
        loginTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backIv:
                getActivity().finish();
                break;
            case R.id.loginTv://登陆
                if (isChecked()) {
                    showView(R.layout.layout_loading);
                    mLoginPresenter.login(mPhoneEt.getText().toString().trim(), mCodeEt.getText().toString().trim());
                }
                break;
            case R.id.getCodeTv://获取验证码
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
        userTokenBean.getMem().setToken(userTokenBean.getToken());
        getModel(UserModel.class).onLoginSuccess(userTokenBean.getMem());
        ToastUtils.showToast("登陆成功");
        mLoginPresenter.getSession();
    }

    @Override
    public void onSuccess(MemBean memBean) {
        if (memBean != null) {
            // 获取到了session的bean
            getModel(SessionModel.class).onLoginSuccess(memBean);
            Intent intent = new Intent(LOGIN_ACTION);
            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        }
    }

    @Override
    public void onGetMobileCodeSuccess() {
        ToastUtils.showToast("验证码发送成功，请查收");
        mCountDownUtils.startCountDown();
    }

    @Override
    public void onError(String errorCode, String message) {
        hideView(R.layout.layout_loading);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLoginPresenter.unsubscribe();
        mCountDownUtils.endCountDown();
    }
}
