package com.qingxin.medical.mine;

import android.support.annotation.NonNull;

import com.qingxin.medical.app.BasePresenter;
import com.qingxin.medical.app.BaseView;
import com.qingxin.medical.app.homepagetask.model.MemBean;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.upload.UploadResult;

import java.io.File;

/**
 * Date 2018-03-07
 *
 * @author zhikuo1
 */
public class PersonalInformationDataContract {

    public interface View extends BaseView<Presenter> {
        void onModifyPersonalInfoSuccess(MemBean memBean);
        void onUploadHeadSuccess(UploadResult uploadResultBean);
        void onError(QingXinError error);
    }

    public interface Presenter extends BasePresenter {
        void uploadPortrait(@NonNull File file);
    }
}
