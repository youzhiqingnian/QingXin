package com.qingxin.medical.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vlee78.android.vl.VLApplication;

/**
 * 吐司工具类
 *
 * @author xiachao
 */
public class ToastUtils {
    private static Toast mToast;
    private static ToastBuilder mToastBuilder;
    private static Toast mCustomToast;


    /**
     * 弹出吐司提示。不会重复一直弹
     *
     * @param mMessage edit by huzhi LENGTH_SHORT by renyang
     */
    public static void showToast(final String mMessage) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    show(mMessage);


                }
            });
        } else {
            show(mMessage);
        }

    }

    private static void show(String mMessage) {
        if (showBuilderToast(mMessage)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(VLApplication.instance().getApplicationContext(), mMessage, Toast.LENGTH_SHORT);
        } else {

            mToast.setText(mMessage);
        }
        mToast.show();

    }



    /**
     * @author xionglei
     */
    public static void showToast(final int msg) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    show(msg);


                }
            });
        } else {
            show(msg);
        }
    }

    private static void show(int msg) {
        if (showBuilderToast(VLApplication.instance().getApplicationContext().getText(msg))) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(VLApplication.instance().getApplicationContext(),msg,Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();

    }

    public static void setToastBuilder(ToastBuilder toastBuilder) {
        mToastBuilder = toastBuilder;

    }

    public static boolean showBuilderToast(CharSequence s ) {
        if (mToastBuilder != null) {
            mCustomToast.setText(s);
            mCustomToast.show();
            mToastBuilder = null;
            return true;
        }
        return false;
    }


    public static class ToastBuilder {




        public ToastBuilder() {
            mCustomToast = Toast.makeText(VLApplication.instance().getApplicationContext(),"",Toast.LENGTH_SHORT);

        }

        public ToastBuilder setToastGravity(int gravity, int xOffset, int yOffset) {
            mCustomToast.setGravity(gravity,xOffset,yOffset);
            return this;

        }

        public ToastBuilder setToastTextColor(int messageColor) {
            View view = mCustomToast.getView();
            if(view!=null){
                TextView message=((TextView) view.findViewById(android.R.id.message));
                message.setTextColor(messageColor);
            }
            return this;

        }

        public ToastBuilder setToastBackground(int messageColor, int background) {
            View view = mCustomToast.getView();
            if(view!=null){
                TextView message=((TextView) view.findViewById(android.R.id.message));
                message.setBackgroundResource(background);
                message.setTextColor(messageColor);
            }
            return this;
        }


    }









}
