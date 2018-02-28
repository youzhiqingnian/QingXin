package com.qingxin.medical.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.map.LocationService;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLModel;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author zhikuo
 */
public class RetrofitModel extends VLModel {

    private static final String TAG = RetrofitModel.class.getSimpleName();

    private static final String BASE_URL = "http://47.93.101.229:3030/";

    private Retrofit mRetrofit;

    /**
     * 连接超时时间
     */
    private final static int CONNECT_TIMEOUT = 15000;
    /**
     * 读取超时时间
     */
    private final static int READ_TIMEOUT = 15000;

    @Override
    protected void onCreate() {
        super.onCreate();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS).writeTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS).readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            String cityCode = "";
            LocationService locationService = QingXinApplication.getInstance().getLocationService();
            if (null != locationService && null != locationService.getAMLocation()) {
                AMapLocation aMapLocation = locationService.getAMLocation();
                cityCode = aMapLocation.getCityCode();
            }
            Log.e("RetrofitModel","cityCode = " + cityCode);
            Request request = original.newBuilder()
                    .header("citycode", cityCode)
                    .header("token", null == QingXinApplication.getInstance().getLoginUser() ? "" : QingXinApplication.getInstance().getLoginUser().getToken())
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        });

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            //打印retrofit日志
            VLDebug.logE(TAG, "retrofitBack = " + message);
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);
        mRetrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public <T> T getService(@NonNull Class<T> service) {
        return mRetrofit.create(service);
    }
}
