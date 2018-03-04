package com.qingxin.medical.mine;

import android.os.Bundle;
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
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.app.vip.VipListAdapter;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;
/**
 * Date 2018-03-02
 *
 * @author zhikuo1
 */

public class MyCollectedProductListFragment extends VLFragment implements MyCollectedProductListContract.View, SwipeRefreshLayout.OnRefreshListener, VipListAdapter.ProductCallbackListener{

    private View mRootView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private VipListAdapter mAdapter;




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
    }


    private void initView() {
        mPresenter = new MyCollectedProductListPresenter(this);

        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VipListAdapter(null, 2);
        mAdapter.setOnLoadMoreListener(() -> getMyCollectList(false), recyclerView);
        mAdapter.setBtnCallBackListener(this);
        recyclerView.setAdapter(mAdapter);
        //add padding
        SpaceItemDecoration dividerDecoration = new SpaceItemDecoration(VLUtils.dip2px(18));
        recyclerView.addItemDecoration(dividerDecoration);
        //add header
        mAdapter.setOnItemClickListener((adapter, view, position) -> VipDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), null));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
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

    @Override
    protected void onVisible(boolean first) {
        super.onVisible(first);
        if (first) {
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
    public void onSuccess(VipListBean vipListBean) {
        hideView(R.layout.layout_loading);
        Log.i("我收藏的产品列表", vipListBean.toString());

        if (isClear) {
            mSwipeRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(vipListBean.getItems());
        } else {
            mAdapter.addData(vipListBean.getItems());
        }
        if (vipListBean.getItems().size() < QingXinConstants.ROWS) {
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
            getMyCollectList(true);
        }
    }

    @Override
    public void onError(String result) {
        hideView(R.layout.layout_loading);
        if (isClear) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.loadMoreFail();
        }
    }

    @Override
    public void onRefresh() {
        getMyCollectList(true);
    }

    @Override
    public void onProductButtonClick(String id) {
        // 取消收藏
        Log.i("产品取消收藏", "我的产品列表取消收藏");
        // 取消收藏产品
        mPresenter.cancelCollect(id);
    }

}
