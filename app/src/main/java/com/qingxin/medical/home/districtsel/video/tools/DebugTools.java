package com.qingxin.medical.home.districtsel.video.tools;

import android.util.Log;

public class DebugTools {
	private static final String TAG = "DebugUtils";
	public static final boolean DEBUG = false;
	
    public static void d(String info){
    	if(DEBUG){
    		Log.d(TAG, info);
    	}
    }
    
    public static void e(String info){
    	if(DEBUG){
    		Log.e(TAG, info);
    	}
    }
    
    public static void test(){
//    	for(int i = 0; i < 10; i++){
//        	int random = CommonUtils.getRandInt(0, 10);
//        	DebugUtils.d("random2: " + random);
//    	}

    }
}
