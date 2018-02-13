package com.qingxin.medical.home.medicalbeauty;

import android.os.Bundle;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.home.ListBean;

/**
 * Date 2018-02-11
 *
 * @author zhikuo1
 */

public class MedicalBeautyDetailActivity extends QingXinActivity implements MedicalBeautyContract.View {

    private MedicalBeautyDetailContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_beauty_detail);
    }

    @Override
    public void setPresenter(MedicalBeautyContract.Presenter presenter) {

    }

    @Override
    public void onSucess(ListBean<MedicalBeautyListBean> medicalBeautyListBeen) {

    }

    @Override
    public void onError(String result) {

    }
}
