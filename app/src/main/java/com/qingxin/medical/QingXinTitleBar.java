package com.qingxin.medical;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLTitleBar.VLTitleBarPos;
import com.vlee78.android.vl.VLUtils;

public class QingXinTitleBar {
    private static final int BAR_HEIGHT = 44;
    private static final int BAR_BGCOLOR = 0xffffff;

    private static final int TITLE_SIZE = 20;
    private static final int RIGHT_TEXT_SIZE = 14;
    private static final int TITLE_COLOR = 0xFF464A4C;
    private static final int RIGHT_TEXT_COLOR = 0xff52ccc0;

    private static final int ICON_MARGIN = 22;
    private static final int ICON_WIDTH = 32;
    private static final int ICON_HEIGHT = 24;
    private static final int ICON_RETURN_RESID = R.mipmap.ic_title_left_return;

    public static void init(VLTitleBar titleBar, String title) {
        init(titleBar, title, 0, 0);
    }

    public static void init(VLTitleBar titleBar, String title, int marginLeft, int marginRight) {
        ViewGroup.LayoutParams layoutParams = titleBar.getLayoutParams();
        layoutParams.height = VLUtils.dip2px(BAR_HEIGHT);
        titleBar.setLayoutParams(layoutParams);
        titleBar.setBackgroundColor(BAR_BGCOLOR);
        titleBar.clear(VLTitleBarPos.TitleBarCenter);
        titleBar.addText(VLTitleBarPos.TitleBarCenter, marginLeft, marginRight, title, TITLE_SIZE, TITLE_COLOR, null);
    }

    public static int fixActionBarHeight(View actionBar) {
        actionBar.getLayoutParams().height = VLUtils.dip2px(BAR_HEIGHT);
        actionBar.setBackgroundColor(BAR_BGCOLOR);
        return actionBar.getLayoutParams().height;
    }

    public static View setTitle(VLTitleBar titleBar, String title) {
        titleBar.clear(VLTitleBarPos.TitleBarCenter);
        return titleBar.addText(VLTitleBarPos.TitleBarCenter, 0, 0, title, TITLE_SIZE, TITLE_COLOR, null);
    }

    public static View setLeftTextIcon(VLTitleBar titleBar, String text, int iconResId, float iconWidthDp, float iconHeightDp, View.OnClickListener listener) {
        titleBar.clear(VLTitleBarPos.TitleBarLeft);
        return titleBar.addTextIcon(VLTitleBarPos.TitleBarLeft, ICON_MARGIN, ICON_MARGIN, text, TITLE_SIZE, TITLE_COLOR, ICON_MARGIN, iconResId, iconWidthDp, iconHeightDp, listener);
    }

    public static View setRightIcon(VLTitleBar titleBar, int iconResId, View.OnClickListener listener) {
        titleBar.clear(VLTitleBarPos.TitleBarRight);
        return titleBar.addIcon(VLTitleBarPos.TitleBarRight, ICON_MARGIN, ICON_MARGIN, iconResId, ICON_WIDTH, ICON_HEIGHT, listener);
    }

    public static View addRightIcon(VLTitleBar titleBar, int iconResId, View.OnClickListener listener) {
        return titleBar.addIcon(VLTitleBarPos.TitleBarRight, ICON_MARGIN, ICON_MARGIN, iconResId, ICON_WIDTH, ICON_HEIGHT, listener);
    }

    public static View setRightText(VLTitleBar titleBar, String text, View.OnClickListener listener) {
        titleBar.clear(VLTitleBarPos.TitleBarRight);
        return titleBar.addText(VLTitleBarPos.TitleBarRight, VLUtils.dip2px(15), VLUtils.dip2px(15), text, RIGHT_TEXT_SIZE, RIGHT_TEXT_COLOR, listener);
    }

    public static View addRightText(VLTitleBar titleBar, String text, View.OnClickListener listener) {
        return titleBar.addText(VLTitleBarPos.TitleBarRight, VLUtils.dip2px(15), VLUtils.dip2px(15), text, RIGHT_TEXT_SIZE, RIGHT_TEXT_COLOR, listener);
    }

    public static View setLeftText(VLTitleBar titleBar, String text, View.OnClickListener listener) {
        titleBar.clear(VLTitleBarPos.TitleBarLeft);
        return titleBar.addText(VLTitleBarPos.TitleBarLeft, VLUtils.dip2px(15), VLUtils.dip2px(15), text, TITLE_SIZE, TITLE_COLOR, listener);
    }

    public static View setLeftIcon(VLTitleBar titleBar, int iconResId, View.OnClickListener listener) {
        titleBar.clear(VLTitleBarPos.TitleBarLeft);
        return titleBar.addIcon(VLTitleBarPos.TitleBarLeft, ICON_MARGIN, 0, iconResId, ICON_WIDTH, ICON_HEIGHT, 0, listener);
    }

    public static void setLeftReturn(VLTitleBar titleBar, final Activity activity) {
        titleBar.clear(VLTitleBarPos.TitleBarLeft);
        setLeftIcon(titleBar, ICON_RETURN_RESID, v -> activity.finish());
    }

    public static View setLeftReturnListener(VLTitleBar titleBar, View.OnClickListener listener) {
        titleBar.clear(VLTitleBarPos.TitleBarLeft);
        return setLeftIcon(titleBar, ICON_RETURN_RESID, listener);
    }
}
