package com.qingxin.medical.utils;

import com.qingxin.medical.R;
import com.vlee78.android.vl.VLApplication;

/**
 * Date 2018-02-28
 *
 * @author zhikuo1
 */

public class HandErrorUtils {

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

}
