package com.qingxin.medical.app.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qingxin.medical.R;
import com.qingxin.medical.app.Constants;
import com.qingxin.medical.app.homepagetask.HomePageTaskActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.utils.ToastUtils;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;

/**
 * 启动页
 * Date 2018-02-05
 * @author zhikuo1
 */
public class SplashActivity extends QingXinActivity {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        requestGaodeLoction();
        VLScheduler.instance.schedule(3000, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                // 进入主页
                HomePageTaskActivity.startSelf(SplashActivity.this, 0);
                finish();
            }
        });
    }

    private void requestGaodeLoction() {

        //初始化定位
        mLocationClient = new AMapLocationClient(this);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
//        mLocationOption.setOnceLocationLatest(true);

        //获取一次定位结果：
        //该方法默认为false
        mLocationOption.setOnceLocation(true);

//        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(1000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);


        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);


        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {

                //权限还没有授予，需要在这里写申请权限的代码
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE},
                        Constants.GAODE_MAP_GRANTED_REQUEST_CODE);
            } else {
                //权限已经被授予，在这里直接写要执行的相应方法即可
                //启动定位
                mLocationClient.startLocation();
            }

        } else {
            //启动定位
            mLocationClient.startLocation();
        }


        //设置定位回调监听
       /* mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {

                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                        // 提示定位失败
                        Toast.makeText(this, getStr(R.string.location_failuer), Toast.LENGTH_LONG);
                    }
                }
            }
        });*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == Constants.GAODE_MAP_GRANTED_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationClient.startLocation();
            } else {
                ToastUtils.showToast("没有获取到定位权限");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
