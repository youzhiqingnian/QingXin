package com.qingxin.medical.home.medicalbeauty;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.vlee78.android.vl.VLApplication;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class MedicalBeautyPresenter implements MedicalBeautyContract.Presenter {

    @NonNull
    private MedicalBeautyContract.View mStrictSelView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    private MedicalBeautyModel mMedicalBeautyModel;

    MedicalBeautyPresenter(@NonNull MedicalBeautyContract.View goddessDiaryView) {
        mStrictSelView = goddessDiaryView;
        mCompositeSubscription = new CompositeSubscription();
        mStrictSelView.setPresenter(this);
        mMedicalBeautyModel = VLApplication.instance().getModel(MedicalBeautyModel.class);
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
    public void getMedicalBeautyList(String id) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(MedicalStrictService.class).getMedicalBeautyList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<MedicalBeautyListBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mStrictSelView.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<MedicalBeautyListBean>> contentBean) {
                        mStrictSelView.onSucess(contentBean.getContent());
                    }
                })
        );
    }

    @Override
    public void getMedicalBeautySecondList(String id) {
        if (null != mMedicalBeautyModel.getData(id)) {
            mStrictSelView.onGetSecondarySuccess(mMedicalBeautyModel.getData(id));
            return;
        }
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(MedicalStrictService.class).getMedicalBeautySecondaryList(id, "children")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<MedicalBeautyDetailBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mStrictSelView.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<MedicalBeautyDetailBean>> contentBean) {
                        mStrictSelView.onGetSecondarySuccess(contentBean.getContent());
                        mMedicalBeautyModel.putData(id, contentBean.getContent());
                    }
                })
        );
    }
}
