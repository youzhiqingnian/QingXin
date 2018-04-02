package com.qingxin.medical.mine;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.common.QingXinError;
/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */
public class MineDataContract {

    public interface View extends BaseView<Presenter> {
        void onSuccess(com.qingxin.medical.base.MemBean memBean);
        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void getSession();
    }
}
