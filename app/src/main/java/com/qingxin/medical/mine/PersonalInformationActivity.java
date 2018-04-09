package com.qingxin.medical.mine;

import android.annotation.SuppressLint;
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
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.CommonDialogAdapter;
import com.qingxin.medical.common.CommonDialogFactory;
import com.qingxin.medical.common.DistrictContract;
import com.qingxin.medical.common.DistrictItemData;
import com.qingxin.medical.common.DistrictPresenter;
import com.qingxin.medical.common.MapperUtils;
import com.qingxin.medical.common.ProvinceData;
import com.qingxin.medical.common.QingXinDatePopuWindow;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.common.QingXinLocalPhotoPopupWindow;
import com.qingxin.medical.common.QingXinLocationPopupWindow;
import com.qingxin.medical.config.ConfigModel;
import com.qingxin.medical.upload.UploadResult;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLResHandler;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Date 2018-04-02
 *
 * @author zhikuo1
 */
public class PersonalInformationActivity extends QingXinActivity implements View.OnClickListener, PersonalInformationDataContract.View {

    private SimpleDraweeView mUserHeadSdv;
    private EditText mNicknameEt;
    private View mRigthtView;
    private boolean isPortraitChange;
    private PersonalInformationDataPresenter mPresenter;
    private TextView mGenderTv, mBirthdayTv, mReginTv;
    private String mUploadFileName, mStartName, mStartGender, mStartBirthday, mStartRegion, mLastChooseDate;
    private String mCityCode, mProvinceCode;
    private DistrictPresenter mDistrictPresenter;

    public static void startSelf(@NonNull Context context) {
        Intent intent = new Intent(context, PersonalInformationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        initView();
        initData();
    }

    private void initView() {
        mNicknameEt = findViewById(R.id.nicknameEt);
        mGenderTv = findViewById(R.id.genderTv);
        mBirthdayTv = findViewById(R.id.birthdayTv);
        mReginTv = findViewById(R.id.reginTv);
        TextView cellphoneTv = findViewById(R.id.cellphoneTv);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.my_information));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        mRigthtView = QingXinTitleBar.setRightText(titleBar, getResources().getString(R.string.save), this);
        mUserHeadSdv = findViewById(R.id.userHeadSdv);
        FrameLayout modifyHeadFl = findViewById(R.id.modifyHeadFl);
        FrameLayout genderFl = findViewById(R.id.genderFl);
        FrameLayout birthdayFl = findViewById(R.id.birthdayFl);
        FrameLayout regionFl = findViewById(R.id.regionFl);
        genderFl.setOnClickListener(this);
        modifyHeadFl.setOnClickListener(this);
        birthdayFl.setOnClickListener(this);
        regionFl.setOnClickListener(this);
        mPresenter = new PersonalInformationDataPresenter(this);

        com.qingxin.medical.base.MemBean memBean = QingXinApplication.getInstance().getLoginSession();
        cellphoneTv.setText(memBean.getMem().getMobile());
        String gender = memBean.getMem().getGender();
        if (!TextUtils.isEmpty(gender)) {
            if ("M".equals(gender)) {
                mGenderTv.setText("男");
            } else if ("F".equals(gender)) {
                mGenderTv.setText("女");
            }
        }
        setAge(memBean.getMem().getBirthday());
        mCityCode = memBean.getMem().getCity_id();
        mProvinceCode = memBean.getMem().getProvince_id();
        setCityName(mCityCode, mProvinceCode);
        if (!TextUtils.isEmpty(memBean.getMem().getCover())) {
            mUserHeadSdv.setImageURI(Uri.parse(memBean.getMem().getCover()));
        }
        if (!TextUtils.isEmpty(memBean.getMem().getName())) {
            mNicknameEt.setText(memBean.getMem().getName());
            mNicknameEt.setSelection(memBean.getMem().getName().length());
        }
    }

    private void setCityName(String cityId, String proviceId) {
        ConfigModel configModel = getModel(ConfigModel.class);
        if (configModel.getProviceData() == null || configModel.getProviceData().size() == 0) {
            getDistrictData(FLAG_SET_CITYNAME, cityId, proviceId);
            return;
        }
        if (!TextUtils.isEmpty(cityId)) {
            DistrictItemData districtItemData = configModel.getCitiesDataMap().get(cityId);
            if (null == districtItemData) return;
            mReginTv.setText(String.format("%s%s", districtItemData.getProvinceData().getName(), districtItemData.getName()));
        } else {
            if (!TextUtils.isEmpty(proviceId)) {
                ProvinceData provinceData = configModel.getProvinceDataMap().get(proviceId);
                mReginTv.setText(null == provinceData ? "" : provinceData.getName());
            }
        }
    }

    private void setAge(String birthday) {
        if (!TextUtils.isEmpty(birthday) && birthday.length() >= 10) {
            mBirthdayTv.setText(birthday.substring(0, 10));
        }
    }

    private String getGender(String gender) {
        if (TextUtils.isEmpty(gender)) return "";
        if ("男".equals(gender)) {
            return "M";
        } else if ("女".equals(gender)) {
            return "F";
        }
        return "";
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
        return isPortraitChange || MapperUtils.isChanged(mStartName, endName) || MapperUtils.isChanged(mStartGender, endGender) || MapperUtils.isChanged(mStartBirthday, endBirthday) || MapperUtils.isChanged(mStartRegion, endRegion);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onClick(View view) {
        if (mRigthtView == view) {
            if (isChanged()) {
                mPresenter.modifyPersonalInfo(mNicknameEt.getText().toString().trim(), mUploadFileName, getGender(mGenderTv.getText().toString().trim()), mBirthdayTv.getText().toString().trim(), mProvinceCode, mCityCode);
            } else {
                this.finish();
            }
        } else {
            switch (view.getId()) {
                case R.id.modifyHeadFl://修改头像
                    getPhotoPopupWindow().show(mRigthtView);
                    break;
                case R.id.genderFl://性别
                    CommonDialogFactory.getInstance().createGenderSelectDialog(this, "请选择性别", new VLResHandler() {
                        @Override
                        protected void handler(boolean succeed) {
                            if (!succeed) return;
                            CommonDialogAdapter.CommonDialogData data = (CommonDialogAdapter.CommonDialogData) param();
                            mGenderTv.setText(data.getName());
                        }
                    }).show();
                    break;
                case R.id.birthdayFl://生日
                    Date chooseDate = null;
                    if (!TextUtils.isEmpty(mLastChooseDate)) {
                        try {
                            chooseDate = new SimpleDateFormat(VLUtils.formatDate2).parse(mLastChooseDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        chooseDate = new Date();
                    }
                    CommonDialogFactory.createDatePopuWindow(chooseDate, "请选择您的生日", this, new VLAsyncHandler<QingXinDatePopuWindow>(null, VLScheduler.THREAD_MAIN) {
                        @Override
                        protected void handler(boolean succeed) {
                            if (!succeed) return;
                            QingXinDatePopuWindow qingXinDatePopuWindow = getParam();
                            Date date = qingXinDatePopuWindow.getSelectedDate();
                            if (date.getTime() >= System.currentTimeMillis()) {
                                ToastUtils.showToast("您的生日大于当前时间哦!");
                                return;
                            }
                            mLastChooseDate = VLUtils.dateToString(date, VLUtils.formatDate2);
                            mBirthdayTv.setText(mLastChooseDate);
                        }
                    }).show(mRigthtView);
                    break;
                case R.id.regionFl: //地区
                    if (getModel(ConfigModel.class).getProviceData() == null || getModel(ConfigModel.class).getProviceData().size() == 0) {
                        getDistrictData(FLAG_SHOW_POPUPWINDOW);
                        return;
                    }
                    showLocationPopupWindow();
                    break;
                default:
                    break;
            }
        }
    }

    private static final String FLAG_SHOW_POPUPWINDOW = "FLAG_SHOW_POPUPWINDOW";
    private static final String FLAG_SET_CITYNAME = "FLAG_SET_CITYNAME";

    private void getDistrictData(String flag) {
        this.getDistrictData(flag, "", "");
    }

    private void getDistrictData(String flag, String cityId, String provinceId) {
        if (null == mDistrictPresenter) {
            mDistrictPresenter = new DistrictPresenter(new DistrictContract.View() {
                @Override
                public void onSuccess(List<DistrictItemData> districtBeans) {
                    getModel(ConfigModel.class).resetDistrictData(districtBeans);
                    if (FLAG_SET_CITYNAME.equals(flag)) {
                        setCityName(cityId, provinceId);
                    } else {
                        showLocationPopupWindow();
                    }
                }

                @Override
                public void onError(QingXinError error) {
                    HandErrorUtils.handleError(error);
                }

                @Override
                public void setPresenter(DistrictContract.Presenter presenter) {
                }
            });
        }
        mDistrictPresenter.getDistrictData("province,city");
    }

    private void showLocationPopupWindow() {
        CommonDialogFactory.createLocationPopupWindow("", "0", new VLAsyncHandler<QingXinLocationPopupWindow>(null, VLScheduler.THREAD_MAIN) {
            @Override
            protected void handler(boolean succeed) {
                if (!succeed) return;
                QingXinLocationPopupWindow qingXinLocationPopupWindow = getParam();
                if (null == qingXinLocationPopupWindow) return;
                DistrictItemData city = qingXinLocationPopupWindow.getSelectedCity();
                mCityCode = city == null ? "" : city.getId();
                mProvinceCode = city == null ? "" : (city.getProvinceData() == null ? "" : city.getProvinceData().getId());
                setCityName(mCityCode, mProvinceCode);
            }
        }, this).show(mRigthtView);
    }

    private QingXinLocalPhotoPopupWindow getPhotoPopupWindow() {
        return CommonDialogFactory.createLoadLocalPhotoPopupWindow(this, true, new VLAsyncHandler<QingXinLocalPhotoPopupWindow.LoadPhotoResult>(null, VLScheduler.THREAD_MAIN) {
            @Override
            protected void handler(boolean succeed) {
                if (!succeed) return;
                QingXinLocalPhotoPopupWindow.LoadPhotoResult result = getParam();
                if (null == result) return;
                File file = result.getLoadPhotoFile();
                String photoPath;
                if (null != file && !TextUtils.isEmpty(file.getPath())) {
                    photoPath = file.getPath();
                } else {
                    Bitmap bitmap = result.getCutResult();
                    photoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), bitmap);
                    assert photoPath != null;
                    file = new File(photoPath);
                    if (null != bitmap) {
                        bitmap.recycle();
                    }
                }
                mUserHeadSdv.setImageURI(Uri.parse(String.format("file://%s", photoPath)));
                mPresenter.uploadPortrait(file);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
        if (null != mDistrictPresenter) {
            mDistrictPresenter.subscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        if (null != mDistrictPresenter) {
            mDistrictPresenter.unsubscribe();
        }
    }

    @Override
    public void setPresenter(PersonalInformationDataContract.Presenter presenter) {
    }

    @Override
    public void onModifyPersonalInfoSuccess(MemBean memBean) {
        // 修改个人资料成功
        ToastUtils.showToast(getResources().getString(R.string.save_success));
        this.finish();
    }

    @Override
    public void onUploadHeadSuccess(UploadResult uploadResultBean) {
        // 上传头像成功
        if (!TextUtils.isEmpty(uploadResultBean.getFilename())) {
            isPortraitChange = true;
            mUploadFileName = uploadResultBean.getFilename();
        }
    }

    @Override
    public void onError(QingXinError error) {
        HandErrorUtils.handleError(error);
    }
}
