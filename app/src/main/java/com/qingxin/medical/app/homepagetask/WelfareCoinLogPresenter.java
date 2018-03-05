package com.qingxin.medical.app.homepagetask;

import android.support.annotation.NonNull;
import android.util.Log;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.app.homepagetask.model.WithdrawalsItemBean;
import com.qingxin.medical.base.ContentBean;
import com.qingxin.medical.home.ItemBean;
import com.qingxin.medical.home.ListBean;
import com.qingxin.medical.service.manager.NetRequestListManager;
import com.qingxin.medical.utils.HandErrorUtils;
import com.qingxin.medical.utils.ToastUtils;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
/**
 * Date 2018-02-03
 *
 * @author zhikuo1
 */
public class WelfareCoinLogPresenter implements WelfareCoinLogsListContract.Presenter {

    @NonNull
    private final WelfareCoinLogsListContract.View mWelfareCoinLogView;

    @NonNull
    private CompositeSubscription mCompositeSubscription;

    WelfareCoinLogPresenter(@NonNull WelfareCoinLogsListContract.View welfareCoinLogView) {
        mWelfareCoinLogView = welfareCoinLogView;
        mCompositeSubscription = new CompositeSubscription();
        mWelfareCoinLogView.setPresenter(this);
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
    public void getCoinLogList(int limit, int skip) {
        mCompositeSubscription.add(NetRequestListManager.getCoinLogList(limit, skip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ListBean<CoinLogBean>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<ListBean<CoinLogBean>> coinLogs) {
                        Log.i("清新比列表", "youfanhui"+ coinLogs.toString());
                        if(!HandErrorUtils.isError(coinLogs.getCode())){
                            mWelfareCoinLogView.onSuccess(coinLogs.getContent());
                        }else{
                            ToastUtils.showToast(coinLogs.getMsg());
                        }

                    }
                })
        );
    }

    @Override
    public void checkIn() {
        mCompositeSubscription.add(NetRequestListManager.checkIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<CheckInBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<CheckInBean> checkInBean) {
                        if(null != checkInBean.getMsg()){
                            if(!HandErrorUtils.isError(checkInBean.getCode())){
                                mWelfareCoinLogView.onSuccess(checkInBean.getContent());
                            }else{
                                ToastUtils.showToast(checkInBean.getMsg());
                            }
                        }

                    }
                })
        );
    }

    @Override
    public void applyWithdrawals(String amount) {
        mCompositeSubscription.add(NetRequestListManager.applyWithdrawals(amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<ItemBean<WithdrawalsItemBean>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<ItemBean<WithdrawalsItemBean>> withdrawalsBean) {
                        if(null != withdrawalsBean.getMsg()){
                            if(!HandErrorUtils.isError(withdrawalsBean.getCode())){
                                mWelfareCoinLogView.onSuccess(withdrawalsBean.getContent().getItem());
                            }else{
                                ToastUtils.showToast(withdrawalsBean.getMsg());
                            }
                        }

                    }
                })
        );
    }

    /*@Override
    public void isChcekIn() {
        mCompositeSubscription.add(NetRequestListManager.isChcekIn()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentBean<MemBean>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        HandErrorUtils.handleError(e);
                    }

                    @Override
                    public void onNext(ContentBean<MemBean> memBean) {
                        if(!HandErrorUtils.isError(memBean.getCode())){
                            mWelfareCoinLogView.onSuccess(memBean.getContent());
                        }else{
                            ToastUtils.showToast(memBean.getMsg());
                        }

                    }
                })
        );
    }*/
}
