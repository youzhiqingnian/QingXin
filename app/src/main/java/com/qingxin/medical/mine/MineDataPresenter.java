package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishContract;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
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
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */

public class MineDataPresenter implements MineDataContract.Presenter {


    private volatile boolean isFirstUploadSuccess;
    private MineDataContract.View mUploadHeadView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    MineDataPresenter(@NonNull MineDataContract.View uploadHeadView) {
        mUploadHeadView = uploadHeadView;
        mCompositeSubscription = new CompositeSubscription();
        mUploadHeadView.setPresenter(this);
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
        File file = isFirstUploadSuccess ? diaryPublishParams.getAfterFile() : diaryPublishParams.getBeforeFile();
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
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
//                                publishDiary(diaryPublishParams);
                            }
                        } else {
//                            mDiaryPublishView.onPublishFailed(uploadResultContentBean.getMsg());
                        }
                    }
                }));
    }
}
