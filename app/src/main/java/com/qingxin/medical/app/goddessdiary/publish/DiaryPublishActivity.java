package com.qingxin.medical.app.goddessdiary.publish;

import android.app.Activity;
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
import com.qingxin.medical.app.goddessdiary.CollectBean;
import com.qingxin.medical.app.goddessdiary.DiaryDetailContract;
import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailBean;
import com.qingxin.medical.app.goddessdiary.GoddessDiaryDetailPresenter;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.common.CommonDialogFactory;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.common.QingXinLocalPhotoPopupWindow;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyActivity;
import com.qingxin.medical.home.medicalbeauty.MedicalBeautyListBean;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLActivity;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLResHandler;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import static com.qingxin.medical.app.goddessdiary.publish.DiaryPublishContract.Presenter;
import static com.qingxin.medical.app.goddessdiary.publish.DiaryPublishContract.PublishView;

/**
 * 日记发布界面
 * Date 2018/2/26
 *
 * @author zhikuo
 */
public class DiaryPublishActivity extends QingXinActivity implements View.OnClickListener, PublishView {

    public static void startSelf(@NonNull VLActivity activity) {
        startSelf(activity, null, null);
    }

    public static void startSelf(@NonNull VLActivity activity, @Nullable DiaryItemBean diaryItemBean, VLActivityResultListener resultListener) {
        Intent intent = new Intent(activity, DiaryPublishActivity.class);
        intent.putExtra(DIARYITEMBEAN, diaryItemBean);
        activity.setActivityResultListener(resultListener);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public static final String DIARYITEMBEAN = "DIARYITEMBEAN";
    public static final int REQUEST_CODE = 1111;

    private View mLeftReturn;
    private TextView mCategoryTv, mPublishTv;
    private EditText mDescrTv;
    private AlbumAdapter<Bitmap> mAfterAlbumAdapter, mBeforeAlbumAdapter;
    private Bitmap mBeforePhoto, mAfterPhoto;
    private String mBeforePhotoPath, mAfterPhotoPath;
    private MedicalBeautyListBean mMedicalBeautyListBean;
    private DiaryPublishPresenter mPresenter;
    private DiaryPublishParams mDiaryPublishParams;
    private boolean isEdit;//是否是修改日记
    private DiaryItemBean mDiaryItemBean;
    private DiaryDetailContract.Presenter mDiaryDetailPresenter;
    private boolean isWikiChanged, isBeforeChanged, isAfterChanged, isWordsChanged;

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        if (null != mDiaryDetailPresenter) {
            mDiaryDetailPresenter.subscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        if (null != mDiaryDetailPresenter) {
            mDiaryDetailPresenter.unsubscribe();
        }
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
        mLeftReturn = QingXinTitleBar.setLeftReturnListener(titleBar, this);
        mPublishTv = findViewById(R.id.publishTv);
        chooseItemRl.setOnClickListener(this);
        mPublishTv.setOnClickListener(this);
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

        mDiaryItemBean = (DiaryItemBean) getIntent().getSerializableExtra(DIARYITEMBEAN);
        isEdit = mDiaryItemBean != null;
        mMedicalBeautyListBean = new MedicalBeautyListBean();
        mDiaryPublishParams = new DiaryPublishParams();
        mPresenter = new DiaryPublishPresenter(this);
        if (isEdit) {
            if (null != mDiaryItemBean.getWiki()) {
                mCategoryTv.setText(mDiaryItemBean.getWiki().getName());
            }
            mDiaryPublishParams.setDiaryId(mDiaryItemBean.getId());
            mDiaryPublishParams.setWikiId(mDiaryItemBean.getWiki_id());
            mDiaryDetailPresenter = new GoddessDiaryDetailPresenter(new DiaryDetailContract.View() {
                @Override
                public void onSuccess(GoddessDiaryDetailBean mDiary) {
                    DiaryItemBean diaryItemBean = mDiary.getItem();
                    if (null != diaryItemBean.getWords()) {
                        mDiaryItemBean.setWords(diaryItemBean.getWords());
                        mDescrTv.setText(mDiaryItemBean.getWords());
                        mDescrTv.setSelection(mDiaryItemBean.getWords().length());
                        mDiaryPublishParams.setContent(mDiaryItemBean.getWords());
                    }
                }

                @Override
                public void onSuccess(CollectBean mCollectBean) {
                }

                @Override
                public void onError(QingXinError error) {

                }

                @Override
                public void setPresenter(DiaryDetailContract.Presenter presenter) {
                }
            });
            mDiaryDetailPresenter.getGoddessDiaryDetail(mDiaryItemBean.getId());
            mPublishTv.setText(getResources().getString(R.string.sure_modify));
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_NORMAL, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    mBeforePhoto = VLUtils.getNetWorkBitmap(mDiaryItemBean.getOper_before_photo());
                    mAfterPhoto = VLUtils.getNetWorkBitmap(mDiaryItemBean.getOper_after_photo());
                    VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                        @Override
                        protected void process(boolean canceled) {
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
                            mAfterPhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mAfterPhoto);
                            mBeforePhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mBeforePhoto);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mLeftReturn) {
            if (VLUtils.stringIsNotEmpty(mDiaryPublishParams.getWikiId()) || VLUtils.stringIsNotEmpty(mDescrTv.getText().toString().trim()) || null != mBeforePhoto || VLUtils.stringIsNotEmpty(mBeforePhotoPath) || null != mAfterPhoto || VLUtils.stringIsNotEmpty(mAfterPhotoPath)) {
                showOkCancelDialog("提示", "您确定退出吗?", "确定", "取消", true, new VLResHandler() {
                    @Override
                    protected void handler(boolean succeed) {
                        if (!succeed) return;
                        DiaryPublishActivity.this.finish();
                    }
                });
            } else {
                finish();
            }
        } else {
            switch (v.getId()) {
                case R.id.chooseItemRl:
                    MedicalBeautyActivity.startSelf(this, mMedicalBeautyListBean, mResultListener);
                    break;
                case R.id.publishTv:
                    if (isCheck()) {
                        if (!isEdit) {
                            mPresenter.diaryPublish(mDiaryPublishParams);
                            mPublishTv.setEnabled(false);
                            mPublishTv.setText("正在发布中,请稍后");
                        } else {
                            if (!isWordsChanged && !isWikiChanged && !isAfterChanged && !isBeforeChanged) {//都没变动
                                this.finish();
                                return;
                            }
                            if (!isWordsChanged) {
                                mDiaryPublishParams.setContent(null);
                            }
                            if (!isWikiChanged) {
                                mDiaryPublishParams.setWikiId(null);
                            }
                            if (!isBeforeChanged) {
                                mDiaryPublishParams.setBeforeFile(null);
                            }
                            if (!isAfterChanged) {
                                mDiaryPublishParams.setAfterFile(null);
                            }
                            mPresenter.diaryUpdate(mDiaryPublishParams);
                            mPublishTv.setEnabled(false);
                            mPublishTv.setText("正在发布中,请稍后");
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        onClick(mLeftReturn);
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
                    mAfterPhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mAfterPhoto);
                    isAfterChanged = isEdit;
                } else if (AlbumAdapter.BEFORE_PHOTO.equals(whichPhoto)) {
                    mBeforePhoto = result.getCutResult();
                    AlbumItemData<Bitmap> albumItemData = new AlbumItemData<Bitmap>(mBeforePhoto) {
                        @Override
                        public String getImageUrl() {
                            return null;
                        }
                    };
                    mBeforeAlbumAdapter.addItem(albumItemData);
                    mBeforePhotoPath = VLUtils.saveBitmap(QingXinApplication.getInstance(), mBeforePhoto);
                    isBeforeChanged = isEdit;
                }
            }
        });
    }

    private boolean isCheck() {
        if (VLUtils.stringIsEmpty(mDiaryPublishParams.getWikiId())) {
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
        isWordsChanged = isEdit && !(mDescrTv.getText().toString().trim()).equals(mDiaryPublishParams.getContent());
        mDiaryPublishParams.setContent(mDescrTv.getText().toString().trim());
        return true;
    }

    private VLActivityResultListener mResultListener = (requestCode, resultCode, intent) -> {
        if (requestCode == MedicalBeautyActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            mMedicalBeautyListBean = (MedicalBeautyListBean) intent.getSerializableExtra(MedicalBeautyActivity.MEDICAL_BEAUTY_LIST_BEAN);
            mCategoryTv.setText(mMedicalBeautyListBean.getName());
            isWikiChanged = isEdit && !mMedicalBeautyListBean.getId().equals(mDiaryPublishParams.getWikiId());
            mDiaryPublishParams.setWikiId(mMedicalBeautyListBean.getId());
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
    public void setPresenter(Presenter presenter) {

    }

    @Override
    public void onPublishSuccess(DiaryPublishResult diaryPublishResult) {
        showToast("发布成功，后台人员审核过后您将看到您的日记");
        DiaryPublishActivity.this.finish();
    }

    @Override
    public void onUpdateSuccess(DiaryItemBean diaryItemBean) {
        showToast("更新成功");
        Intent intent = new Intent();
        intent.putExtra(DIARYITEMBEAN, diaryItemBean);
        setResult(RESULT_OK, intent);
        DiaryPublishActivity.this.finish();
    }

    @Override
    public void onPublishFailed(QingXinError error) {
        HandErrorUtils.handleError(error);
        mPublishTv.setEnabled(true);
        mPublishTv.setText(isEdit ? getResources().getString(R.string.sure_modify) : getResources().getString(R.string.sure_publish));
    }

}
