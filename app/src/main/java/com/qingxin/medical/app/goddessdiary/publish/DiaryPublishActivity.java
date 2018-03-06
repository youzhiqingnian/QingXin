package com.qingxin.medical.app.goddessdiary.publish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingxin.medical.QingXinTitleBar;
import com.qingxin.medical.R;
import com.qingxin.medical.album.AlbumAdapter;
import com.qingxin.medical.album.AlbumItemData;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.CommonDialogFactory;
import com.qingxin.medical.common.QingXinLocalPhotoPopupWindow;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyActivity;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyListBean;
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
public class DiaryPublishActivity extends QingXinActivity implements View.OnClickListener, DiaryPublishContract.View {

    public static void startSelf(@NonNull Context context) {
        startSelf(context, null);
    }

    public static void startSelf(@NonNull Context context, @Nullable DiaryItemBean diaryItemBean) {
        Intent intent = new Intent(context, DiaryPublishActivity.class);
        intent.putExtra(DIARYITEMBEAN, diaryItemBean);
        context.startActivity(intent);
    }

    public static final String DIARYITEMBEAN = "DIARYITEMBEAN";

    private TextView mCategoryTv;
    private EditText mDescrTv;
    private AlbumAdapter<Bitmap> mAfterAlbumAdapter, mBeforeAlbumAdapter;
    private Bitmap mBeforePhoto, mAfterPhoto;
    private String mBeforePhotoPath, mAfterPhotoPath;
    private MedicalBeautyListBean mMedicalBeautyListBean;
    private DiaryPublishPresenter mPresenter;
    private DiaryPublishParams mDiaryPublishParams;
    private boolean isEdit;//是否是修改日记

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        TextView publishTv = findViewById(R.id.publishTv);
        chooseItemRl.setOnClickListener(this);
        publishTv.setOnClickListener(this);
        mAfterAlbumAdapter = new AlbumAdapter<>(this, AlbumAdapter.AFTER_PHOTO, 1, new AlbumAdapter.OnClickListener() {
            @Override
            public void onAddItemClicked(View view) {
                getPhotoPopupWindow(AlbumAdapter.AFTER_PHOTO).show(mDescrTv);
            }

            @Override
            public void onItemClicked(View view, int position) {
                getPhotoPopupWindow(AlbumAdapter.AFTER_PHOTO).show(mDescrTv);
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
                getPhotoPopupWindow(AlbumAdapter.BEFORE_PHOTO).show(mDescrTv);
            }
        });
        beforeAlbumRv.setAdapter(mBeforeAlbumAdapter);

        DiaryItemBean diaryItemBean = (DiaryItemBean) getIntent().getSerializableExtra(DIARYITEMBEAN);
        isEdit = diaryItemBean != null;
        if (isEdit) {
            mDescrTv.setText(diaryItemBean.getWords());
//            if (null != diaryItemBean.getProduct()) {
//                mCategoryTv.setText(diaryItemBean.getProduct().getName());
//            }
            mBeforePhoto = VLUtils.getNetWorkBitmap(diaryItemBean.getOper_before_photo());
            mAfterPhoto = VLUtils.getNetWorkBitmap(diaryItemBean.getOper_after_photo());
            AlbumItemData<Bitmap> albumItemData = new AlbumItemData<Bitmap>(mAfterPhoto) {
                @Override
                public String getImageUrl() {
                    return null;
                }
            };
            mAfterAlbumAdapter.addItem(albumItemData);
            AlbumItemData<Bitmap> beforeItemData = new AlbumItemData<Bitmap>(mBeforePhoto) {
                @Override
                public String getImageUrl() {
                    return null;
                }
            };
            mBeforeAlbumAdapter.addItem(beforeItemData);
            publishTv.setText(getResources().getString(R.string.sure_modify));
        }
        mMedicalBeautyListBean = new MedicalBeautyListBean();
        mDiaryPublishParams = new DiaryPublishParams();
        mPresenter = new DiaryPublishPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chooseItemRl:
                MedicalBeautyActivity.startSelf(this, mMedicalBeautyListBean, mResultListener);
                break;
            case R.id.publishTv:
                if (isCheck()) {
                    mPresenter.diaryPublish(mDiaryPublishParams);
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
                    mAfterPhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mBeforePhoto);
                } else {
                    mBeforePhoto = result.getCutResult();
                    AlbumItemData<Bitmap> albumItemData = new AlbumItemData<Bitmap>(mBeforePhoto) {
                        @Override
                        public String getImageUrl() {
                            return null;
                        }
                    };
                    mBeforeAlbumAdapter.addItem(albumItemData);
                    mBeforePhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mBeforePhoto);
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
        if (null == mBeforePhoto || VLUtils.stringIsEmpty(mBeforePhotoPath)) {
            showToast("请选择术前照");
            return false;
        }
        if (null == mAfterPhoto || VLUtils.stringIsEmpty(mAfterPhotoPath)) {
            showToast("请选择术后照");
            return false;
        }
        mDiaryPublishParams.setBeforeFile(new File(mBeforePhotoPath));
        mDiaryPublishParams.setAfterFile(new File(mAfterPhotoPath));
        return true;
    }

    private VLActivityResultListener mResultListener = (requestCode, resultCode, intent) -> {
        if (requestCode == MedicalBeautyActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mMedicalBeautyListBean = (MedicalBeautyListBean) intent.getSerializableExtra(MedicalBeautyActivity.MEDICAL_BEAUTY_LIST_BEAN);
            mCategoryTv.setText(mMedicalBeautyListBean.getName());
            mDiaryPublishParams.setProductId(mMedicalBeautyListBean.getId());
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (VLUtils.isShouldHideInput(getCurrentFocus(), ev)) {
                hideKeyboard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setPresenter(DiaryPublishContract.Presenter presenter) {

    }

    @Override
    public void onSuccess(DiaryPublishResult diaryPublishResult) {
        showToast("发布成功，后台人员审核过后您将看到您的日记");
        DiaryPublishActivity.this.finish();
    }

    @Override
    public void onError(String result) {
        showToast(result);
    }
}
