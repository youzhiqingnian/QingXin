package com.qingxin.medical.mine;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.upload.UploadResult;
import com.qingxin.medical.upload.UploadService;
import com.qingxin.medical.utils.HandErrorUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
    public void uploadPortrait(@NonNull File file) {
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
                        } else {
                            mUploadHeadView.onError(new QingXinError(uploadResultContentBean.getMsg()));
                        }
                    }
                }));
    }

    public void modifyPersonalInfo(@Nullable String name, @Nullable String cover, @Nullable String gender, @Nullable String birthday, @Nullable String province_id, @Nullable String city_id) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        if (!TextUtils.isEmpty(cover)) {
            map.put("cover", cover);
        }
        map.put("province_id", province_id);
        map.put("birthday", birthday);
        map.put("gender", gender);
        map.put("city_id", city_id);
        mCompositeSubscription.add(getModel(RetrofitModel.class).getService(ModifyPersonalInfoService.class).modifyPersonalInfo(map)
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
