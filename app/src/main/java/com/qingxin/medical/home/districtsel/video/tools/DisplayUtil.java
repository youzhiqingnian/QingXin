package com.qingxin.medical.home.districtsel.video.tools;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

public class DisplayUtil {
	public static float dpToPx(Resources res, float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}
	
	public static float spToPx(Resources res, float sp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, res.getDisplayMetrics());
	}
	
	
	public static float spToPx(float sp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
	}
	
	
	public static float dpToPx(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
	}
	
	public static void setBackgroundKeepPadding(View view, int resid) {
		int bottom = view.getPaddingBottom();
		int top = view.getPaddingTop();
		int right = view.getPaddingRight();
		int left = view.getPaddingLeft();
		view.setBackgroundResource(resid);
		view.setPadding(left, top, right, bottom);
	}


	public static int dip2px(Context context, float dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		DebugTools.d("scale2 scale: " + scale);
		return (int) (dip * scale + 0.5f);
	}

}