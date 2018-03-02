package com.qingxin.medical.common;

import android.content.Context;
import com.vlee78.android.vl.VLAsyncHandler;

/**
 * Dialog工厂类
 * Created by liliang on 2016/4/20.
 */
public class CommonDialogFactory {

    private final static CommonDialogFactory INSTANCE = new CommonDialogFactory();

    private CommonDialogFactory() {
    }

    public static CommonDialogFactory getInstance() {
        return INSTANCE;
    }

    /**
     * 创建本地相册选择框(3级)
     *
     * @param context                调用上下文
     * @param startCutAfterTakePhoto 是否开启剪裁处理
     * @param callBack               选中回调
     */
    public static QingXinLocalPhotoPopupWindow createLoadLocalPhotoPopupWindow(final Context context, boolean startCutAfterTakePhoto, final VLAsyncHandler<QingXinLocalPhotoPopupWindow.LoadPhotoResult> callBack) {
        return new QingXinLocalPhotoPopupWindow(context, startCutAfterTakePhoto, callBack);
    }
}
