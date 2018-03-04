package com.qingxin.medical.home.medicalbeauty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.home.ListBean;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;

import java.util.List;

/**
 * Date 2018/2/9
 *
 * @author zhikuo
 */

public class MedicalBeautyActivity extends QingXinActivity implements MedicalBeautyContract.View {

    private MedicalBeautyListAdapter mListAdapter;
    private MedicalBeautyPresenter mMedicalBeautyPresenter;
    private MedicalBeautyListBean mCurrentSelItem;
    private MedicalBeautyDetailAdapter mMedicalBeautyDetailAdapter;
    public static final String MEDICAL_BEAUTY_LIST_BEAN = "MEDICAL_BEAUTY_LIST_BEAN";
    public static final int REQUEST_CODE = 1001;

    public static void startSelf(@NonNull VLActivity activity) {
        startSelf(activity, null, null);
    }

    public static void startSelf(@NonNull VLActivity activity, @Nullable MedicalBeautyListBean medicalBeautyListBean, @Nullable VLActivityResultListener activityResultListener) {
        Intent intent = new Intent(activity, MedicalBeautyActivity.class);
        intent.putExtra(MEDICAL_BEAUTY_LIST_BEAN, medicalBeautyListBean);
        activity.setActivityResultListener(activityResultListener);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_beauty);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.medical_beauty));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        RecyclerView listRecycyclerView = findViewById(R.id.recyclerView);
        RecyclerView itemRecyclerView = findViewById(R.id.itemRecyclerView);
        listRecycyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mListAdapter = new MedicalBeautyListAdapter();
        listRecycyclerView.setAdapter(mListAdapter);
        mMedicalBeautyPresenter = new MedicalBeautyPresenter(this);
        mMedicalBeautyPresenter.getMedicalBeautyList("");
        mListAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == mCurrentSelItem.getPosition()) return;
            mCurrentSelItem.setSelect(false);
            adapter.notifyItemChanged(mCurrentSelItem.getPosition());
            mCurrentSelItem = (MedicalBeautyListBean) adapter.getData().get(position);
            mCurrentSelItem.setSelect(true);
            adapter.notifyItemChanged(mCurrentSelItem.getPosition());
            mMedicalBeautyPresenter.getMedicalBeautySecondList(mCurrentSelItem.getId());
        });
        MedicalBeautyListBean medicalChildBeautyBean = (MedicalBeautyListBean) getIntent().getSerializableExtra(MEDICAL_BEAUTY_LIST_BEAN);
        mMedicalBeautyDetailAdapter = new MedicalBeautyDetailAdapter(medicalBeautyListBean -> {
            if (null != medicalChildBeautyBean) {
                Intent intent = new Intent();
                intent.putExtra(MEDICAL_BEAUTY_LIST_BEAN, medicalBeautyListBean);
                setResult(Activity.RESULT_OK, intent);
                MedicalBeautyActivity.this.finish();
            } else {
                MedicalBeautyDetailActivity.startSelf(MedicalBeautyActivity.this, medicalBeautyListBean.getId());
            }
        });

        itemRecyclerView.setAdapter(mMedicalBeautyDetailAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMedicalBeautyPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMedicalBeautyPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(MedicalBeautyContract.Presenter presenter) {
    }

    @Override
    public void onSucess(ListBean<MedicalBeautyListBean> medicalBeautyListBeen) {
        List<MedicalBeautyListBean> medicalBeautyListBeans = medicalBeautyListBeen.getItems();
        int index = 0;
        for (MedicalBeautyListBean medicalBeautyListBean : medicalBeautyListBeans) {
            if (index == 0) {
                medicalBeautyListBean.setSelect(true);
                mCurrentSelItem = medicalBeautyListBean;
                mMedicalBeautyPresenter.getMedicalBeautySecondList(mCurrentSelItem.getId());
            }
            medicalBeautyListBean.setPosition(index);
            index++;
        }
        mListAdapter.addData(medicalBeautyListBeans);
    }

    @Override
    public void onGetSecondarySuccess(ListBean<MedicalBeautyDetailBean> medicalBeautyDetailBeen) {
        mMedicalBeautyDetailAdapter.setNewData(medicalBeautyDetailBeen.getItems());
    }

    @Override
    public void onError(String result) {
    }
}
