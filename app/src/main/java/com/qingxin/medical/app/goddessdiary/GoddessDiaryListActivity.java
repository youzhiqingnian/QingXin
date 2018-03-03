package com.qingxin.medical.app.goddessdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.widget.decoration.SpaceItemDecoration;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;
import java.util.List;

/**
 * 女神日记列表
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class GoddessDiaryListActivity extends QingXinActivity implements DiaryListContract.View, SwipeRefreshLayout.OnRefreshListener {

    public static void startSelf(@NonNull Context context){
        Intent intent = new Intent(context, GoddessDiaryListActivity.class);
        context.startActivity(intent);
    }

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
        //add header
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.mipmap.goddess_diary_top_cover);
        mAdapter.addHeaderView(imageView);

        mAdapter.setOnItemClickListener((adapter, view, position) -> GoddessDiaryDetailActivity.startSelf(GoddessDiaryListActivity.this, mAdapter.getData().get(position).getId(), mResultListener));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setRefreshing(true);
        mAdapter.setEmptyView(R.layout.group_empty);
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
    public void onSuccess(ListBean<DiaryItemBean> diary) {
        Log.i("女神日记列表",diary.toString());
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

    private VLActivity.VLActivityResultListener mResultListener = new VLActivity.VLActivityResultListener() {

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent intent) {
            if (requestCode == GoddessDiaryDetailActivity.DIARY_DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {
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
