package com.qingxin.medical.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class MyCollectionListFragment extends VLFragment implements View.OnClickListener{

    private View mRootView;

    private AgencyAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private boolean isClear;
    private StrictSelPresenter mPresenter;

    private LinearLayout mCollectionTypeLl;

    private TextView mProductTv, mDiaryTv;

    public MyCollectionListFragment() {
    }

    public static MyCollectionListFragment newInstance() {
        return new MyCollectionListFragment();
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
        initListener();
    }



    private void initView() {

        LinearLayout collectionTypeLl = mRootView.findViewById(R.id.collectionTypeLl);
        collectionTypeLl.setVisibility(View.VISIBLE);
        mProductTv = mRootView.findViewById(R.id.productTv);
        mDiaryTv = mRootView.findViewById(R.id.diaryTv);
    }

    private void initListener() {
        mProductTv.setOnClickListener(this);
        mDiaryTv.setOnClickListener(this);
    }

    private void getDiaryList(boolean isClear) {
        this.isClear = isClear;
        int skip = isClear ? 0 : mAdapter.getData().size();
        if (isClear) {
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
//        mPresenter.getStrictSelList(mType, QingXinConstants.ROWS, skip);
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.productTv:
                // 我收藏的产品


                break;

            case R.id.diaryTv:
                // 我收藏的日记


                break;

        }

    }
}
