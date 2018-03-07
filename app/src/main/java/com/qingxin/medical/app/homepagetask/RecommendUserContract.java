package com.qingxin.medical.app.homepagetask;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.RecommendResultBean;
import com.qingxin.medical.common.QingXinError;

/**
 * Date 2018-01-31
 *
 * @author zhikuo1
 */
public class RecommendUserContract {

    public interface View extends BaseView<Presenter> {

        void onSuccess(RecommendResultBean result);

        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void submitRecommendUser(String name, String mobile, String product, String inthos, String remark);
    }
}
