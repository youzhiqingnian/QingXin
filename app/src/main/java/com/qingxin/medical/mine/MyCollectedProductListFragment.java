package com.qingxin.medical.mine;

import android.app.Activity;
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

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.homepagetask.model.ProductBean;
import com.qingxin.medical.app.vip.ProductListBean;
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.app.vip.ProductListAdapter;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.service.QingXinBroadCastReceiver;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;

import java.util.List;

/**
 * Date 2018-03-02
 *
 * @author zhikuo1
 */

public class MyCollectedProductListFragment extends VLFragment implements MyCollectedProductListContract.View, SwipeRefreshLayout.OnRefreshListener, ProductListAdapter.ProductCallbackListener, QingXinBroadCastReceiver.OnReceiverCallbackListener {

    private View mRootView;

    private SwipeRefreshLayout mRefreshLayout;

    private ProductListAdapter mAdapter;

    private List<ProductBean> mProductList;

    private int mCurrentCancelPosition = 0;

    public static final String REFRESH_ACTION = "com.archie.action.REFRESH_ACTION";

    private QingXinBroadCastReceiver mReceiver;

    private boolean isClear;


    private MyCollectedProductListContract.Presenter mPresenter;

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


        if (null == getView()) return;
        mRootView = getView();
        initView();
        initBroadcastReceiver();
    }


    private void initView() {
        mPresenter = new MyCollectedProductListPresenter(this);

        mRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProductListAdapter(null, 2);
        mAdapter.setOnLoadMoreListener(() -> getMyCollectList(false), recyclerView);
        mAdapter.setBtnCallBackListener(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> VipDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getName() ,mResultListener));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        mAdapter.setEmptyView(R.layout.layout_my_collect_empty_view);
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
                int bookNum = intent.getIntExtra(VipDetailActivity.BOOK_NUM, 0);
                List<ProductBean> vipItemBeans = mAdapter.getData();


                int index = 0;
                for (ProductBean vipItemBean : vipItemBeans) {
                    if (vipItemBean.getId().equals(vipId)) {
                        vipItemBean.setOrder(bookNum);
                        vipItemBeans.remove(index);
//                        mAdapter.notifyItemChanged(index + mAdapter.getHeaderLayoutCount());
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
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(false);
        }
        if (first && QingXinApplication.getInstance().getLoginUser() != null) {
            showView(R.layout.layout_loading);
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
    public void onSuccess(ProductListBean ProductListBean) {
        hideView(R.layout.layout_loading);
        mProductList = ProductListBean.getItems();
        Log.i("我收藏的产品列表", ProductListBean.toString());

        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(ProductListBean.getItems());
        } else {
            mAdapter.addData(ProductListBean.getItems());
        }
        if (ProductListBean.getItems().size() < QingXinConstants.ROWS) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isClear);
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onSuccess(CollectBean collectBean) {
        if (collectBean.getIs_collect().equals("n")) {
            showToast(getString(R.string.cancel_collect_ok));
            mProductList.remove(mCurrentCancelPosition);
            mAdapter.notifyItemRemoved(mCurrentCancelPosition);
        }
    }

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
    public void onRefresh() {
        getMyCollectList(true);
    }

    @Override
    public void onProductButtonClick(int position, String id) {

        mCurrentCancelPosition = position;

        Log.i("产品取消收藏", "我的产品列表取消收藏");
        // 取消收藏产品
        mPresenter.cancelCollect(id);
    }

    @Override
    public void receiverUpdata(Intent intent) {
        if (intent.getBooleanExtra("refresh", false)) {
            getMyCollectList(true);
        }
    }
}
