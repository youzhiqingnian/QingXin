package com.qingxin.medical.mine;

import android.support.annotation.NonNull;
import com.qingxin.medical.app.goddessdiary.publish.DiaryPublishParams;
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.upload.UploadResult;
import com.qingxin.medical.upload.UploadService;
import com.qingxin.medical.utils.HandErrorUtils;
import java.io.File;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */
public class PersonalInformationDataPresenter implements PersonalInformationDataContract.Presenter {

    private PersonalInformationDataContract.View mUploadHeadView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    PersonalInformationDataPresenter(@NonNull PersonalInformationDataContract.View uploadHeadView) {
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
    public void headUpload(@NonNull DiaryPublishParams diaryPublishParams) {
        uploadPhotos(diaryPublishParams);
    }

    @Override
    public void updateDiary(@NonNull DiaryPublishParams diaryPublishParams) {

    }

    private void uploadPhotos(@NonNull DiaryPublishParams diaryPublishParams) {
        File file = diaryPublishParams.getBeforeFile();
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(UploadService.class).uploadFile(body)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ContentBean<UploadResult>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mUploadHeadView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<UploadResult> uploadResultContentBean) {
                        if (!HandErrorUtils.isError(uploadResultContentBean.getCode())) {
                            mUploadHeadView.onUploadHeadSuccess(uploadResultContentBean.getContent());
                            diaryPublishParams.setBeforeFileName(uploadResultContentBean.getContent().getFilename());
//                            modifyHead(diaryPublishParams.getBeforeFileName());
                        } else {
                            mUploadHeadView.onError(new QingXinError(uploadResultContentBean.getMsg()));
                        }
                    }
                }));
    }

    @Override
    public void getSession() {
        mCompositeSubscription.add(NetRequestListManager.isChcekIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<com.qingxin.medical.base.MemBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        mUploadHeadView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<com.qingxin.medical.base.MemBean> memBean) {
                        if (!HandErrorUtils.isError(memBean.getCode())) {
                            mUploadHeadView.onSessionSuccess(memBean.getContent());
                        } else {
                            mUploadHeadView.onError(new QingXinError(memBean.getMsg()));
                        }
                    }
                })
        );
    }

    @Override
    public void saveMyInfo() {

    }


    public void modifyPersonalInfo(@NonNull String name, @NonNull String cover, @NonNull String gender, @NonNull String birthday, @NonNull String province_id, @NonNull String city_id) {
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(ModifyPersonalInfoService.class).modifyPersonalInfo(name,cover,gender,birthday,province_id,city_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<MemBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mUploadHeadView.onError(new QingXinError(e));
                    }

                    @Override
                    public void onNext(ContentBean<MemBean> resultContentBean) {
                        if (!HandErrorUtils.isError(resultContentBean.getCode())) {
                            mUploadHeadView.onModifyPersonalInfoSuccess(resultContentBean.getContent());
                        } else {
                            mUploadHeadView.onError(new QingXinError(resultContentBean.getMsg()));
                        }
                    }
                }));
    }
}
