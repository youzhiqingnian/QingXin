package com.qingxin.medical.app.homepagetask;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

/**
 * 歆人专享
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class ExclusiveServiceFragment extends VLFragment implements ServiceListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private ServiceListContract.Presenter mPresenter;
    private ExclusiveServiceListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;

    public ExclusiveServiceFragment() {
    }

    public static ExclusiveServiceFragment newInstance() {
        return newInstance(false);
    }

    public static ExclusiveServiceFragment newInstance(boolean showLeftReturn) {
        ExclusiveServiceFragment fragment = new ExclusiveServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_LEFT_RETURN, showLeftReturn);
        fragment.setArguments(bundle);
        return fragment;
    }

    private static final String SHOW_LEFT_RETURN = "SHOW_LEFT_RETURN";

    @Override
    public View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_goddess_diary_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        initView();
    }

    private void initView() {
        mPresenter = new ExclusiveServicePresenter(this);
        if (null == getView()) return;
        VLTitleBar titleBar = getView().findViewById(R.id.titleBar);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        mRefreshLayout = getView().findViewById(R.id.swipeLayout);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.exclusive_service));
        if (getArguments().getBoolean(SHOW_LEFT_RETURN)) {
            QingXinTitleBar.setLeftReturn(titleBar, getActivity());
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ExclusiveServiceListAdapter(null, moblile -> VLUtils.gotoDail(getActivity(), moblile));
        mAdapter.setOnLoadMoreListener(() -> getServiceList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add padding
        SpaceItemDecoration dividerDecoration = new SpaceItemDecoration(VLUtils.dip2px(18));
        recyclerView.addItemDecoration(dividerDecoration);

        //add header
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.mipmap.exclusive_service_top_cover);
        mAdapter.addHeaderView(imageView);
        mRefreshLayout.setOnRefreshListener(this);
        showViewBelowActionBar(R.layout.layout_loading, QingXinTitleBar.fixActionBarHeight(titleBar));
        mRefreshLayout.setRefreshing(true);

        VLScheduler.instance.schedule(200, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                getServiceList(true);
            }
        });
    }

    private void getServiceList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getExclusiveService(QingXinConstants.ROWS, skip);
    }

    @Override
    public void onRefresh() {
        getServiceList(true);
    }

    @Override
    public void setPresenter(ServiceListContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(ListBean<ServiceBean> service) {
        hideView(R.layout.layout_loading);
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(service.getItems());
        } else {
            mAdapter.addData(service.getItems());
        }
        if (service.getItems().size() < QingXinConstants.ROWS) {
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
}
