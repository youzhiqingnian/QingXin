package com.qingxin.medical.app.goddessdiary;

import android.os.Bundle;
import android.util.Log;

import com.qingxin.medical.QingXinListView;
import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.GoddessDiary;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.widget.indicator.view.RefreshListView;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLListView;
import com.vlee78.android.vl.VLTitleBar;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 女神日记列表
 * Date 2018-01-31
 *
 * @author zhikuo1
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

    private QingXinListView mQingXinListView;
    private VLListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddess_diary_list);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.goddess_diary));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        new GoddessDiaryPresenter( this);
        mListView = findViewById(R.id.listView);


        mPresenter.getGoddessDiaryList("2", skip);

        updateListView();

    }

    private void updateListView(){
        if (null == mQingXinListView){
            mQingXinListView  = new QingXinListView(mListView, new QingXinListView.QingXinListViewDelegate() {
                @Override
                public void onLoadMore(VLListView listView, boolean isClear, VLAsyncHandler<Object> asyncHandler) {
                    //TODO
                }

                @Override
                public void onEmpty(VLListView listView) {

                }
            });
        }else {
            mQingXinListView.update();
        }
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
        //TODO
        //mDiaryListRlv = findViewById(R.id.mDiaryListRlv);
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
