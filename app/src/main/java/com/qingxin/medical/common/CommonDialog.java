package com.qingxin.medical.common;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLResHandler;
import com.vlee78.android.vl.VLUtils;
import java.util.List;


/**
 * 公共下弹浮框list. 来源自好蛙项目并做重构
 * Created by liliang on 2016/4/19.
 */
public class CommonDialog extends Dialog implements OnClickListener {

    private Context mContext;
    private VLResHandler mResHandler;
    private List<CommonDialogAdapter.CommonDialogData> mViewDataList;
    private TextView mCancelBtn;
    private String mTitle;

    /**
     * 创建公共弹框
     *
     * @param context      打开弹框的上下文
     * @param theme        布局id
     * @param viewDataList 数据列表
     * @param title        标题
     * @param resHandler   选中列表回调
     */
    CommonDialog(Context context, int theme, List<CommonDialogAdapter.CommonDialogData> viewDataList, String title, VLResHandler resHandler) {
        super(context, theme);
        VLDebug.Assert(viewDataList != null);
        VLDebug.Assert(title != null);
        this.mContext = context;
        this.mResHandler = resHandler;
        this.mViewDataList = viewDataList;
        this.mTitle = title;
        initDialogAttrs();
        initUi();
        addListenser();
    }

    /**
     * 初始化Dialog基础属性
     */
    private void initDialogAttrs() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        Window window = getWindow();
        assert window != null;
        LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        window.getDecorView().setPadding(0, 0, 0, 0);
        // 超出屏幕高度使用铺满全屏 反之使用计算高度
        int height = (VLUtils.dip2px(50) * (mViewDataList.size() + 1));
        int screenHeight = VLUtils.getScreenHeight(mContext);
        window.setLayout(LayoutParams.MATCH_PARENT, height > screenHeight ? LayoutParams.MATCH_PARENT : height);
    }

    /**
     * 初始化Dialog UI界面信息
     */
    @SuppressLint("InflateParams")
    private void initUi() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_dialog_public, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mCancelBtn = view.findViewById(R.id.publicCancel);
        TextView titleTv = view.findViewById(R.id.titleTv);
        titleTv.setText(mTitle);
        CommonDialogAdapter commonDialogAdapter = new CommonDialogAdapter(mContext, mViewDataList, (commonDialogData, position) -> {
            if (mResHandler != null) {
                mResHandler.setParam(commonDialogData);
                mResHandler.handlerSuccess();
            }
            CommonDialog.this.dismiss();
        });
        recyclerView.setAdapter(commonDialogAdapter);
        setContentView(view);
    }

    private void addListenser() {
        mCancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 点击关闭按钮
        if (v == mCancelBtn) {
            this.dismiss();
        }
    }
}
