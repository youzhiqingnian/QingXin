package com.qingxin.medical.app.homepagetask;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.DiaryPublishActivity;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.home.ListBean;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLTitleBar;

/**
 * 福利
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class WelFareServiceFragment extends VLFragment implements WelfareCoinLogsListContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    private WelfareCoinLogsListContract.Presenter mPresenter;
    private WelfareServiceListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;

    private TextView mQingxinCoinAmountTv;
    private TextView mClickToSignTv;

    public WelFareServiceFragment() {
    }

    public static WelFareServiceFragment newInstance() {
        return new WelFareServiceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_goddess_diary_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (getView() == null) return;

        initView();
    }

    private void initView() {
        mPresenter = new WelfareCoinLogPresenter(this);
        if (null == getView()) return;
        VLTitleBar titleBar = getView().findViewById(R.id.titleBar);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        mRefreshLayout = getView().findViewById(R.id.swipeLayout);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.welfare_home));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new WelfareServiceListAdapter(null);
        mAdapter.setOnLoadMoreListener(() -> getServiceList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add header
        View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_welfare_service_header, null);
        TextView mQingxinCoinRuleTv = mHeaderView.findViewById(R.id.qingxinCoinRuleTv);
        mQingxinCoinAmountTv = mHeaderView.findViewById(R.id.qingxinCoinAmountTv);
        TextView mApplyWithDrawalsTv = mHeaderView.findViewById(R.id.applyWithDrawalsTv);
        mClickToSignTv = mHeaderView.findViewById(R.id.clickToSignTv);
        TextView mReleaseDairyTv = mHeaderView.findViewById(R.id.releaseDairyTv);
        TextView mClickToInviteTv = mHeaderView.findViewById(R.id.clickToInviteTv);
        TextView mClickToRecommendTv = mHeaderView.findViewById(R.id.clickToRecommendTv);

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
        if (first) {
            mRefreshLayout.setRefreshing(true);
            getServiceList(true);
        }
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
        Log.i("福利社的bean", coinLog.toString());

        if (coinLog != null && coinLog.getCount() > 0) {
            mQingxinCoinAmountTv.setText(String.valueOf(coinLog.getItems().get(coinLog.getCount() - 1).getBalance()));
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSuccess(CheckInBean checkIn) {
        Log.i("每日签到", checkIn.toString());
        mClickToSignTv.setText(getActivity().getString(R.string.already_signed));
        mClickToSignTv.setBackground(getActivity().getResources().getDrawable(R.drawable.gray_button));
        mClickToSignTv.setTextColor(getActivity().getResources().getColor(R.color.text_color_origin_price));
        mClickToSignTv.setEnabled(false);
        getServiceList(true);
    }

    @Override
    public void onError(String result) {
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.qingxinCoinRuleTv:
                // 青歆币规则


                break;
            case R.id.applyWithDrawalsTv:
                // 申请提现


                break;
            case R.id.clickToSignTv:
                // 点击签到
                mPresenter.checkIn();

                break;
            case R.id.clickToInviteTv:
                // 点击邀请


                break;
            case R.id.clickToRecommendTv:
                // 点击推荐


                break;
            case R.id.releaseDairyTv:
                DiaryPublishActivity.startSelf(getVLActivity(), mActivityResultListener);
                break;
            default:
                break;
        }
    }

    private VLActivity.VLActivityResultListener mActivityResultListener = (requestCode, resultCode, intent) -> {
        if (requestCode == DiaryPublishActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {//发布日记成功

        }
    };
}
