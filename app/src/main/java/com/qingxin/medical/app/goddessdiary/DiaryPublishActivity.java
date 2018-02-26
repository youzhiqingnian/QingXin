package com.qingxin.medical.app.goddessdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.album.AlbumAdapter;
import com.qingxin.medical.album.LocalAlbumActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLTitleBar;

/**
 * 日记发布界面
 * Date 2018/2/26
 *
 * @author zhikuo
 */
public class DiaryPublishActivity extends QingXinActivity implements View.OnClickListener {

    public static void startSelf(@NonNull VLActivity activity, @NonNull VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, DiaryPublishActivity.class);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    private TextView mCategoryTv;
    private EditText mDescrTv;
    private AlbumAdapter<String> mAfterAlbumAdapter, mBeforeAlbumAdapter;
    public static final int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_publish);
        VLTitleBar titleBar = findViewById(R.id.titleBar);
        RelativeLayout chooseItemRl = findViewById(R.id.chooseItemRl);
        mCategoryTv = findViewById(R.id.categoryTv);
        mDescrTv = findViewById(R.id.descrTv);
        RecyclerView beforeAlbumRv = findViewById(R.id.beforeAlbumRv);
        RecyclerView afterAlbumRv = findViewById(R.id.afterAlbumRv);
        beforeAlbumRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        afterAlbumRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        QingXinTitleBar.init(titleBar, getResources().getString(R.string.publish_diary));
        chooseItemRl.setOnClickListener(this);

        mAfterAlbumAdapter = new AlbumAdapter<>(this, AlbumAdapter.AFTER_PHOTO, 9, new AlbumAdapter.OnClickListener() {
            @Override
            public void onAddItemClicked(View view) {
                LocalAlbumActivity.startSelf(DiaryPublishActivity.this);
            }

            @Override
            public void onItemClicked(View view, int position) {

            }
        });
        afterAlbumRv.setAdapter(mAfterAlbumAdapter);

        mBeforeAlbumAdapter = new AlbumAdapter<>(this, AlbumAdapter.BEFORE_PHOTO, 9, new AlbumAdapter.OnClickListener() {
            @Override
            public void onAddItemClicked(View view) {
                LocalAlbumActivity.startSelf(DiaryPublishActivity.this);
            }

            @Override
            public void onItemClicked(View view, int position) {

            }
        });
        beforeAlbumRv.setAdapter(mBeforeAlbumAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseItemRl:
                break;
            default:
                break;
        }
    }
}
