package com.qingxin.medical.common;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.config.ConfigModel;
import com.qingxin.medical.widget.OnWheelChangedListener;
import com.qingxin.medical.widget.WheelView;
import com.qingxin.medical.widget.adapters.ArrayWheelAdapter;
import com.vlee78.android.vl.VLAsyncHandler;
import java.util.List;

/**
 * @author zhikuo
 */
public class QingXinLocationPopupWindow {

    private final static int INIT_ITEM_SIZE = 5;

    private LocationType mLocationType;
    private WheelView mWheelPro;
    private WheelView mWheelCity;

    private String mTitle;
    private ProvinceData mCurPro;//当前省
    private DistrictItemData mCurCity;//当前城市
    private VLAsyncHandler<QingXinLocationPopupWindow> mCallBack;
    private CommonCustomPopupWindow mContentPopupWindow;
    private Context mContext;
    private List<ProvinceData> mProvinceData;

    private OnWheelChangedListener onHomeOrAddressWheelChangedListener = (wheel, oldValue, newValue) -> {
        switch (wheel.getId()) {
            case R.id.wheelPro:
                updateCities();
                break;
            case R.id.wheelCity:
                updateAreas();
                break;
            default:
                break;
        }
    };

    /**
     * 选择框内容
     */
    public enum LocationType {
        // 只有省
        PROVINCE,
        // 有省和地市
        CITY,
    }

    /**
     * 创建省地县选择框
     *
     * @param locationType 地域选择框类型
     * @param title        标题
     * @param locationId   位置id, id为省或地市或区县id, 由参数locationType决定
     * @param callBack     选中回调
     * @param context      调用上下文
     *                     从服务器获取配置
     */
    public QingXinLocationPopupWindow(LocationType locationType, String title, final String locationId, final VLAsyncHandler<QingXinLocationPopupWindow> callBack, Context context) {
        if (locationType == null) locationType = LocationType.CITY;
        this.mLocationType = locationType;
        this.mProvinceData = QingXinApplication.getInstance().getModel(ConfigModel.class).getProviceData();
        this.mTitle = title;
        this.mCallBack = callBack;
        this.mContext = context;

        this.initPopupWindow(locationId);

        this.initDefaultView();

        this.updateCities();
    }

    private void initPopupWindow(String locationId) {
        final Context context = mContext;
        final VLAsyncHandler<QingXinLocationPopupWindow> callBack = mCallBack;
        this.mContentPopupWindow = new CommonCustomPopupWindow(context, R.layout.group_select_location_popupwindow, R.id.mainMainHomeOrAddress, R.style.PopupAnimation, new ColorDrawable(0x80000000), v -> {
            switch (v.getId()) {
                case R.id.wheelCancel:
                    mContentPopupWindow.dismiss();
                    if (callBack != null) {
                        callBack.handlerError(VLAsyncHandler.VLAsyncRes.VLAsyncResCanceled, null);
                    }
                    break;
                case R.id.wheelOk:
                    mContentPopupWindow.dismiss();
                    if (callBack != null) {
                        mContentPopupWindow.dismiss();
                        callBack.handlerSuccess(QingXinLocationPopupWindow.this);
                    }
                    break;
                default:
                    break;
            }
        });

        // 判断是否是魅族手机，如果是则显示隐藏的空白布局
        if (TextUtils.equals("Meizu", android.os.Build.BRAND)) {
            mContentPopupWindow.mMenuView.findViewById(R.id.mineSelectHomeTownOrAddressEmpty).setVisibility(View.VISIBLE);
        }

        TextView titleTxt = mContentPopupWindow.mMenuView.findViewById(R.id.wheelChoose);
        titleTxt.setText(mTitle);

        mWheelPro = mContentPopupWindow.mMenuView.findViewById(R.id.wheelPro);
        mWheelCity = mContentPopupWindow.mMenuView.findViewById(R.id.wheelCity);

        if (LocationType.CITY.equals(mLocationType)) {
            initCityMode(locationId);
        } else if (LocationType.PROVINCE.equals(mLocationType)) {
            initProvinceMode(locationId);
        }
    }

    private void initCityMode(String locationId) {
        DistrictItemData curCity = QingXinApplication.getInstance().getModel(ConfigModel.class).getCitiesDataMap().get(locationId);
        if (curCity != null) {
            this.mCurCity = curCity;
            this.mCurPro = curCity.getProvinceData();
        }
        mWheelPro.addChangingListener(onHomeOrAddressWheelChangedListener);
        mWheelCity.addChangingListener(onHomeOrAddressWheelChangedListener);
        mWheelPro.setVisibleItems(INIT_ITEM_SIZE);
        mWheelCity.setVisibleItems(INIT_ITEM_SIZE);
        mWheelPro.setViewAdapter(new ArrayWheelAdapter<>(mContext, mProvinceData.toArray()));
    }

    private void initProvinceMode(String locationId) {
        this.mCurPro = QingXinApplication.getInstance().getModel(ConfigModel.class).getProvinceDataMap().get(locationId);
        // 隐藏地市
        mWheelCity.setVisibility(View.GONE);

        mWheelPro.addChangingListener(onHomeOrAddressWheelChangedListener);
        mWheelPro.setVisibleItems(INIT_ITEM_SIZE);
        mWheelPro.setViewAdapter(new ArrayWheelAdapter<>(mContext, mProvinceData.toArray()));
    }

    private void initDefaultView() {
        // 根据传递过来的区县来初始化滚轮的位置
        int cityIndex = mCurCity != null ? mCurCity.getIndex() : 0;
        int proIndex = mCurPro != null ? mCurPro.getIndex() : 0;

        mWheelPro.setCurrentItem(proIndex);
        mWheelCity.setCurrentItem(cityIndex);
    }

    /**
     * 更新省及下属地市界面展示
     */
    private void updateCities() {
        // 当前选中省份index
        int pCurrent = mWheelPro.getCurrentItem();
        // 当前选中身份
        ProvinceData selectedProvince = mProvinceData.get(pCurrent);
        this.mCurPro = selectedProvince;
        // 更新选中身份下的地市界面
        if (selectedProvince != null && mLocationType.ordinal() > LocationType.PROVINCE.ordinal()) {
            mWheelCity.setViewAdapter(new ArrayWheelAdapter<>(mContext, selectedProvince.getCities().toArray()));
            mWheelCity.setCurrentItem(0);
            if (selectedProvince.getCities() != null && !selectedProvince.getCities().isEmpty()){
                this.mCurCity = selectedProvince.getCities().get(0);
            }
            updateAreas();
        }
    }

    /**
     * 更新地市
     */
    private void updateAreas() {
        // 当前选中地市index
        int cCurrent = mWheelCity.getCurrentItem();
        // 当前选中地市
        this.mCurCity = mCurPro.getCities().get(cCurrent);
    }

    /**
     * 根据省的id来获得ProvinceData对象
     */
    public ProvinceData getProvinceData(String proId) {
        if (mProvinceData == null || mProvinceData.size() == 0) {
            return null;
        }
        for (ProvinceData provinceData : mProvinceData) {
            if (provinceData.getId().equals(proId)) {
                return provinceData;
            }
        }
        return null;
    }

    /**
     * 显示窗口
     */
    public void showAtLocation(View parent, int gravity, int x, int y) {
        this.mContentPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    /**
     * 显示窗口
     */
    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 获取当前选中地市
     */
    public DistrictItemData getSelectedCity() {
        return mCurCity;
    }

    /**
     * 获取当前选中省份
     */
    public ProvinceData getSelectedProvince() {
        return mCurPro;
    }

    public PopupWindow getPopupWindow() {
        return mContentPopupWindow;
    }

}
