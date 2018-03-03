package com.qingxin.medical.app.goddessdiary;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.qingxin.medical.album.AlbumItemData;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.common.CommonDialogFactory;
import com.qingxin.medical.common.QingXinLocalPhotoPopupWindow;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

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
    private AlbumAdapter<Bitmap> mAfterAlbumAdapter, mBeforeAlbumAdapter;
    public static final int REQUEST_CODE = 1001;
    private Bitmap mBeforePhoto, mAfterPhoto;

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

        mAfterAlbumAdapter = new AlbumAdapter<>(this, AlbumAdapter.AFTER_PHOTO, 1, new AlbumAdapter.OnClickListener() {
            @Override
            public void onAddItemClicked(View view) {
                getPhotoPopupWindow(AlbumAdapter.AFTER_PHOTO).show(mDescrTv);
            }

            @Override
            public void onItemClicked(View view, int position) {
            }
        });
        afterAlbumRv.setAdapter(mAfterAlbumAdapter);

        mBeforeAlbumAdapter = new AlbumAdapter<>(this, AlbumAdapter.BEFORE_PHOTO, 1, new AlbumAdapter.OnClickListener() {
            @Override
            public void onAddItemClicked(View view) {
                getPhotoPopupWindow(AlbumAdapter.BEFORE_PHOTO).show(mDescrTv);
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
                if (isCheck()) {
                    //TODO
                }
                break;
            default:
                break;
        }
    }

    private QingXinLocalPhotoPopupWindow getPhotoPopupWindow(@NotNull String whichPhoto) {
        return CommonDialogFactory.createLoadLocalPhotoPopupWindow(this, true, new VLAsyncHandler<QingXinLocalPhotoPopupWindow.LoadPhotoResult>(null, VLScheduler.THREAD_MAIN) {
            @Override
            protected void handler(boolean succeed) {
                if (!succeed) return;
                QingXinLocalPhotoPopupWindow.LoadPhotoResult result = getParam();
                if (null == result) return;
                if (AlbumAdapter.AFTER_PHOTO.equals(whichPhoto)) {
                    mAfterPhoto = result.getCutResult();
                    AlbumItemData<Bitmap> albumItemData = new AlbumItemData<Bitmap>(mAfterPhoto) {
                        @Override
                        public String getImageUrl() {
                            return null;
                        }
                    };
                    mAfterAlbumAdapter.addItem(albumItemData);
                } else {
                    mBeforePhoto = result.getCutResult();
                    AlbumItemData<Bitmap> albumItemData = new AlbumItemData<Bitmap>(mBeforePhoto) {
                        @Override
                        public String getImageUrl() {
                            return null;
                        }
                    };
                    mBeforeAlbumAdapter.addItem(albumItemData);
                }
            }
        });
    }

    private boolean isCheck() {
        if (VLUtils.stringIsEmpty(mCategoryTv.getText().toString().trim())) {
            showToast("请选择项目类别");
            return false;
        }
        if (VLUtils.stringIsEmpty(mDescrTv.getText().toString().trim())) {
            showToast("请输入你的变美心得吧~");
            return false;
        }
        if (null == mBeforePhoto) {
            showToast("请选择术前照");
            return false;
        }
        if (null == mAfterPhoto) {
            showToast("请选择术后照");
            return false;
        }
        return true;
    }
}
