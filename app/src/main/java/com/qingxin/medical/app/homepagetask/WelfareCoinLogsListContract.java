package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.CheckInBean;
import com.qingxin.medical.app.homepagetask.model.CoinLogBean;
import com.qingxin.medical.app.homepagetask.model.WithdrawalsItemBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.home.ListBean;

/**
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class WelfareCoinLogsListContract {

    public interface View extends BaseView<Presenter> {

        void onSuccess(ListBean<CoinLogBean> coinLog);

        void onSuccess(CheckInBean checkIn);

        void onSuccess(WithdrawalsItemBean withdrawalsBean);

        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {

        void getCoinLogList(int limit, int skip);

        void checkIn();

        void applyWithdrawals(String amount);
    }
}
