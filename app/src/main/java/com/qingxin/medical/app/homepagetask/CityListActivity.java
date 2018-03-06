package com.qingxin.medical.app.homepagetask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.model.HomeBean;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;

import java.io.Serializable;
import java.util.List;

/**
 * Date 2018-03-06
 *
 * @author zhikuo1
 */

public class CityListActivity extends QingXinActivity {

    public static final String OPEN_CITY_LIST = "OPEN_CITY_LIST";
    public static final String CURRENT_SELECT_CITY = "CURRENT_SELECT_CITY";
    public static final int CITY_LIST_REQUEST_CODE = 1112;

    public static void startSelf(VLActivity activity, List<HomeBean.OpencitysBean> opencitys, VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, CityListActivity.class);
        intent.putExtra(OPEN_CITY_LIST, (Serializable) opencitys);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, CITY_LIST_REQUEST_CODE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddess_diary_list);
        List<HomeBean.OpencitysBean> opencitysBeans = (List<HomeBean.OpencitysBean>) getIntent().getSerializableExtra(OPEN_CITY_LIST);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.choose_city));
        QingXinTitleBar.setLeftReturn(titleBar, this);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.swipeLayout);
        refreshLayout.setEnabled(false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CityListAdapter mAdapter = new CityListAdapter(opencitysBeans);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(CURRENT_SELECT_CITY, mAdapter.getData().get(position));
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        recyclerView.setAdapter(mAdapter);
    }
}
