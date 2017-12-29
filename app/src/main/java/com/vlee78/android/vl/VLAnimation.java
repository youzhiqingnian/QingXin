package com.vlee78.android.vl;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class VLAnimation 
{
	public static void rotate(View view, int fromDegree, int toDegree, int durationMs)
	{
		if(view == null) return;
		RotateAnimation animation = new RotateAnimation(fromDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(durationMs);
		animation.setFillAfter(true);
		view.startAnimation(animation);
	}
	
	public static void rotate(View view, int roundDurationMs)
	{
		RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(roundDurationMs);
		animation.setRepeatCount(Animation.INFINITE);
		view.startAnimation(animation);
	}
}
