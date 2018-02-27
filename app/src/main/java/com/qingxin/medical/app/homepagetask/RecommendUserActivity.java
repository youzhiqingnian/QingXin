package com.qingxin.medical.app.homepagetask;

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
import android.widget.Toast;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLApplication;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLToast;
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

    private EditText mUserNameEt, mCellphoneNumEt, mRemarkEt;

    private TextView mIntentionProgramEtTv, mIntentionHospitalTv;

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

        mUserNameEt = findViewById(R.id.userNameEt);
        mCellphoneNumEt = findViewById(R.id.cellphoneNumEt);
        mRemarkEt = findViewById(R.id.remarkEt);

        mIntentionProgramEtTv = findViewById(R.id.intentionProgramTv);
        mIntentionHospitalTv = findViewById(R.id.intentionHospitalTv);

        mEnsureSubmitTv = findViewById(R.id.ensureSubmitTv);


    }

    private void initListener() {
        mEnsureSubmitTv.setOnClickListener(this);

        mUserNameEt.addTextChangedListener(this);
        mCellphoneNumEt.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ensureSubmitTv:

                mPresenter.submitRecommendUser(mUserNameEt.getText().toString().trim(), mCellphoneNumEt.getText().toString().trim(), "c5e9d508-c0b5-4197-88f9-9f1bf0955075", mIntentionHospitalTv.getText().toString().trim(), mRemarkEt.getText().toString().trim());

                break;

        }


    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (checkEditTextNotEmpty()) {
            mEnsureSubmitTv.setBackground(getResources().getDrawable(R.drawable.recommend_submit_click));
            mEnsureSubmitTv.setEnabled(true);
        } else {
            mEnsureSubmitTv.setBackground(getResources().getDrawable(R.drawable.recommend_user_btn_unfill_bg));
            mEnsureSubmitTv.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private boolean checkEditTextNotEmpty() {
        return !VLUtils.stringIsEmpty(mUserNameEt.getText().toString().trim()) && !VLUtils.stringIsEmpty(mCellphoneNumEt.getText().toString().trim()) && VLUtils.stringValidatePhoneNumber(mCellphoneNumEt.getText().toString().trim()) && !VLUtils.stringIsEmpty(mIntentionProgramEtTv.getText().toString().trim()) && !VLUtils.stringIsEmpty(mIntentionHospitalTv.getText().toString().trim());
    }

    @Override
    public void setPresenter(RecommendUserContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(RecommendResultBean result) {
        Log.i("推荐用户的bean",result.toString());
        if(!VLUtils.stringIsEmpty(result.getId())){
            ToastUtils.showToast(getResources().getString(R.string.recommend_success));
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
