package com.qingxin.medical.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.WelFareServiceFragment;
import com.qingxin.medical.app.vip.VipDetailActivity;
import com.qingxin.medical.app.vip.VipListAdapter;
import com.qingxin.medical.app.vip.VipListBean;
import com.qingxin.medical.base.QingXinFragment;
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

public class MyAppointmengListFragment extends VLFragment implements MyBookedProductListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private View mRootView;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MyBookedProductListContract.Presenter mPresenter;

    private VipListAdapter mAdapter;

    private boolean isClear;

    public MyAppointmengListFragment() {
    }

    public static MyAppointmengListFragment newInstance() {
        return new MyAppointmengListFragment();
    }


    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_tab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        if (null == getView()) return;
        mRootView = getView();
        initView();
      }

    private void initView() {

        mPresenter = new MyBookProductListPresenter(this);
        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new VipListAdapter(null,1);
        mAdapter.setOnLoadMoreListener(() -> getMyBookedProduct(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add padding
        SpaceItemDecoration dividerDecoration = new SpaceItemDecoration(VLUtils.dip2px(18));
        recyclerView.addItemDecoration(dividerDecoration);
        //add header
        mAdapter.setOnItemClickListener((adapter, view, position) -> VipDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), null));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);



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
        if(first){
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
    public void onSuccess(VipListBean vipListBean) {
        hideView(R.layout.layout_loading);
        Log.i("专享列表为", vipListBean.toString());

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
        getMyBookedProduct(true);
    }
}
