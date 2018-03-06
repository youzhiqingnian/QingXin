package com.qingxin.medical.app.homepagetask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishActivity;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.app.homepagetask.model.WithdrawalsItemBean;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.mine.login.LoginFragment;
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.qingxin.medical.widget.indicator.view.ApplyWithdrawalsDialog;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLDialog;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLResHandler;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

/**
 * 福利
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class WelFareServiceFragment extends VLFragment implements WelfareCoinLogsListContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, QingXinBroadCastReceiver.OnReceiverCallbackListener, ApplyWithdrawalsDialog.OnConfirmWithdrawalListener {


    private WelfareCoinLogsListContract.Presenter mPresenter;
    private WelfareServiceListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;

    private TextView mQingxinCoinAmountTv;
    private TextView mClickToSignTv;
    private QingXinBroadCastReceiver mReceiver;
    private VLTitleBar mTitleBar;

    private ApplyWithdrawalsDialog applyWithdrawalsDialog;


    public WelFareServiceFragment() {
    }

    public static WelFareServiceFragment newInstance() {
        return new WelFareServiceFragment();
    }

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welfareservice, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;

        initView();
        initBroadcastReceiver();
    }

    /**
     * 初始化广播接收者
     */
    private void initBroadcastReceiver() {
        mReceiver = new QingXinBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(LoginFragment.LOGIN_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        mReceiver.setReceiverListener(this);

    }


    private void initView() {
        mPresenter = new WelfareCoinLogPresenter(this);
        if (null == getView()) return;
        mTitleBar = getView().findViewById(R.id.titleBar);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        mRefreshLayout = getView().findViewById(R.id.swipeLayout);
        QingXinTitleBar.init(mTitleBar, getResources().getString(R.string.welfare_home));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new WelfareServiceListAdapter(null);
        mAdapter.setOnLoadMoreListener(() -> getServiceList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add header
        @SuppressLint("InflateParams") View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_welfare_service_header, null);
        TextView mQingxinCoinRuleTv = mHeaderView.findViewById(R.id.qingxinCoinRuleTv);
        mQingxinCoinAmountTv = mHeaderView.findViewById(R.id.qingxinCoinAmountTv);
        TextView mApplyWithDrawalsTv = mHeaderView.findViewById(R.id.applyWithDrawalsTv);
        mClickToSignTv = mHeaderView.findViewById(R.id.clickToSignTv);
        TextView mReleaseDairyTv = mHeaderView.findViewById(R.id.releaseDairyTv);
        TextView mClickToInviteTv = mHeaderView.findViewById(R.id.clickToInviteTv);
        TextView mClickToRecommendTv = mHeaderView.findViewById(R.id.clickToRecommendTv);

        applyWithdrawalsDialog = new ApplyWithdrawalsDialog(getActivity());
        applyWithdrawalsDialog.setOnConfirmWithdrawalListener(this);

        if (QingXinApplication.getInstance().getLoginUser() != null) {
            mQingxinCoinAmountTv.setText(QingXinApplication.getInstance().getLoginUser().getCoin());
            if (QingXinApplication.getInstance().getLoginSession() != null && !QingXinApplication.getInstance().getLoginSession().getMem().getHas_checkin().equals("n")) {
                // 如果已经签到
                setCheckinUnable();
            }
        }

        mQingxinCoinRuleTv.setOnClickListener(this);
        mApplyWithDrawalsTv.setOnClickListener(this);
        mClickToSignTv.setOnClickListener(this);
        mReleaseDairyTv.setOnClickListener(this);
        mClickToInviteTv.setOnClickListener(this);
        mClickToRecommendTv.setOnClickListener(this);


        mAdapter.addHeaderView(mHeaderView);
        mRefreshLayout.setOnRefreshListener(this);


    }

    private void getServiceList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getCoinLogList(QingXinConstants.ROWS, skip);
    }

    @Override
    protected void onVisible(boolean first) {
        super.onVisible(first);
    }

    @Override
    public void onRefresh() {
        getServiceList(true);
    }

    @Override
    public void setPresenter(WelfareCoinLogsListContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(ListBean<CoinLogBean> coinLog) {
        hideView(R.layout.layout_loading);
        Log.i("福利社的bean", coinLog.toString());

        if (!TextUtils.isEmpty(coinLog.getBalance())) {
            mQingxinCoinAmountTv.setText(coinLog.getBalance());
        }

        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(coinLog.getItems());
        } else {
            mAdapter.addData(coinLog.getItems());
        }
        if (coinLog.getItems().size() < QingXinConstants.ROWS) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isClear);
        } else {
            mAdapter.loadMoreComplete();
        }


    }

    @Override
    public void onSuccess(CheckInBean checkIn) {
        hideView(R.layout.layout_loading);
        Log.i("每日签到", checkIn.toString());
        setCheckinUnable();
        getServiceList(true);
    }

    @Override
    public void onSuccess(WithdrawalsItemBean withdrawalsBean) {
        if (withdrawalsBean != null && !VLUtils.stringIsEmpty(withdrawalsBean.getAmount())) {
            VLDialog.showAlertDialog(getActivity(), getActivity().getResources().getString(R.string.warm_remind), getActivity().getResources().getString(R.string.apply_withdrawals_finish_remind_message), true, new VLResHandler() {
                @Override
                protected void handler(boolean succeed) {
                }
            });
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onSuccess(MemBean memBean) {
//        hideView(R.layout.layout_loading);
//        Log.i("是否签到的bean",memBean.toString());
//        if(!memBean.getMem().getHas_checkin().equals("n")){
//            // 如果已经签到
//            setCheckinUnable();
//        }
//    }

    @Override
    public void onError(String result) {
        hideView(R.layout.layout_loading);
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.loadMoreFail();
        }
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
        unRegisterLoginBroadcast();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.qingxinCoinRuleTv:
                // 青歆币规则


                break;
            case R.id.applyWithDrawalsTv:
                // 申请提现
                if (Long.valueOf(QingXinApplication.getInstance().getLoginUser().getCoin()) >= 10000) {
                    applyWithdrawalsDialog.setBalance(mQingxinCoinAmountTv.getText().toString().trim(), VLUtils.keepTwoSecimal2(Double.valueOf((Long.valueOf(mQingxinCoinAmountTv.getText().toString().trim()) / 100)) + ""));
                    applyWithdrawalsDialog.show();
                } else {
                    VLDialog.showAlertDialog(getActivity(), getActivity().getResources().getString(R.string.warm_remind), getActivity().getResources().getString(R.string.can_not_withdrawals_remind_message), true, new VLResHandler() {
                        @Override
                        protected void handler(boolean succeed) {
                        }
                    });
                }

                break;
            case R.id.clickToSignTv:
                // 点击签到
                showViewBelowActionBar(R.layout.layout_loading, QingXinTitleBar.fixActionBarHeight(mTitleBar));
                mPresenter.checkIn();

                break;
            case R.id.clickToInviteTv:
                // 点击邀请

                break;
            case R.id.clickToRecommendTv:
                // 点击推荐
                RecommendUserActivity.startSelf(getVLActivity(), mActivityResultListener);
                break;
            case R.id.releaseDairyTv:
                DiaryPublishActivity.startSelf(getVLActivity());
                break;
            default:
                break;
        }
    }

    private void setCheckinUnable() {
        mClickToSignTv.setText(getActivity().getString(R.string.already_signed));
        mClickToSignTv.setBackgroundResource(R.drawable.gray_button);
        mClickToSignTv.setTextColor(getActivity().getResources().getColor(R.color.text_color_origin_price));
        mClickToSignTv.setEnabled(false);
    }

    private VLActivity.VLActivityResultListener mActivityResultListener = (requestCode, resultCode, intent) -> {
        if (requestCode == RecommendUserActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {//推荐用户成功
            mRefreshLayout.setRefreshing(true);
            showViewBelowActionBar(R.layout.layout_loading, QingXinTitleBar.fixActionBarHeight(mTitleBar));
            VLScheduler.instance.schedule(200, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    getServiceList(true);
                }
            });
        }
    };

    //取消注册
    private void unRegisterLoginBroadcast() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if (QingXinApplication.getInstance().getLoginUser() != null) {
            mQingxinCoinAmountTv.setText(QingXinApplication.getInstance().getLoginUser().getCoin());
        }
        boolean isRefresh = intent.getBooleanExtra("refresh", false);
        if (isRefresh) {
            getServiceList(true);
        }
    }

    @Override
    public void confirmWithdrawal(String amount) {
        // 确定提现
        applyWithdrawalsDialog.dismiss();
        mPresenter.applyWithdrawals(amount);
    }
}
