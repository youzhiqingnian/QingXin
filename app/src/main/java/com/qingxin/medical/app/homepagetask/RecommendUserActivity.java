package com.qingxin.medical.app.homepagetask;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;
/**
 * Date 2018-02-27
 *
 * @author zhikuo1
 */

public class RecommendUserActivity extends QingXinActivity implements RecommendUserContract.View, View.OnClickListener, TextWatcher {


    public static void startSelf(@NonNull VLActivity activity, @NonNull VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, RecommendUserActivity.class);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static final int REQUEST_CODE = 1002;

    private EditText mUserNameEt, mCellphoneNumEt, mRemarkEt, mIntentionProgramEt, mIntentionHospitalEt;

    private TextView mEnsureSubmitTv;

    private RecommendUserContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_user);

        mPresenter = new RecommendUserPresenter(this);

        initView();

        initListener();

    }

    private void initView() {

        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.recommend_user));
        QingXinTitleBar.setLeftReturn(titleBar, this);

        mUserNameEt = findViewById(R.id.userNameEt);
        mCellphoneNumEt = findViewById(R.id.cellphoneNumEt);
        mRemarkEt = findViewById(R.id.remarkEt);

        mIntentionProgramEt = findViewById(R.id.intentionProgramEt);
        mIntentionHospitalEt = findViewById(R.id.intentionHospitalEt);

        mEnsureSubmitTv = findViewById(R.id.ensureSubmitTv);


    }

    private void initListener() {
        mEnsureSubmitTv.setOnClickListener(this);

        mUserNameEt.addTextChangedListener(this);
        mCellphoneNumEt.addTextChangedListener(this);
        mIntentionProgramEt.addTextChangedListener(this);
        mIntentionHospitalEt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ensureSubmitTv:
                mPresenter.submitRecommendUser(mUserNameEt.getText().toString().trim(), mCellphoneNumEt.getText().toString().trim(), /*"c5e9d508-c0b5-4197-88f9-9f1bf0955075"*/mIntentionProgramEt.getText().toString().trim(), mIntentionHospitalEt.getText().toString().trim(), mRemarkEt.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (checkEditTextNotEmpty()) {
            mEnsureSubmitTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.recommend_submit_click));
            mEnsureSubmitTv.setEnabled(true);
        } else {
            mEnsureSubmitTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.recommend_user_btn_unfill_bg));
            mEnsureSubmitTv.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean checkEditTextNotEmpty() {
        return !VLUtils.stringIsEmpty(mUserNameEt.getText().toString().trim()) && !VLUtils.stringIsEmpty(mCellphoneNumEt.getText().toString().trim()) && VLUtils.stringValidatePhoneNumber(mCellphoneNumEt.getText().toString().trim()) && !VLUtils.stringIsEmpty(mIntentionProgramEt.getText().toString().trim()) && !VLUtils.stringIsEmpty(mIntentionHospitalEt.getText().toString().trim());
    }

    @Override
    public void setPresenter(RecommendUserContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(RecommendResultBean result) {
        Log.i("推荐用户的bean",result.toString());
        if(!VLUtils.stringIsEmpty(result.getId())){
            showToast(getResources().getString(R.string.recommend_success));
            setResult(Activity.RESULT_OK);
            this.finish();
        }
    }

    @Override
    public void onError(String result) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
