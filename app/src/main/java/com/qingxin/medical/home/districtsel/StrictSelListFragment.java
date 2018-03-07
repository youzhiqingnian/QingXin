package com.qingxin.medical.home.districtsel;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.utils.HandErrorUtils;

/**
 * 本地严选--机构列表
 * Date 2018/2/6
 *
 * @author zhikuo
 */
public class StrictSelListFragment extends QingXinFragment implements SwipeRefreshLayout.OnRefreshListener, StrictSelContract.View {

    public StrictSelListFragment() {
    }

    public static StrictSelListFragment newInstance(String type) {
        StrictSelListFragment strictSelListFragment = new StrictSelListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(STRICTSEL_TYEP, type);
        strictSelListFragment.setArguments(bundle);
        return strictSelListFragment;
    }

    private AgencyAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;
    private StrictSelPresenter mPresenter;
    private String mType;
    private static final String STRICTSEL_TYEP = "STRICTSEL_TYEP";
    public static final String STRICTSEL_TYEP_HOSPITALS = "hospital";
    public static final String STRICTSEL_TYEP_DOCTORS = "doctor";

    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.group_recyclerview, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == getView()) return;
        mRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AgencyAdapter(null);
        mAdapter.setOnLoadMoreListener(() -> getDiaryList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        mType = getArguments().getString(STRICTSEL_TYEP);
        mPresenter = new StrictSelPresenter(this);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        getDiaryList(true);
        mAdapter.setOnItemClickListener((adapter, view, position) -> StrictSelDetailActivity.startSelf(getActivity(), (StrictSelBean) adapter.getData().get(position)));
    }

    private void getDiaryList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getStrictSelList(mType, QingXinConstants.ROWS, skip);
    }

    @Override
    public void onRefresh() {
        getDiaryList(true);
    }

    @Override
    public void setPresenter(StrictSelContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(ListBean<StrictSelBean> strictSelBeen) {
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(strictSelBeen.getItems());
        } else {
            mAdapter.addData(strictSelBeen.getItems());
        }
        if (strictSelBeen.getItems().size() < QingXinConstants.ROWS) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isClear);
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(QingXinError error) {
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.loadMoreFail();
        }
        HandErrorUtils.handleError(error);
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
}
