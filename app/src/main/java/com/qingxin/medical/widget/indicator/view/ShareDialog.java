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
import com.qingxin.medical.R;

public class ShareDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnShareDialogListener mListener;
    private int mFlag;

    public ShareDialog(Context context) {
        this(context, 0);
    }

    /**
     * 分享弹框
     *
     * @param context 上下文
     * @param flag    标识 0表示普通分享 1表示上传视频成功分享
     */
    public ShareDialog(Context context, int flag) {
        super(context);
        this.mContext = context;
        this.mFlag = flag;
        initConfig();
        initUI();
    }

    private void initConfig() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        assert window != null;
        LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    @SuppressLint("InflateParams")
    private void initUI() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_share, null);
        View mCancelShareTv = view.findViewById(R.id.cancelShareTv);
        View mWechatFriendsLl = view.findViewById(R.id.wechatFriendsLl);
        View mWechatCircleLl = view.findViewById(R.id.wechatCircleLl);
        View mQqFriendsLl = view.findViewById(R.id.qqFriendsLl);
        View mQqZoneLl = view.findViewById(R.id.qqZoneLl);
        View mWeiboLl = view.findViewById(R.id.weiboLl);
        View mCopyLinkLl = view.findViewById(R.id.copyLinkLl);
//        if (mFlag == 0) {
//            shareSuccessIv.setVisibility(View.GONE);
//            shareSuccessText.setVisibility(View.GONE);
//            cancel.setVisibility(View.GONE);
//        } else {
//            shareSuccessIv.setVisibility(View.VISIBLE);
//            shareSuccessText.setVisibility(View.VISIBLE);
//            cancel.setVisibility(View.VISIBLE);
//        }
        mCancelShareTv.setOnClickListener(this);
        mWechatFriendsLl.setOnClickListener(this);
        mWechatCircleLl.setOnClickListener(this);
        mQqFriendsLl.setOnClickListener(this);
        mQqZoneLl.setOnClickListener(this);
        mWeiboLl.setOnClickListener(this);
        mCopyLinkLl.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelShareTv:
                this.dismiss();
                break;
            case R.id.wechatFriendsLl:
                if (mListener != null) mListener.weiboShare();
                break;
            case R.id.wechatCircleLl:
                if (mListener != null) mListener.wxFriendShare();
                break;
            case R.id.qqFriendsLl:
                if (mListener != null) mListener.wxCircleShare();
                break;
            case R.id.qqZoneLl:
                if (mListener != null) mListener.qqFriendsShare();
                break;
            case R.id.weiboLl:
                if (mListener != null) mListener.qqZoneShare();
                break;
            case R.id.copyLinkLl:
                if (mListener != null) mListener.copyUrl();
                break;
        }
    }

    public interface OnShareDialogListener {


        void wxFriendShare();

        void wxCircleShare();

        void qqFriendsShare();

        void qqZoneShare();

        void weiboShare();

        void copyUrl();

    }

    public void setOnShareDialogListener(OnShareDialogListener shareDialogListener) {
        mListener = shareDialogListener;
    }
}
