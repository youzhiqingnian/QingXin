package com.qingxin.medical.app.goddessdiary.publish;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.upload.UploadResult;
import com.qingxin.medical.upload.UploadService;
import com.qingxin.medical.utils.HandErrorUtils;
import com.vlee78.android.vl.VLApplication;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018/3/4
 *
 * @author zhikuo
 */
public class DiaryPublishPresenter implements DiaryPublishContract.Presenter {

    private volatile boolean isFirstUploadSuccess;
    private DiaryPublishContract.View mDiaryPublishView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    DiaryPublishPresenter(@NonNull DiaryPublishContract.View diaryPublishView) {
        mDiaryPublishView = diaryPublishView;
        mCompositeSubscription = new CompositeSubscription();
        mDiaryPublishView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void diaryPublish(@NonNull DiaryPublishParams diaryPublishParams) {
        uploadPhotos(diaryPublishParams);
    }

    @Override
    public void updateDiary(@NonNull DiaryPublishParams diaryPublishParams) {

    }

    private void uploadPhotos(@NonNull DiaryPublishParams diaryPublishParams) {
        File file = isFirstUploadSuccess ? diaryPublishParams.getBeforeFile() : diaryPublishParams.getAfterFile();
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(UploadService.class).uploadFile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ContentBean<UploadResult>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<UploadResult> uploadResultContentBean) {
                        if (!HandErrorUtils.isError(uploadResultContentBean.getCode())) {
                            if (!isFirstUploadSuccess) {
                                diaryPublishParams.setBeforeFileName(uploadResultContentBean.getContent().getFilename());
                                isFirstUploadSuccess = true;
                                uploadPhotos(diaryPublishParams);
                            } else {
                                diaryPublishParams.setAfterFileName(uploadResultContentBean.getContent().getFilename());
                                publishDiary(diaryPublishParams);
                            }
                        } else {
                            mDiaryPublishView.onError(uploadResultContentBean.getMsg());
                        }
                    }
                }));
    }

    private void publishDiary(@NonNull DiaryPublishParams diaryPublishParams) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(DiaryPublishService.class).diaryPublish(diaryPublishParams.getWikiId(), diaryPublishParams.getBeforeFileName(), diaryPublishParams.getAfterFileName(), diaryPublishParams.getContent())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<DiaryPublishResult>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<DiaryPublishResult> resultContentBean) {
                        if (!HandErrorUtils.isError(resultContentBean.getCode())) {
                            mDiaryPublishView.onSuccess(resultContentBean.getContent());
                        } else {
                            mDiaryPublishView.onError(resultContentBean.getMsg());
                        }
                    }
                }));
    }
}
