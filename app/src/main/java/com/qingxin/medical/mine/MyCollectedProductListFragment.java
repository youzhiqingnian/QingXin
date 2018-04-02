package com.qingxin.medical.mine;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.qingxin.medical.NetErrorView;
import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.homepagetask.model.ProductBean;
import com.qingxin.medical.app.vip.ProductListAdapter;
import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;
import java.util.Iterator;
import java.util.List;

/**
 * Date 2018-03-02
 *
 * @author zhikuo1
 */

public class MyCollectedProductListFragment extends VLFragment implements MyCollectedProductListContract.View, SwipeRefreshLayout.OnRefreshListener, ProductListAdapter.ProductCallbackListener, QingXinBroadCastReceiver.OnReceiverCallbackListener {

    private SwipeRefreshLayout mRefreshLayout;
    private ProductListAdapter mAdapter;
    private List<ProductBean> mProductList;
    private int mCurrentCancelPosition = 0;
    private QingXinBroadCastReceiver mReceiver;
    private boolean isClear;
    private MyCollectedProductListContract.Presenter mPresenter;
    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";

    public MyCollectedProductListFragment() {
    }

    public static MyCollectedProductListFragment newInstance() {
        return new MyCollectedProductListFragment();
    }

    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initBroadcastReceiver();
    }


    private void initView() {
        if (null == getView()) return;
        mPresenter = new MyCollectedProductListPresenter(this);
        mRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProductListAdapter(null, 2);
        mAdapter.setOnLoadMoreListener(() -> getMyCollectList(false), recyclerView);
        mAdapter.setBtnCallBackListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> VipDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getName(), mResultListener));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
    }

    /**
     * 初始化广播接收者
     */
    private void initBroadcastReceiver() {
        mReceiver = new QingXinBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter(REFRESH_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, intentFilter);
        mReceiver.setReceiverListener(this);
    }

    private void getMyCollectList(boolean isClear) {
        this.isClear = isClear;
        // 收藏的产品
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getMyCollectProductList(QingXinConstants.ROWS, skip, "product", "collect");
    }

    private VLActivity.VLActivityResultListener mResultListener = new VLActivity.VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == VipDetailActivity.VIP_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                String vipId = intent.getStringExtra(VipDetailActivity.VIP_ID);
                List<ProductBean> vipItemBeans = mAdapter.getData();
                int index = 0;
                Iterator iterator = vipItemBeans.iterator();
                while (iterator.hasNext()) {
                    ProductBean vipItemBean = (ProductBean) iterator.next();
                    if (vipId.equals(vipItemBean.getId())) {
                        iterator.remove();
                        mAdapter.notifyItemRemoved(index);
                        break;
                    }
                    index++;
                }
            }
        }
    };

    @Override
    protected void onVisible(boolean first) {
        super.onVisible(first);
        if (first) {
            VLScheduler.instance.schedule(200, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    getMyCollectList(true);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
    }


    @Override
    public void setPresenter(MyCollectedProductListContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(ProductListBean productListBean) {
        mProductList = productListBean.getItems();
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            if (productListBean.getItems().size() == 0) {
                mAdapter.setEmptyView(R.layout.layout_my_collect_empty_view);
            } else {
                mAdapter.setNewData(productListBean.getItems());
            }
        } else {
            mAdapter.addData(productListBean.getItems());
        }
        if (productListBean.getItems().size() < QingXinConstants.ROWS) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isClear);
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onCollectSuccess(CollectBean collectBean) {
        if (collectBean.getIs_collect().equals("n")) {
            showToast(getString(R.string.cancel_collect_ok));
            mProductList.remove(mCurrentCancelPosition);
            mAdapter.notifyItemRemoved(mCurrentCancelPosition);
        }
    }

    @Override
    public void onError(QingXinError error) {
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            NetErrorView netErrorView = new NetErrorView(getActivity());
            netErrorView.setOnClickListener(view -> getMyCollectList(true));
            mAdapter.setEmptyView(netErrorView);
        } else {
            mAdapter.loadMoreFail();
        }
        HandErrorUtils.handleError(error);
    }

    @Override
    public void onCollectError(QingXinError error) {
        HandErrorUtils.handleError(error);
    }

    @Override
    public void onRefresh() {
        getMyCollectList(true);
    }

    @Override
    public void onProductButtonClick(int position, String id) {
        mCurrentCancelPosition = position;
        // 取消收藏产品
        mPresenter.cancelCollect(id);
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if (intent.getBooleanExtra("refresh", false)) {
            getMyCollectList(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }
}
