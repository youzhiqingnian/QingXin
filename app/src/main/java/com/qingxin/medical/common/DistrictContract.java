package com.qingxin.medical.common;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;

import java.util.List;

/**
 * Date 2018/4/8
 *
 * @author zhikuo
 */
public class DistrictContract {

    public interface View extends BaseView<Presenter> {
        void onSuccess(List<DistrictItemData> districtBeans);

        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void getDistrictData(String level);
    }
}
