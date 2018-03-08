package com.qingxin.medical.mine;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.app.vip.VipListActivity;
import com.qingxin.medical.app.vip.ProductListAdapter;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;

/**
 * Date 2018-03-02
 *
 * @author zhikuo1
 */

public class MyBookedProductListFragment extends VLFragment implements MyBookedProductListContract.View, SwipeRefreshLayout.OnRefreshListener, ProductListAdapter.ProductCallbackListener,QingXinBroadCastReceiver.OnReceiverCallbackListener {

    private View mRootView;

    private SwipeRefreshLayout mRefreshLayout;

    private MyBookedProductListContract.Presenter mPresenter;

    private ProductListAdapter mAdapter;

    private ProductListBean mProductListBean;

    private boolean isClear;

    public static final String COUNT_ACTION = "com.archie.action.COUNT_ACTION";

    private QingXinBroadCastReceiver mReceiver;

    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";

    public MyBookedProductListFragment() {
    }

    public static MyBookedProductListFragment newInstance() {
        return new MyBookedProductListFragment();
    }


    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == getView()) return;
        mRootView = getView();
        initView();
        initBroadcastReceiver();
    }

    private void initView() {

        mPresenter = new MyBookProductListPresenter(this);
        mRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProductListAdapter(null, 1);
        mAdapter.setOnLoadMoreListener(() -> getMyBookedProduct(false), recyclerView);
        mAdapter.setBtnCallBackListener(this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> VipDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getName(),null));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);


        View emptyView = LayoutInflater.from(getVLActivity()).inflate(R.layout.layout_my_booked_product_empty_view,null);
        TextView goToScanProductTv = emptyView.findViewById(R.id.goToScanProductTv);
        mAdapter.setEmptyView(emptyView);

        goToScanProductTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VipListActivity.startSelf(getVLActivity());
            }
        });

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


    private void getMyBookedProduct(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getMyBookedProductList(QingXinConstants.ROWS, skip, "product", "book");
    }

    @Override
    protected void onVisible(boolean first) {
        super.onVisible(first);
        if(mRefreshLayout != null){
            mRefreshLayout.setRefreshing(false);
        }
        if (first && QingXinApplication.getInstance().getLoginUser() != null) {
            showView(R.layout.layout_loading);
            VLScheduler.instance.schedule(200, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    getMyBookedProduct(true);
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
    public void setPresenter(MyBookedProductListContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(ProductListBean productListBean) {
        hideView(R.layout.layout_loading);
        mProductListBean = productListBean;
        Log.i("我预定的产品列表", productListBean.toString());
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(productListBean.getItems());
        } else {
            mAdapter.addData(productListBean.getItems());
        }
        if (productListBean.getItems().size() < QingXinConstants.ROWS) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isClear);
        } else {
            mAdapter.loadMoreComplete();
        }
        if(productListBean != null && productListBean.getCount() != 0){
            sendBroadCast(productListBean.getCount()+"");
        }
    }

    @Override
    public void onError(QingXinError error) {
        hideView(R.layout.layout_loading);
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.loadMoreFail();
        }
        HandErrorUtils.handleError(error);
    }

    @Override
    public void onRefresh() {
        getMyBookedProduct(true);
    }

    @Override
    public void onProductButtonClick(int position,String id) {
        // 联系我们
        VLUtils.gotoDail(getActivity(), mProductListBean.getItems().get(position).getMobile());
    }

    private void sendBroadCast(String bookCount) {
        Intent intent = new Intent(COUNT_ACTION);
        intent.putExtra("bookCount",bookCount);
        LocalBroadcastManager.getInstance(getVLActivity()).sendBroadcast(
                intent
        );
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if(intent.getBooleanExtra("refresh",false)){
            getMyBookedProduct(true);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }
}
