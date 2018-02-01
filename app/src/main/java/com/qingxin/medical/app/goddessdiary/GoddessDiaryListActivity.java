package com.qingxin.medical.app.goddessdiary;

import android.os.Bundle;
import android.util.Log;

import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.widget.indicator.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by zhikuo1 on 2018-01-31.
 */
public class GoddessDiaryListActivity extends QingXinActivity implements GoddessDiaryContract.View {

    private GoddessDiaryContract.Presenter mPresenter;

    private GoddessDiary mDiary;

    private String skip = "0";

    private boolean isFirst = true;

    private boolean hasMore = true;

    private String limit = "2";

    private RefreshListView mDiaryListRlv;

    private GoddessDiaryListAdapter mAdapter;

    private List<GoddessDiary.ContentBean.ItemsBean> mGoddessDiaryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddess_diary_list);

        new GoddessDiaryPresenter(this, this);


        mPresenter.getGoddessDiaryList("2", skip);

    }

    @Override
    public void setPresenter(GoddessDiaryContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
        mPresenter.subscribe();
    }

    @Override
    public void onSuccess(GoddessDiary diary) {

        mDiary = diary;

        if (skip.equals("0") && mGoddessDiaryList.size() > 0) {
            mGoddessDiaryList.clear();
        }

        mGoddessDiaryList.addAll(mDiary.getContent().getItems());

        int count = mDiary.getContent().getCount();

        if (mGoddessDiaryList.size() == count) {
            hasMore = false;
        }

        Log.i("女神日记列表=", mDiary.toString());

        if (isFirst) {
            setData();
        } else {
            if (mDiary.getContent().getItems() == null || mDiary.getContent().getItems().size() > 0) {
                mAdapter.refresh(mGoddessDiaryList);
            }
        }

        mDiaryListRlv.onRefreshComplete(true);

    }

    private void setData() {

        mDiaryListRlv = findViewById(R.id.mDiaryListRlv);
        mAdapter = new GoddessDiaryListAdapter(this, mDiary.getContent().getItems());
        mDiaryListRlv.setAdapter(mAdapter);
        isFirst = false;


        mDiaryListRlv.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                hasMore = true;
                skip = "0";
                mPresenter.getGoddessDiaryList(limit, skip);
            }

            @Override
            public void onLoadMore() {
                if (hasMore) {
                    skip = (Integer.valueOf(skip) + Integer.valueOf(limit)) + "";
                    mPresenter.getGoddessDiaryList(limit, skip);
                } else {
                    mDiaryListRlv.onRefreshComplete(true);
                }
            }
        });
    }

    @Override
    public void onError(String result) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
