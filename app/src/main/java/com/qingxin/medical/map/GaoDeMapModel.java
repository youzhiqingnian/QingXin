package com.qingxin.medical.map;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLModel;

/**
 * 高德的相关数据
 *
 * @author zhikuo
 */
public class GaoDeMapModel extends VLModel {

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;

    private AMapLocation mAMapLocation;

    @Override
    protected void onCreate() {
        super.onCreate();
        //初始化定位
        mLocationClient = new AMapLocationClient(getConcretApplication());
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //locationClientOption.setOnceLocationLatest(true);

        //获取一次定位结果：
        //该方法默认为false
        locationClientOption.setOnceLocation(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        //locationClientOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        locationClientOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        locationClientOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(locationClientOption);
        mLocationClient.startLocation();
        //设置定位回调监听
        mLocationClient.setLocationListener(aMapLocation -> {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                mAMapLocation = aMapLocation;
                mLocationClient.stopLocation();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                if (null != aMapLocation){
                    VLDebug.logE("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                }
            }
        });
    }

    public AMapLocation getAMapLocation() {
        return mAMapLocation;
    }
}
