package com.qingxin.medical.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.JsonParseException;
import com.qingxin.medical.R;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.user.SessionModel;
import com.qingxin.medical.user.UserModel;
import com.vlee78.android.vl.VLApplication;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * Date 2018-02-28
 *
 * @author zhikuo1
 */

public class HandErrorUtils {

    public static final String LOGOUT_ACTION = "com.archie.action.LOGOUT_ACTION";

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
            case "1003":
            case "1010":
            case "1011":
            case "1012":
            case "1020":
            case "1021":
            case "1030":
            case "1040":
            case "1050":
            case "1051":
            case "1100":
            case "1110":
            case "1111":
            case "1200":
                isError = true;
                break;
            case "1005":
                logout();
                isError = true;
                break;

            case "200":
                isError = false;
                break;
        }
        return isError;
    }

    private static void logout() {
        VLApplication.instance().getModel(UserModel.class).onLogout();
        VLApplication.instance().getModel(SessionModel.class).onLogout();
        Intent intent = new Intent(LOGOUT_ACTION);
        LocalBroadcastManager.getInstance(VLApplication.instance()).sendBroadcast(intent);
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
            case "1003":
                strRes = R.string.e1003;
                break;
            case "1005":
                strRes = R.string.e1005;
                break;
            case "1010":
                strRes = R.string.e1010;
                break;
            case "1011":
                strRes = R.string.e1011;
            case "1012":
                strRes = R.string.e1012;
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
            case "1050":
                strRes = R.string.e1050;
                break;
            case "1051":
                strRes = R.string.e1051;
                break;
            case "1100":
                strRes = R.string.e1100;
                break;
            case "1110":
                strRes = R.string.e1110;
                break;
            case "1111":
                strRes = R.string.e1111;
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

    public static void handleError(QingXinError error) {
        if (null == error.getThrowable()) {
            ToastUtils.showToast(error.getMsg());
        } else {
            handleError(error.getThrowable());
        }
    }

    public static void handleError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                    //权限错误，需要实现
                    ToastUtils.showToast("当前资源不可用，未获取到响应权限");
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
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {//均视为解析错误
            ToastUtils.showToast("数据解析错误");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showToast("连接超时,请稍后再试");
        } else if (e instanceof ConnectException) {
            ToastUtils.showToast("当前网络错误，请检查您的网络!");
        } else {
            ToastUtils.showToast("未知错误");
        }
    }
}
