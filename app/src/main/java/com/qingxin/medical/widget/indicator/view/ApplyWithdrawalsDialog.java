package com.qingxin.medical.widget.indicator.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLUtils;

public class ApplyWithdrawalsDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnConfirmWithdrawalListener mListener;

    private TextView mMyQingxinCoinBalanceTv,mWithdrawalsBalanceTv;

    private String mAmount = "";

    /**
     * 分享弹框
     *
     * @param context 上下文
     */
    public ApplyWithdrawalsDialog(Context context) {
        super(context);
        this.mContext = context;
        initConfig();
        initUI();
    }

    private void initConfig() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        assert window != null;
        LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @SuppressLint("InflateParams")
    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_apply_withdrawals_dialog, null);
        mMyQingxinCoinBalanceTv = view.findViewById(R.id.myQingxinCoinBalanceTv);
        mWithdrawalsBalanceTv = view.findViewById(R.id.withdrawalsBalanceTv);
        TextView saveAndSaveTv = view.findViewById(R.id.saveAndSaveTv);
        TextView confirmWithdrawalTv = view.findViewById(R.id.confirmWithdrawalTv);

        saveAndSaveTv.setOnClickListener(this);
        confirmWithdrawalTv.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveAndSaveTv:
                this.dismiss();
                break;
            case R.id.confirmWithdrawalTv:
                if (mListener != null) mListener.confirmWithdrawal(mAmount);
                break;

        }
    }

    public interface OnConfirmWithdrawalListener {

        void confirmWithdrawal(String amount);

    }

    public void setOnConfirmWithdrawalListener(OnConfirmWithdrawalListener confirmWithdrawalDialogListener) {
        mListener = confirmWithdrawalDialogListener;
    }

    public void setBalance(String totalBalance,String withdrawalsBalance){
        mMyQingxinCoinBalanceTv.setText(totalBalance);
        mWithdrawalsBalanceTv.setText(withdrawalsBalance);
        mAmount = withdrawalsBalance;
    }
}
