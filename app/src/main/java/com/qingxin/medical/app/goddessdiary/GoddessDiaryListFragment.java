package com.qingxin.medical.app.goddessdiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.qingxin.medical.base.QingXinFragment;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

import java.util.List;

/**
 * Date 2018/3/6
 *
 * @author zhikuo
 */

public class GoddessDiaryListFragment extends QingXinFragment implements DiaryListContract.View, SwipeRefreshLayout.OnRefreshListener {

    public GoddessDiaryListFragment() {
    }

    public static GoddessDiaryListFragment newInstance(boolean isShowHeader) {
        GoddessDiaryListFragment goddessDiaryFragment = new GoddessDiaryListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(SHOW_HEADER, isShowHeader);
        goddessDiaryFragment.setArguments(bundle);
        return goddessDiaryFragment;
    }

    private DiaryListContract.Presenter mPresenter;
    private GoddessDiaryListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;
    private String mSearch;//查询条件
    public static final String SHOW_HEADER = "SHOW_HEADER";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_goddess_diary_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null == getView()) return;
        VLTitleBar titleBar = getView().findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.goddess_diary));
        QingXinTitleBar.setLeftReturn(titleBar, getActivity());
        mPresenter = new GoddessDiaryPresenter(this);
        mRefreshLayout = getView().findViewById(R.id.swipeLayout);
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GoddessDiaryListAdapter(null);
        mAdapter.setOnLoadMoreListener(() -> getDiaryList(false), recyclerView);
        recyclerView.setAdapter(mAdapter);
        //add padding
        SpaceItemDecoration dividerDecoration = new SpaceItemDecoration(VLUtils.dip2px(18));
        recyclerView.addItemDecoration(dividerDecoration);
        //add header
        boolean showHeader = getArguments().getBoolean(SHOW_HEADER);
        mAdapter.setOnItemClickListener((adapter, view, position) -> GoddessDiaryDetailActivity.startSelf(getVLActivity(), mAdapter.getData().get(position).getId(), mResultListener));
        mRefreshLayout.setOnRefreshListener(this);
        if (showHeader) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(R.mipmap.goddess_diary_top_cover);
            mAdapter.addHeaderView(imageView);
            mRefreshLayout.setRefreshing(true);
            getDiaryList(true);
        } else {
            titleBar.setVisibility(View.GONE);
            mAdapter.setEmptyView(R.layout.group_empty);
            mRefreshLayout.setEnabled(false);
        }
    }

    public void setSearch(@NonNull String search) {
        this.mSearch = search;
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setRefreshing(true);
        getDiaryList(true);
    }

    private void getDiaryList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        mPresenter.getGoddessDiaryList(QingXinConstants.ROWS, skip, mSearch);
    }

    @Override
    public void setPresenter(DiaryListContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(ListBean<DiaryItemBean> diary) {
        Log.i("女神日记列表", diary.toString());
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void onRefresh() {
        getDiaryList(true);
    }

    private VLActivity.VLActivityResultListener mResultListener = new VLActivity.VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == GoddessDiaryDetailActivity.DIARY_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                String diaryId = intent.getStringExtra(GoddessDiaryDetailActivity.DIARY_ID);
                int collectNum = intent.getIntExtra(GoddessDiaryDetailActivity.COLLECT_NUM, 0);
                List<DiaryItemBean> diaryItemBeans = mAdapter.getData();
                int index = 0;
                for (DiaryItemBean diaryItemBean : diaryItemBeans) {
                    if (diaryItemBean.getId().equals(diaryId)) {
                        diaryItemBean.setCollect_num(collectNum);
                        mAdapter.notifyItemChanged(index + mAdapter.getHeaderLayoutCount());
                        break;
                    }
                    index++;
                }
            }
        }
    };
}
