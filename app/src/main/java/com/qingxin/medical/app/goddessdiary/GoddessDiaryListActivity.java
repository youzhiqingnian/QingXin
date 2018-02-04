package com.qingxin.medical.app.goddessdiary;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.GoddessDiaryBean;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

/**
 * 女神日记列表
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class GoddessDiaryListActivity extends QingXinActivity implements DiaryListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private DiaryListContract.Presenter mPresenter;
    private GoddessDiaryListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddess_diary_list);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.goddess_diary));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        mPresenter = new GoddessDiaryPresenter(this);

        mRefreshLayout = findViewById(R.id.swipeLayout);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GoddessDiaryListAdapter(null);
        mAdapter.setOnLoadMoreListener(() -> getDiaryList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add padding
        SpaceItemDecoration dividerDecoration = new SpaceItemDecoration(VLUtils.dip2px(18));
        recyclerView.addItemDecoration(dividerDecoration);
        //addheader
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.mipmap.goddess_diary_top_cover);
        mAdapter.addHeaderView(imageView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> GoddessDiaryDetailActivity.startSelf(GoddessDiaryListActivity.this,mAdapter.getData().get(position).getId()));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        getDiaryList(true);
    }

    private void getDiaryList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getGoddessDiaryList(QingXinConstants.ROWS, skip);
    }

    @Override
    public void setPresenter(DiaryListContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(GoddessDiaryBean diary) {
        if (null == diary) return;
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
        if (isClear) {
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.loadMoreFail();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void onRefresh() {
        getDiaryList(true);
    }
}
