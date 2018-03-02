package com.qingxin.medical.home.medicalbeauty;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.home.ItemBean;
import com.qingxin.medical.home.ListBean;
import com.vlee78.android.vl.VLTitleBar;

import java.util.List;

/**
 * Date 2018-02-11
 *
 * @author zhikuo1
 */

public class MedicalBeautyDetailActivity extends QingXinActivity implements MedicalBeautyDetailContract.View {

    private MedicalBeautyDetailContract.Presenter mPresenter;

    private static final String MEDICAL_BEAUTY_ID = "MEDICAL_BEAUTY_ID";
    private String mMedicalId;

    private VLTitleBar mTitleBar;

    public static void startSelf(@NonNull Context context, @NonNull String id) {
        Intent intent = new Intent(context, MedicalBeautyDetailActivity.class);
        intent.putExtra(MEDICAL_BEAUTY_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_beauty_detail);

        mTitleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.setLeftReturn(mTitleBar, this);


        mMedicalId = getIntent().getStringExtra(MEDICAL_BEAUTY_ID);

        mPresenter = new MedicalBeautyDetailPresenter(this);

        if (!TextUtils.isEmpty(mMedicalId)) {
            mPresenter.getMedicalBeautyDetail(mMedicalId);
        }

    }


    @Override
    public void onSucess(ItemBean<MedicalBeautyRealDetailBean> medicalBeautyDetailBeen) {

        Log.i("医美百科详情", medicalBeautyDetailBeen.toString());

        setData(medicalBeautyDetailBeen);

    }

    @Override
    public void onError(String result) {

    }

    @Override
    public void setPresenter(MedicalBeautyDetailContract.Presenter presenter) {

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


    public void setData(ItemBean<MedicalBeautyRealDetailBean> data) {
        QingXinTitleBar.init(mTitleBar, data.getItem().getName());
        SimpleDraweeView mCoverSdv = findViewById(R.id.coverSdv);
//        SimpleDraweeView mProgramCoverSdv = findViewById(R.id.programCoverSdv);
        TextView mNameTv = findViewById(R.id.nameTv);
        TextView mProductIntroTv = findViewById(R.id.productIntroTv);
        TextView mCategoryTv = findViewById(R.id.categoryTv);
        TextView mPainLevel = findViewById(R.id.painLevel);
        TextView mTakeEffectDurationTv = findViewById(R.id.takeEffectDurationTv);
        TextView mRecoveryDurationTv = findViewById(R.id.recoveryDurationTv);
        TextView mCharacteristicsTv = findViewById(R.id.characteristicsTv);
        TextView mAttentionsTv = findViewById(R.id.attentionsTv);
        TextView mProgramIntroTv = findViewById(R.id.programIntroTv);
        TextView mTipsBeforeOperationTv = findViewById(R.id.tipsBeforeOperationTv);
        TextView mTipsAfterOperationTv = findViewById(R.id.tipsAfterOperationTv);
//        LinearLayout mExpandOrShrinkLl = findViewById(R.id.expandOrShrinkLl);

        MedicalBeautyRealDetailBean.DetailBean detailBean = data.getItem().getDetail();
        mCoverSdv.setImageURI(Uri.parse(detailBean.getCover()));
        mNameTv.setText(data.getItem().getName());
//        mProductIntroTv.setText(Html.fromHtml(detailBean.getIcontxt()));


        /*if (!TextUtils.isEmpty(detailBean.getIcontxt()) && detailBean.getIcontxt().contains(",")) {
            String[] iconText = detailBean.getIcontxt().split(",");
            if (iconText.length >= 4) {
                mCategoryTv.setText(iconText[0]);
                mPainLevel.setText(iconText[1]);
                mTakeEffectDurationTv.setText(iconText[2]);
                mRecoveryDurationTv.setText(iconText[3]);
            }
       }*/
        List<String> iconText = detailBean.getIcontxt();
        if (iconText.size() >= 4) {
            mCategoryTv.setText(iconText.get(0));
            mPainLevel.setText(iconText.get(1));
            mTakeEffectDurationTv.setText(iconText.get(2));
            mRecoveryDurationTv.setText(iconText.get(3));
        }

        mCharacteristicsTv.setText(Html.fromHtml(detailBean.getFeature()));
        mAttentionsTv.setText(Html.fromHtml(detailBean.getCare()));
        mProgramIntroTv.setText(Html.fromHtml(detailBean.getIntroduce()));
        mTipsBeforeOperationTv.setText(Html.fromHtml(detailBean.getOper_before_tip()));
        mTipsAfterOperationTv.setText(Html.fromHtml(detailBean.getOper_after_tip()));

    }
}
