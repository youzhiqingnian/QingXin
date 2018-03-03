package com.qingxin.medical.mine;

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
import com.qingxin.medical.home.districtsel.AgencyAdapter;
import com.qingxin.medical.home.districtsel.StrictSelBean;
import com.qingxin.medical.home.districtsel.StrictSelDetailActivity;
import com.qingxin.medical.home.districtsel.StrictSelPresenter;
import com.vlee78.android.vl.VLFragment;

/**
 * Date 2018-03-02
 *
 * @author zhikuo1
 */

public class MyDiaryListFragment extends VLFragment {

    private AgencyAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;
    private StrictSelPresenter mPresenter;
    private String mType;
    private static final String STRICTSEL_TYEP = "STRICTSEL_TYEP";
    public static final String STRICTSEL_TYEP_HOSPITALS = "hospital";
    public static final String STRICTSEL_TYEP_DOCTORS = "doctor";

    public MyDiaryListFragment() {
    }

    public static MyDiaryListFragment newInstance() {
        return new MyDiaryListFragment();
    }

    @Override
    protected View onCreateContent(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_mine_tab, container, false);
        return inflater.inflate(R.layout.fake_2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       if (null == getView()) return;
        /* mRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
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
   */ }

    private void getDiaryList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getStrictSelList(mType, QingXinConstants.ROWS, skip);
    }


    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.subscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mPresenter.unsubscribe();
    }

}
