package com.qingxin.medical.home.medicalbeauty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;

/**
 * Date 2018-02-11
 *
 * @author zhikuo1
 */

public class MedicalBeautyDetailActivity extends QingXinActivity {

    private static final String MEDICAL_BEAUTY_ID = "MEDICAL_BEAUTY_ID";
    private String mMedicalId;

    public static void startSelf(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, MedicalBeautyDetailActivity.class);
        intent.putExtra(MEDICAL_BEAUTY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_beauty_detail);
        mMedicalId = getIntent().getStringExtra(MEDICAL_BEAUTY_ID);
    }
}
