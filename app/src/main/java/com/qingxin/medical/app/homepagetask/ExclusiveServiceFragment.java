package com.qingxin.medical.app.homepagetask;

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
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.ServiceBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;
/**
 * 歆人专享
 * Date 2018-02-05
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
        return new ExclusiveServiceFragment();
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
        mPresenter = new ExclusiveServicePresenter(this);
        View mView = getView();
        VLTitleBar titleBar = null;
        RecyclerView recyclerView = null;
        if (mView != null) {
            titleBar = mView.findViewById(R.id.titleBar);
            mRefreshLayout = mView.findViewById(R.id.swipeLayout);
            recyclerView = mView.findViewById(R.id.recyclerView);
        }
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.exclusive_service));
        QingXinTitleBar.setLeftReturn(titleBar, getActivity());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter = new ExclusiveServiceListAdapter(null,getActivity());
            mAdapter.setOnLoadMoreListener(() -> getServiceList(false), recyclerView);
            recyclerView.setAdapter(mAdapter);
            //add padding
            SpaceItemDecoration dividerDecoration = new SpaceItemDecoration(VLUtils.dip2px(18));
            recyclerView.addItemDecoration(dividerDecoration);
        }

        //add header
        ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.mipmap.goddess_diary_top_cover);
        mAdapter.addHeaderView(imageView);

//        mAdapter.setOnItemClickListener((adapter, view, position) -> GoddessDiaryDetailActivity.startSelf(getActivity(), mAdapter.getData().get(position).getId(), mResultListener));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        mAdapter.setEmptyView(R.layout.group_empty);
        getServiceList(true);

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

    }

    @Override
    public void setPresenter(ServiceListContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(ListBean<ServiceBean> service) {

        Log.i("专属服务列表",service.toString());

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
