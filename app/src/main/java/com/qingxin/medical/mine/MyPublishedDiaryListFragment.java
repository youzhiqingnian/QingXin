package com.qingxin.medical.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailActivity;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListAdapter;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.home.ListBean;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLFragment;
import com.vlee78.android.vl.VLScheduler;

/**
 * Date 2018-03-02
 *
 * @author zhikuo1
 */

public class MyPublishedDiaryListFragment extends VLFragment implements MyPublishedDiaryListContract.View, SwipeRefreshLayout.OnRefreshListener, GoddessDiaryListAdapter.DeleteDiaryListener, GoddessDiaryListAdapter.EditDiaryListener {

    private View mRootView;

    private SwipeRefreshLayout mRefreshLayout;

    private GoddessDiaryListAdapter mAdapter;

    private MyPublishedDiaryListContract.Presenter mPresenter;

    public static final String COUNT_ACTION = "com.archie.action.COUNT_ACTION";

    private boolean isClear;

    public MyPublishedDiaryListFragment() {
    }

    public static MyPublishedDiaryListFragment newInstance() {
        return new MyPublishedDiaryListFragment();
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
        mPresenter = new MyPublishedDiaryListPresenter(this);

        mRefreshLayout = mRootView.findViewById(R.id.swipeRefreshLayout);
        RecyclerView recyclerView = mRootView.findViewById(R.id.recyclerView);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GoddessDiaryListAdapter(null, 1);
        mAdapter.setOnLoadMoreListener(() -> getMyCollectList(false), recyclerView);
        mAdapter.setDeleteDiaryListener(this);
        mAdapter.setEditDiaryListener(this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> GoddessDiaryDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), null));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);

        View emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_my_diary_empty_view, null);
        TextView publishDiaryTv = emptyView.findViewById(R.id.publishDiaryTv);
        publishDiaryTv.setOnClickListener(view -> DiaryPublishActivity.startSelf(getVLActivity()));
        mAdapter.setEmptyView(emptyView);


    }

    private void getMyCollectList(boolean isClear) {
        this.isClear = isClear;
        // 收藏的产品
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getMyPublishedDiaryList(QingXinApplication.getInstance().getLoginUser().getId(), QingXinConstants.ROWS, skip);
    }

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
    public void setPresenter(MyPublishedDiaryListContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(ListBean<DiaryItemBean> diary) {
        hideView(R.layout.layout_loading);
        if(diary != null && diary.getCount() > 0){
            QingXinApplication.getInstance().getLoginSession().getMem().setDiary_amount(diary.getCount());
            sendBroadCast("",1);
        }

        if (isClear) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(diary.getItems());
        } else {
            mAdapter.addData(diary.getItems());
        }
        if (diary.getItems().size() < QingXinConstants.ROWS) {
            //第一页如果不够一页就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isClear);
        } else {
            mAdapter.loadMoreComplete();
        }

        if (0 != diary.getCount()) {
            sendBroadCast(diary.getCount() + "",0);
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
    public void deleteDiary(String id) {
        // 删除日记
        // TODO
    }

    @Override
    public void editDiary(int position, String id) {
        // 编辑日记
        DiaryPublishActivity.startSelf(getVLActivity(), mAdapter.getData().get(position));
    }

    private void sendBroadCast(String diaryCount,int flag) {
        Intent intent;
        if(0 == flag){
            intent = new Intent(COUNT_ACTION);
            intent.putExtra("diaryCount", diaryCount);
        }else{
            intent = new Intent(MineDataFragment.REFRESH_ACTION);
            intent.putExtra("refresh", true);
        }

        LocalBroadcastManager.getInstance(getVLActivity()).sendBroadcast(intent);
    }
}
