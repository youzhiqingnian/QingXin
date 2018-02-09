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
    private final MedicalBeautyContract.View mStrictSelView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    MedicalBeautyPresenter(@NonNull MedicalBeautyContract.View goddessDiaryView) {
        mStrictSelView = goddessDiaryView;
        mCompositeSubscription = new CompositeSubscription();
        mStrictSelView.setPresenter(this);
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
}
