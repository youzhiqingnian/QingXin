package com.qingxin.medical.app.vip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.qingxin.medical.app.homepagetask.model.VipProductBean;
import com.qingxin.medical.base.QingXinFragment;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;
import java.util.List;


/**
 * Date 2018/3/6
 *
 * @author zhikuo
 */

public class VipListFragment extends QingXinFragment implements VipListContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static VipListFragment newInstance(boolean isShowHeader) {
        return newInstance(isShowHeader, null);
    }

    public static VipListFragment newInstance(boolean isShowHeader, String search) {
        VipListFragment vipListFragment = new VipListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_HEADER, isShowHeader);
        bundle.putString(VIP_SEARCH, search);
        vipListFragment.setArguments(bundle);
        return vipListFragment;
    }

    private VipListContract.Presenter mPresenter;
    private VipListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;
    private String mSearch;//查询条件
    public static final String SHOW_HEADER = "SHOW_HEADER";
    public static final String VIP_SEARCH = "VIP_SEARCH";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_goddess_diary_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == getView()) {
            return;
        }
        VLTitleBar titleBar = getView().findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.share_only_by_new_person));
        QingXinTitleBar.setLeftReturn(titleBar, getActivity());
        mPresenter = new VipListPresenter(this);

        mRefreshLayout = getView().findViewById(R.id.swipeLayout);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new VipListAdapter(null);
        mAdapter.setOnLoadMoreListener(() -> getVipList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add header
        boolean showHeader = getArguments().getBoolean(SHOW_HEADER);
        mSearch = getArguments().getString(VIP_SEARCH);
        mAdapter.setOnItemClickListener((adapter, view, position) -> VipDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), mAdapter.getData().get(position).getName(), mResultListener));
        mRefreshLayout.setOnRefreshListener(this);
        if (showHeader) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(R.mipmap.vip_top_banner);
            mAdapter.addHeaderView(imageView);
            mRefreshLayout.setRefreshing(true);
            getVipList(true);
        }else {
            titleBar.setVisibility(View.GONE);
            mAdapter.setEmptyView(R.layout.group_empty);
            mRefreshLayout.setEnabled(false);
        }
    }

    public void setSearch(@NonNull String search){
        this.mSearch = search;
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setRefreshing(true);
        getVipList(true);
    }

    private void getVipList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getVipList(QingXinConstants.ROWS, skip, mSearch);
    }

    @Override
    public void setPresenter(VipListContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(VipListBean vip) {
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(vip.getItems());
        } else {
            mAdapter.addData(vip.getItems());
        }
        if (vip.getItems().size() < QingXinConstants.ROWS) {
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

    @Override
    public void onRefresh() {
        getVipList(true);
    }

    private VLActivity.VLActivityResultListener mResultListener = new VLActivity.VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == VipDetailActivity.VIP_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                String vipId = intent.getStringExtra(VipDetailActivity.VIP_ID);
                int bookNum = intent.getIntExtra(VipDetailActivity.BOOK_NUM, 0);
                List<VipProductBean> vipItemBeans = mAdapter.getData();
                int index = 0;
                for (VipProductBean vipItemBean : vipItemBeans) {
                    if (vipItemBean.getId().equals(vipId)) {
                        vipItemBean.setOrder(bookNum);
                        mAdapter.notifyItemChanged(index + mAdapter.getHeaderLayoutCount());
                        break;
                    }
                    index++;
                }
            }
        }
    };
}
