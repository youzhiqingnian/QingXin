package com.qingxin.medical.common;

import android.content.Context;

import com.qingxin.medical.R;
import com.qingxin.medical.prototype.GenderData;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLResHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

    /**
     * 创建性别选择Dialog
     *
     * @param context    调用上下文
     * @param title      标题
     * @param resHandler 选中回调
     */
    public CommonDialog createGenderSelectDialog(Context context, String title, VLResHandler resHandler) {
        return createGenderSelectDialog(context, R.style.BaseAlertDialog, title, resHandler);
    }

    /**
     * 创建性别选择Dialog
     *
     * @param context    调用上下文
     * @param theme      布局样式资源id
     * @param title      标题
     * @param resHandler 选中回调
     */
    public CommonDialog createGenderSelectDialog(Context context, int theme, String title, VLResHandler resHandler) {
        List<CommonDialogAdapter.CommonDialogData> dataList = new ArrayList<>();
        Collection<GenderData> dataValues = GenderData.KEY_ID_GENDER_MAP.values();
        for (final GenderData genderData : dataValues) {
            CommonDialogAdapter.CommonDialogData data = new CommonDialogAdapter.CommonDialogData() {

                @Override
                public int getId() {
                    return genderData.getId();
                }

                @Override
                public String getName() {
                    return genderData.getName();
                }

                @Override
                public Object getTag() {
                    return genderData;
                }
            };
            dataList.add(data);
        }
        return new CommonDialog(context, theme, dataList, title, resHandler);
    }

    /**
     * 创建日期选择Dialog(年月日)
     *
     * @param initDate 日期框初始时间
     * @param title    日期框标题
     * @param context  调用上下文
     * @param callback 点击日期框后回调
     */
    public static QingXinDatePopuWindow createDatePopuWindow(Date initDate, String title, Context context, VLAsyncHandler<QingXinDatePopuWindow> callback) {
        return new QingXinDatePopuWindow(initDate, title, callback, context);
    }

    /**
     * 创建日期选择Dialog
     *
     * @param initDate 日期框初始时间
     * @param title    日期框标题
     * @param context  调用上下文
     * @param callback 点击日期框后回调
     */
    public static QingXinDatePopuWindow createDatePopuWindow(QingXinDatePopuWindow.DateFormatType type, Date initDate, String title, Context context, VLAsyncHandler<QingXinDatePopuWindow> callback) {
        return new QingXinDatePopuWindow(type, initDate, title, callback, context);
    }

    /**
     * 创建日期范围选择Dialog
     *
     * @param startDate 初始化日期框起始时间
     * @param endDate   初始化日期框截止时间
     * @param context   调用上下文
     * @param callback  点击日期框后回调
     */
    public static QingXinDatePopuWindow createDateRangeWindow(Date startDate, Date endDate, Context context, VLAsyncHandler<QingXinDatePopuWindow> callback) {
        return new QingXinDatePopuWindow(startDate, endDate, callback, context);
    }
}
