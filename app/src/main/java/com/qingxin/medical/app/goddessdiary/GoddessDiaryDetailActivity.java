package com.qingxin.medical.app.goddessdiary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.login.LoginActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.widget.indicator.view.ShareDialog;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
/**
 * Date 2018-02-02
 *
 * @author zhikuo1
 */
public class GoddessDiaryDetailActivity extends QingXinActivity implements DiaryDetailContract.View, ShareDialog.OnShareDialogListener, View.OnClickListener {

    public static final int DIARY_DETAIL_REQUEST_CODE = 5; // 跳到日记详情里的请求码

    public static void startSelf(VLActivity activity, String diaryId, VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, GoddessDiaryDetailActivity.class);
        intent.putExtra(DIARY_ID, diaryId);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, DIARY_DETAIL_REQUEST_CODE);
    }

    public static final String DIARY_ID = "DIARY_ID";
    public static final String COLLECT_NUM = "COLLECT_NUM";

    private ScrollView mScrollSv;

    private SimpleDraweeView mAuthoerHeadSdv,
            mBeforeCoverSdv,
            mAfterCoverSdv,
            mProductCoverSdv;

    private TextView mAuthorNameTv,
            mCollectionTv,
            mDiaryProductIntroTv,
            mReserveCountTv,
            mProductPriceTv,
            mDiaryDetailTv,
            mDiaryPublishDateTv,
            mScanCountTv,
            mCollectionCountTv;

    private ShareDialog mShareDialog;

    private ImageView mScrollTopIv;

    private DiaryDetailContract.Presenter mPresenter;

    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddess_diary_detail);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.goddess_diary));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        QingXinTitleBar.setRightIcon(titleBar, R.mipmap.ic_top_right_share, view -> mShareDialog.show());

        mPresenter = new GoddessDiaryDetailPresenter(this);

        dealIntent();

        initView();

        initListener();

        if (!TextUtils.isEmpty(id)) {
            mPresenter.getGoddessDiaryDetail(id);
        }
    }

    private void initListener() {
        mCollectionTv.setOnClickListener(this);
        mScrollTopIv.setOnClickListener(this);
    }

    private void dealIntent() {
        if (getIntent() != null) {
            id = getIntent().getStringExtra(DIARY_ID);
        }
    }

    private void initView() {
        mScrollSv = findViewById(R.id.scrollSv);
        mAuthoerHeadSdv = findViewById(R.id.authoerHeadSdv);
        mBeforeCoverSdv = findViewById(R.id.beforeCoverSdv);
        mAfterCoverSdv = findViewById(R.id.afterCoverSdv);
        mProductCoverSdv = findViewById(R.id.productCoverSdv);
        mAuthorNameTv = findViewById(R.id.authorNameTv);
        mCollectionTv = findViewById(R.id.collectionTv);
        mDiaryProductIntroTv = findViewById(R.id.diaryProductIntroTv);
        mReserveCountTv = findViewById(R.id.reserveCountTv);
        mProductPriceTv = findViewById(R.id.productPriceTv);
        mDiaryDetailTv = findViewById(R.id.diaryDetailTv);
        mDiaryPublishDateTv = findViewById(R.id.diaryPublishDateTv);
        mScanCountTv = findViewById(R.id.scanCountTv);
        mCollectionCountTv = findViewById(R.id.collectionCountTv);
        mScrollTopIv = findViewById(R.id.scrollTopIv);
        mShareDialog = new ShareDialog(this);
        mShareDialog.setOnShareDialogListener(this);
    }

    @Override
    public void setPresenter(DiaryDetailContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(GoddessDiaryDetailBean diaryDetailBean) {
        Log.i("diaryDetailBean", diaryDetailBean.toString());
        if (diaryDetailBean.getItem() != null) {
            setData(diaryDetailBean);
        }
    }

    @Override
    public void onSuccess(CollectBean mCollectBean) {
        Log.i("collectBean", mCollectBean.toString());
        // 如果收藏的结果不为空
        mCollectionCountTv.setText(String.valueOf(mCollectBean.getAmount()));
        if (mCollectBean.getIs_collect().equals("n")) {
            mCollectionTv.setText(R.string.plus_collection);
            showToast(getString(R.string.cancel_collect_ok));
        } else {
            mCollectionTv.setText(R.string.cancel_collection);
            showToast(getString(R.string.collect_ok));
        }
        Intent intent = new Intent();
        intent.putExtra(DIARY_ID, id);
        intent.putExtra(COLLECT_NUM, mCollectBean.getAmount());
        setResult(Activity.RESULT_OK, intent);
    }

    @SuppressLint("DefaultLocale")
    private void setData(GoddessDiaryDetailBean diaryDetailBean) {

        DiaryItemBean itemBean;
        itemBean = diaryDetailBean.getItem();
        String collectState;
        if (itemBean.getIs_collect().equals("y")) {
            collectState = getString(R.string.cancel_collection);
        } else {
            collectState = getString(R.string.plus_collection);
        }
        mCollectionTv.setText(collectState);

        mAuthoerHeadSdv.setImageURI(Uri.parse(itemBean.getMem().getCover()));
        mAuthorNameTv.setText(itemBean.getMem().getName());
        mBeforeCoverSdv.setImageURI(Uri.parse(itemBean.getOper_before_photo()));
        mAfterCoverSdv.setImageURI(Uri.parse(itemBean.getOper_after_photo()));
        mProductCoverSdv.setImageURI(Uri.parse(itemBean.getProduct().getCover()));
        mDiaryProductIntroTv.setText(itemBean.getProduct().getName());
        mReserveCountTv.setText(String.format("%d%s", itemBean.getProduct().getOrder(), getString(R.string.book_times)));
        mProductPriceTv.setText(String.format("%d%s%d", itemBean.getProduct().getPrice(), getString(R.string.gap), itemBean.getProduct().getOld_price()));
        mDiaryDetailTv.setText(itemBean.getSummary());
        mDiaryPublishDateTv.setText(itemBean.getCreated_at());
        mScanCountTv.setText(String.valueOf(itemBean.getVisit_num()));
        mCollectionCountTv.setText(String.valueOf(itemBean.getCollect_num()));
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


    @Override
    public void wxFriendShare() {

    }

    @Override
    public void wxCircleShare() {

    }

    @Override
    public void qqFriendsShare() {

    }

    @Override
    public void qqZoneShare() {

    }

    @Override
    public void weiboShare() {
    }

    @Override
    public void copyUrl() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.collectionTv:
                // 收藏
                if (QingXinApplication.getInstance().getLoginUser() != null) {
                    // 如果登录过了
                    mPresenter.collectDiary(id);
                } else {
                    // 如果没有登录就跳到登录页面
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.scrollTopIv:
                //设置默认滚动到顶部
                VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        mScrollSv.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
                break;
            default:
                break;
        }
    }
}
