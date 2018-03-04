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
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailActivity;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryListAdapter;
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

public class MyPublishedDiaryListFragment extends VLFragment implements MyPublishedDiaryListContract.View, SwipeRefreshLayout.OnRefreshListener, GoddessDiaryListAdapter.DeleteDiaryListener, GoddessDiaryListAdapter.EditDiaryListener  {

    private View mRootView;

    private SwipeRefreshLayout mRefreshLayout;

    private GoddessDiaryListAdapter mAdapter;

    private MyPublishedDiaryListContract.Presenter mPresenter;


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
        mAdapter = new GoddessDiaryListAdapter(null,1);
        mAdapter.setOnLoadMoreListener(() -> getMyCollectList(false), recyclerView);
        mAdapter.setDeleteDiaryListener(this);
        mAdapter.setEditDiaryListener(this);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> GoddessDiaryDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), null));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        mAdapter.setEmptyView(R.layout.group_empty);


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
        if(mRefreshLayout != null){
            mRefreshLayout.setRefreshing(false);
        }
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
    public void setPresenter(MyPublishedDiaryListContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(ListBean<DiaryItemBean> diary) {
        hideView(R.layout.layout_loading);
        Log.i("我收藏的日记列表", diary.toString());

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
    public void editDiary(DiaryItemBean item) {
        // 编辑日记
        // TODO

    }
}
