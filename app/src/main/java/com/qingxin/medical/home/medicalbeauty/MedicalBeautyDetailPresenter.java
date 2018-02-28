package com.qingxin.medical.home.medicalbeauty;

import android.support.annotation.NonNull;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ItemBean;
import com.qingxin.medical.retrofit.RetrofitModel;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
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
public class MedicalBeautyDetailPresenter implements MedicalBeautyDetailContract.Presenter {

    @NonNull
    private final MedicalBeautyDetailContract.View mMedicalBeautyDetailView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    MedicalBeautyDetailPresenter(@NonNull MedicalBeautyDetailContract.View goddessDiaryView) {
        mMedicalBeautyDetailView = goddessDiaryView;
        mCompositeSubscription = new CompositeSubscription();
        mMedicalBeautyDetailView.setPresenter(this);
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
    public void getMedicalBeautyDetail(String id) {
        mCompositeSubscription.add(VLApplication.instance().getModel(RetrofitModel.class).getService(MedicalStrictService.class).getMedicalBeautyDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ItemBean<MedicalBeautyRealDetailBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMedicalBeautyDetailView.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(ContentBean<ItemBean<MedicalBeautyRealDetailBean>> contentBean) {
                        if(!HandErrorUtils.isError(contentBean.getCode())){
                            mMedicalBeautyDetailView.onSucess(contentBean.getContent());
                        }else{
                            ToastUtils.showToast(contentBean.getMsg());
                        }

                    }
                })
        );
    }
}
