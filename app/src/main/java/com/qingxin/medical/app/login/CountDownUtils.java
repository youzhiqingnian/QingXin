package com.qingxin.medical.app.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLScheduler.VLScheduleRepeater;

/**
 * 倒计时的工具类
 *
 * @author zhikuo
 */
public class CountDownUtils {
    private Context mContext;
    private VLScheduleRepeater mRepeater;
    private TextView mGetCodeTv;

    public CountDownUtils(@NonNull Context context, @NonNull TextView getCodeTv) {
        this.mContext = context;
        this.mGetCodeTv = getCodeTv;
    }

    public void startCountDown() {
        if (mRepeater != null) mRepeater.setCanceled();
        mRepeater = VLScheduler.instance.scheduleRepeat(0, 1000, 60, new VLAsyncHandler<VLScheduleRepeater>(mContext, VLScheduler.THREAD_MAIN) {
            @SuppressLint("SetTextI18n")
            @Override
            protected void progress(VLScheduleRepeater repeater) {
                mGetCodeTv.setEnabled(false);
                mGetCodeTv.setText(String.format("%sS重新发送", repeater.getTotal() - repeater.getIndex()));
                mGetCodeTv.setTextColor(0x4dffffff);
                mGetCodeTv.setBackgroundResource(R.drawable.ripple_login_no_click_bg);
            }

            @Override
            protected void handler(boolean succeed) {
                if (mRepeater != null) mRepeater.setCanceled();
                mGetCodeTv.setEnabled(true);
                mGetCodeTv.setText("获取验证码");
                mGetCodeTv.setTextColor(0xffffffff);
                mGetCodeTv.setBackgroundResource(R.drawable.ripple_vcode_click_bg);
            }
        });
    }

    public void endCountDown() {
        if (mRepeater != null) mRepeater.setCanceled();
    }
}
