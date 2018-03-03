package com.qingxin.medical.utils;

import com.google.gson.JsonParseException;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLApplication;
import org.json.JSONException;
import java.text.ParseException;
import retrofit2.HttpException;
/**
 * Date 2018-02-28
 *
 * @author zhikuo1
 */

public class HandErrorUtils {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static boolean isError(String errorCode) {
        boolean isError = false;
        switch (errorCode) {
            case "1000":
            case "1001":
            case "1002":
            case "1020":
            case "1021":
            case "1030":
            case "1040":
            case "1100":
            case "1110":
            case "1200":
                isError = true;
                break;
            case "200":
                isError = false;
                break;
        }
        return isError;

    }


    public static String getErrorMsg(String errorCode) {

        int strRes = 0;
        switch (errorCode) {
            case "1000":
                strRes = R.string.e1000;
                break;
            case "1001":
                strRes = R.string.e1001;
                break;
            case "1002":
                strRes = R.string.e1002;
                break;
            case "1020":
                strRes = R.string.e1020;
                break;
            case "1021":
                strRes = R.string.e1021;
                break;
            case "1030":
                strRes = R.string.e1030;
                break;
            case "1040":
                strRes = R.string.e1040;
                break;
            case "1100":
                strRes = R.string.e1100;
                break;
            case "1110":
                strRes = R.string.e1110;
                break;
            case "1200":
                strRes = R.string.e1200;
                break;
        }

        return getMsg(strRes);

    }

    private static String getMsg(int strRes) {
        return VLApplication.instance().getResources().getString(strRes);
    }


    public static void handleError(Throwable e){
        Throwable throwable = e;
        //获取最根源的异常
        while(throwable.getCause() != null){
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                    ToastUtils.showToast("当前资源不可用，未获取到响应权限");        //权限错误，需要实现
                    break;
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ToastUtils.showToast("当前网络错误，请检查您的网络!");  //均视为网络错误
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ToastUtils.showToast("数据解析错误");            //均视为解析错误
        } else {
            ToastUtils.showToast("未知错误");          //未知错误
        }
    }

}
