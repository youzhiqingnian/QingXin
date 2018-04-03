package com.qingxin.medical.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.CommonDialogFactory;
import com.qingxin.medical.common.MapperUtils;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.common.QingXinLocalPhotoPopupWindow;
import com.qingxin.medical.upload.UploadResult;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;
import java.io.File;
/**
 * Date 2018-04-02
 *
 * @author zhikuo1
 */
public class PersonalInformationActivity extends QingXinActivity implements View.OnClickListener, PersonalInformationDataContract.View {

    private FrameLayout mModifyHeadFl,
            mGenderFl,
            mBirthdayFl,
            mRegionFl;
    private SimpleDraweeView mUserHeadSdv;
    private EditText mNicknameEt;
    private TextView mGenderTv,
                    mBirthdayTv,
                    mReginTv,
                    mCellphoneTv;
    private View mBottomV;
    private Bitmap mBeforePhoto;
    private String mBeforePhotoPath;
    private DiaryPublishParams mDiaryPublishParams;
    private PersonalInformationDataPresenter mPresenter;
    private String mUploadHeadFileName = "";
    private String mStartName;
    private String mStartGender;
    private String mStartBirthday;
    private String mStartRegion;

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, PersonalInformationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mNicknameEt = findViewById(R.id.nicknameEt);
        mGenderTv = findViewById(R.id.genderTv);
        mBirthdayTv = findViewById(R.id.birthdayTv);
        mReginTv = findViewById(R.id.reginTv);
        mCellphoneTv = findViewById(R.id.cellphoneTv);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.my_information));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        QingXinTitleBar.setRightText(titleBar, getResources().getString(R.string.save), view -> {
            if(isChanged()){
                mPresenter.modifyPersonalInfo(mNicknameEt.getText().toString().trim(),mUploadHeadFileName,mGenderTv.getText().toString().trim(),mBirthdayTv.getText().toString().trim(),"","");
            }
        });
        mModifyHeadFl = findViewById(R.id.modifyHeadFl);
        mGenderFl = findViewById(R.id.genderFl);
        mBirthdayFl = findViewById(R.id.birthdayFl);
        mRegionFl = findViewById(R.id.regionFl);
        mUserHeadSdv = findViewById(R.id.userHeadSdv);
        mBottomV = findViewById(R.id.bottomV);
        mPresenter = new PersonalInformationDataPresenter(this);
        mDiaryPublishParams = new DiaryPublishParams();
    }

    private void initListener() {
        mModifyHeadFl.setOnClickListener(this);
        mGenderFl.setOnClickListener(this);
        mBirthdayFl.setOnClickListener(this);
        mRegionFl.setOnClickListener(this);
    }

    private void initData() {
        mStartName = mNicknameEt.getText().toString().trim();
        mStartGender = mGenderTv.getText().toString().trim();
        mStartBirthday = mBirthdayTv.getText().toString().trim();
        mStartRegion = mReginTv.getText().toString().trim();
    }

    private boolean isChanged() {
        String endName = mNicknameEt.getText().toString().trim();
        String endGender = mGenderTv.getText().toString().trim();
        String endBirthday = mBirthdayTv.getText().toString().trim();
        String endRegion = mReginTv.getText().toString().trim();
        return MapperUtils.isChanged(mStartName, endName) || MapperUtils.isChanged(mStartGender, endGender) || MapperUtils.isChanged(mStartBirthday, endBirthday) || MapperUtils.isChanged(mStartRegion, endRegion);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.modifyHeadFl:
                // 修改头像
                getPhotoPopupWindow().show(mBottomV);
                break;
            case R.id.genderFl:
                // 性别

                break;
            case R.id.birthdayFl:
                // 生日

                break;
            case R.id.regionFl:
                // 地区

                break;

        }
    }

    private QingXinLocalPhotoPopupWindow getPhotoPopupWindow() {
        return CommonDialogFactory.createLoadLocalPhotoPopupWindow(this, true, new VLAsyncHandler<QingXinLocalPhotoPopupWindow.LoadPhotoResult>(null, VLScheduler.THREAD_MAIN) {
            @Override
            protected void handler(boolean succeed) {
                if (!succeed) return;
                QingXinLocalPhotoPopupWindow.LoadPhotoResult result = getParam();
                if (null == result) return;
                mBeforePhoto = result.getCutResult();
                mBeforePhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mBeforePhoto);
                assert mBeforePhotoPath != null;
                mDiaryPublishParams.setBeforeFile(new File(mBeforePhotoPath));
                mPresenter.headUpload(mDiaryPublishParams);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(PersonalInformationDataContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(MemBean membean) {
        // 修改个人资料成功
        if (membean != null) {
            /*mPresenter.getSession();*/
            if (!VLUtils.stringIsEmpty(membean.getCover())) {
                mUserHeadSdv.setImageURI(Uri.parse(membean.getCover()));
            }
        }
    }

    @Override
    public void onSuccess(com.qingxin.medical.base.MemBean memBean) {
        if (!VLUtils.stringIsEmpty(memBean.getMem().getCover())) {
            mUserHeadSdv.setImageURI(Uri.parse(memBean.getMem().getCover()));
        }
    }

    @Override
    public void onSuccess(UploadResult uploadResultBean) {
        // 上传头像成功
        if(!TextUtils.isEmpty(uploadResultBean.getFilename())){
            mUploadHeadFileName = uploadResultBean.getFilename();
        }
    }

    @Override
    public void onError(QingXinError error) {

    }
}
