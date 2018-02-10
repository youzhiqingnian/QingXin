package com.qingxin.medical.home.districtsel.video.tools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.telephony.TelephonyManager;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class CommonTools {

	public static String getCurSystemFormatTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = format.format(new Date());
		return time;
	}
	
	public static String getCurSystemYMDTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String time = format.format(new Date());
		return time;
	}

	public static String getDeviceId(Context context) {
		if (context == null) {
			return null;
		}

		String id = "";

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		id = tm.getDeviceId();

		return id;
	}

	public static String getVersionValue(Context a) {
		PackageManager packageManager = a.getPackageManager();
		PackageInfo packInfo;
		String version = "";
		try {
			packInfo = packageManager.getPackageInfo(a.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	private static Toast mToast;

	public static void showToast(Context context, String info) {
		showToast(context, info, Toast.LENGTH_SHORT);
	}

	public static void showToast(Context context, String info, int duration) {
		
		if(context == null){
			return;
		}
		
		if (mToast == null) {
			mToast = Toast.makeText(context, info, duration);
		}

		mToast.setText(info);
		mToast.show();
	}

	public static void showToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_SHORT);
	}

	public static void showToast(Context context, int resId, int duration) {
		
		if(context == null){
			return;
		}
		
		if (mToast == null) {
			mToast = Toast.makeText(context, resId, duration);
		}
		mToast.setText(resId);
		mToast.show();
	}

	public static String[] spliteStrBySymbol(String str, String symbol) {
		if (str == null || symbol == null) {
			return null;
		}

		String[] strArr = str.split("/");

		return strArr;
	}

	public static int getSumInList(List<Integer> numList) {
		if (numList == null) {
			return 0;
		}

		int sum = 0;

		for (Integer num : numList) {
			sum += num;
		}

		sum = Collections.max(numList);
		return sum;
	}

	public static boolean isContainsChinese(String str) {
		String regEx = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(regEx);
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find()) {
			flg = true;
		}
		return flg;
	}

	public static boolean hasSpecialSymbol(String str) {
		if (str == null) {
			return false;
		}

		String regEx = "^[a-zA-Z0-9_\u4e00-\u9fa5]+$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		boolean hasSymbol = !m.find();
		return hasSymbol;
	}

	public static int findIdInUrl(String url) {


		if (url == null) {
			return 0;
		}

		String[] arr = url.split("&id=");
		if (arr.length != 2) {
			return 0;
		}

		int id = 0;

		try {
			id = Integer.valueOf(arr[1]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			id = 0;
		}

		return id;
	}

	public static int[] getScreenWidthHeight(Context context) {

		int[] posArr = new int[2];

		if (context == null) {
			return posArr;
		}

		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		int width = mDisplayMetrics.widthPixels;
		int height = mDisplayMetrics.heightPixels;

		posArr[0] = width;
		posArr[1] = height;

		return posArr;
	}

	public static void copyStr2Clipboard(Context context, String str) {
		if (context == null || str == null) {
			return;
		}

		ClipboardManager cmb = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		cmb.setText(str);
	}


	public static Bitmap takeScreenShot(Activity activity) {
		// View是你�?要截图的View
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();

		// 获取状�?�栏高度
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		// 获取屏幕长和�?
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay()
				.getHeight();
		// 去掉标题�?
		// Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
		Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return b;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Bitmap fastblur(Context context, Bitmap sentBitmap, int radius) {

		if (Build.VERSION.SDK_INT > 16) {
			Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

			final RenderScript rs = RenderScript.create(context);
			final Allocation input = Allocation.createFromBitmap(rs,
					sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
					Allocation.USAGE_SCRIPT);
			final Allocation output = Allocation.createTyped(rs,
					input.getType());
			final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
					Element.U8_4(rs));
			script.setRadius(radius /* e.g. 3.f */);
			script.setInput(input);
			script.forEach(output);
			output.copyTo(bitmap);
			return bitmap;
		}

		// Stack Blur v1.0 from
		// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
		//
		// Java AuthorSearch: Mario Klingemann <mario at quasimondo.com>
		// http://incubator.quasimondo.com
		// created Feburary 29, 2004
		// Android port : Yahel Bouaziz <yahel at kayenko.com>
		// http://www.kayenko.com
		// ported april 5th, 2012

		// This is a compromise between Gaussian Blur and Box blur
		// It creates much better looking blurs than Box Blur, but is
		// 7x faster than my Gaussian Blur implementation.
		//
		// I called it Stack Blur because this describes best how this
		// filter works internally: it creates a kind of moving stack
		// of colors whilst scanning through the image. Thereby it
		// just has to add one new block of color to the right side
		// of the stack and remove the leftmost color. The remaining
		// colors on the topmost layer of the stack are either added on
		// or reduced by one, depending on if they are on the right or
		// on the left side of the stack.
		//
		// If you are using this algorithm in your code please add
		// the following line:
		//
		// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		if (radius < 1) {
			return (null);
		}

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int[] pix = new int[w * h];
		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.getPixels(pix, 0, w, 0, 0, w, h);

		int wm = w - 1;
		int hm = h - 1;
		int wh = w * h;
		int div = radius + radius + 1;

		int r[] = new int[wh];
		int g[] = new int[wh];
		int b[] = new int[wh];
		int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
		int vmin[] = new int[Math.max(w, h)];

		int divsum = (div + 1) >> 1;
		divsum *= divsum;
		int dv[] = new int[256 * divsum];
		for (i = 0; i < 256 * divsum; i++) {
			dv[i] = (i / divsum);
		}

		yw = yi = 0;

		int[][] stack = new int[div][3];
		int stackpointer;
		int stackstart;
		int[] sir;
		int rbs;
		int r1 = radius + 1;
		int routsum, goutsum, boutsum;
		int rinsum, ginsum, binsum;

		for (y = 0; y < h; y++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			for (i = -radius; i <= radius; i++) {
				p = pix[yi + Math.min(wm, Math.max(i, 0))];
				sir = stack[i + radius];
				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);
				rbs = r1 - Math.abs(i);
				rsum += sir[0] * rbs;
				gsum += sir[1] * rbs;
				bsum += sir[2] * rbs;
				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}
			}
			stackpointer = radius;

			for (x = 0; x < w; x++) {

				r[yi] = dv[rsum];
				g[yi] = dv[gsum];
				b[yi] = dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (y == 0) {
					vmin[x] = Math.min(x + radius + 1, wm);
				}
				p = pix[yw + vmin[x]];

				sir[0] = (p & 0xff0000) >> 16;
				sir[1] = (p & 0x00ff00) >> 8;
				sir[2] = (p & 0x0000ff);

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[(stackpointer) % div];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi++;
			}
			yw += w;
		}
		for (x = 0; x < w; x++) {
			rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
			yp = -radius * w;
			for (i = -radius; i <= radius; i++) {
				yi = Math.max(0, yp) + x;

				sir = stack[i + radius];

				sir[0] = r[yi];
				sir[1] = g[yi];
				sir[2] = b[yi];

				rbs = r1 - Math.abs(i);

				rsum += r[yi] * rbs;
				gsum += g[yi] * rbs;
				bsum += b[yi] * rbs;

				if (i > 0) {
					rinsum += sir[0];
					ginsum += sir[1];
					binsum += sir[2];
				} else {
					routsum += sir[0];
					goutsum += sir[1];
					boutsum += sir[2];
				}

				if (i < hm) {
					yp += w;
				}
			}
			yi = x;
			stackpointer = radius;
			for (y = 0; y < h; y++) {
				// Preserve alpha channel: ( 0xff000000 & pix[yi] )
				pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
						| (dv[gsum] << 8) | dv[bsum];

				rsum -= routsum;
				gsum -= goutsum;
				bsum -= boutsum;

				stackstart = stackpointer - radius + div;
				sir = stack[stackstart % div];

				routsum -= sir[0];
				goutsum -= sir[1];
				boutsum -= sir[2];

				if (x == 0) {
					vmin[y] = Math.min(y + r1, hm) * w;
				}
				p = x + vmin[y];

				sir[0] = r[p];
				sir[1] = g[p];
				sir[2] = b[p];

				rinsum += sir[0];
				ginsum += sir[1];
				binsum += sir[2];

				rsum += rinsum;
				gsum += ginsum;
				bsum += binsum;

				stackpointer = (stackpointer + 1) % div;
				sir = stack[stackpointer];

				routsum += sir[0];
				goutsum += sir[1];
				boutsum += sir[2];

				rinsum -= sir[0];
				ginsum -= sir[1];
				binsum -= sir[2];

				yi += w;
			}
		}

		Log.e("pix", w + " " + h + " " + pix.length);
		bitmap.setPixels(pix, 0, w, 0, 0, w, h);
		return (bitmap);
	}

	/**
	 * 获取版本�?
	 * */
	public static int getVersionName(Context a) {
		PackageManager packageManager = a.getPackageManager();
		PackageInfo packInfo;
		int version = 0;
		try {
			packInfo = packageManager.getPackageInfo(a.getPackageName(), 0);
			version = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 签名生成算法
	 * 
	 * @param HashMap
	 *            <String,String> params 请求参数集，�?有参数必须已转换为字符串类型
	 * @param String
	 *            secret 签名密钥
	 * @return 签名
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String, String> params,
			String secret) {
		// 先将参数以其参数名的字典序升序进行排�?
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将�?有参数按"key=value"格式拼接在一�?
		StringBuilder basestring = new StringBuilder();
		for (Entry<String, String> param : entrys) {
			basestring.append(param.getKey()).append("=")
					.append(param.getValue());
		}
		basestring.append(secret);

		DebugTools.d("post2 md5: " + basestring.toString());

		// 使用MD5对待签名串求�?
		byte[] bytes = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// 将MD5输出的二进制结果转换为小写的十六进制
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}

	public static String getSignature(String value){
		// 使用MD5对待签名串求�?
		byte[] bytes = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(value.getBytes("UTF-8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// 将MD5输出的二进制结果转换为小写的十六进制
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}

	public static final String MD5_SECRET = "fk4iy@98(*Y98fh-^o)re+wg=";
	public static String getSignature(HashMap<String, String> params) {
		String secret = MD5_SECRET;
		return getSignature(params, secret);
	}

	public static String convertMap2Params(HashMap<String, String> params) {
		// 先将参数以其参数名的字典序升序进行排�?
		Map<String, String> sortedParams = new TreeMap<String, String>(params);
		Set<Entry<String, String>> entrys = sortedParams.entrySet();

		// 遍历排序后的字典，将�?有参数按"key=value"格式拼接在一�?
		StringBuilder basestring = new StringBuilder();
		int i = 0;
		for (Entry<String, String> param : entrys) {
			if (i == 0) {
				basestring.append(param.getKey()).append("=")
						.append(param.getValue());
			} else {
				basestring.append("&" + param.getKey()).append("=")
						.append(param.getValue());
			}

			i = 1;
		}
		return basestring.toString();
	}
	
	public static String convertEncodeByUtf8(String value){
		if(TextUtils.isEmpty(value)){
			return value;
		}
		
		try{
			value = URLEncoder.encode(value, "UTF-8");
		}catch (UnsupportedEncodingException ex){
			ex.printStackTrace();
		}
	    
	    return value;
	}
	
	// 手机型号
	public static String getDeviceModel() {
		return Build.MANUFACTURER + "_" + Build.MODEL;
	}
	
	
	public static int getStatusbarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认�?38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }
	
	public static int getNavigationBarHeight(Context context) {  
        Resources resources = context.getResources();  
        int resourceId = resources.getIdentifier("navigation_bar_height",  
                "dimen", "android");  
        //获取NavigationBar的高�?  
        int height = resources.getDimensionPixelSize(resourceId);  
        return height;  
    }  
	
    public static boolean checkDeviceHasNavigationBar(Context context) {  
    	  
        //通过判断设备是否有返回键、菜单键(不是虚拟�?,是手机屏幕外的按�?)来确定是否有navigation bar  
        boolean hasMenuKey = ViewConfiguration.get(context)  
                .hasPermanentMenuKey();  
        boolean hasBackKey = KeyCharacterMap  
                .deviceHasKey(KeyEvent.KEYCODE_BACK);  
  
        if (!hasMenuKey && !hasBackKey) {  
            // 做任何你�?要做�?,这个设备有一个导航栏  
            return true;  
        }  
        return false;  
    } 
    
//    public static boolean checkDeviceHasNavigationBar(Context context) {
//        boolean hasNavigationBar = false;
//        Resources rs = context.getResources();
//        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
//        if (id > 0) {
//            hasNavigationBar = rs.getBoolean(id);
//            DebugUtils.d("ha2 id hasNavigationBar: " + hasNavigationBar);
//        }
//        try {
//            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
//            Method m = systemPropertiesClass.getMethod("get", String.class);
//            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
//            if ("1".equals(navBarOverride)) {
//                hasNavigationBar = false;
//            } else if ("0".equals(navBarOverride)) {
//                hasNavigationBar = true;
//            }
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
//
//        return hasNavigationBar;
//    }
    
    public static void pauseMusic(Context context) {  
        Intent freshIntent = new Intent();  
        freshIntent.setAction("com.android.music.musicservicecommand.pause");  
        freshIntent.putExtra("command", "pause");  
        context.sendBroadcast(freshIntent); 
        DebugTools.d("music2 send broadcase pauseMusic");
//        sendMediaButton(context.getApplicationContext(), KeyEvent.KEYCODE_MEDIA_PAUSE);
    }  
    
    private static void sendMediaButton(Context context, int keyCode) {
        KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        context.sendOrderedBroadcast(intent, null);

        keyEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
        intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
		context.sendOrderedBroadcast(intent, null);
	}

	public static boolean isFileExist(String path) {
		if (path == null) {
			return false;
		}

		File file = new File(path);

		return file.exists();
	}

	public static int dip2px(Context context, float dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		DebugTools.d("scale2 scale: " + scale);
		return (int) (dip * scale + 0.5f);
	}

  public static int randInSection(int start, int end){
    	if(start >= end){
    		return start;
    	}
    	
    	double base = Math.random();
    	
    	base = base * (end - start) + start;
    	
    	return (int)Math.floor(base);
    }

	public static int convertStr2Int(String value) {
		int result = 0;
		try{
			result = Integer.valueOf(value);
		}catch (NumberFormatException ex){
			ex.printStackTrace();
		}

		return result;
	}

	public static int dpToPx(final Context context, final float dp) {
		// Took from http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
		if(context == null){
			return 0;
		}
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) ((dp * scale) + 0.5f);
	}

	public static String convertStr2Utf8(String value){
		try{
			value = URLEncoder.encode(value, "UTF-8");
		}catch (UnsupportedEncodingException ex){
			ex.printStackTrace();
		}

		return value;
	}

	/**
	 * 关闭键盘事件
	 */
	public static void closeInput(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null && activity.getCurrentFocus() != null) {
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),0);
		}
	}
	public static void showInput(EditText editText){
		InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void showInputForce(final Activity c,int delay){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				((InputMethodManager) c.getSystemService(c.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, delay);
	}

	public static void showMapData(Map<String, String> map) {
		DebugTools.d("map2 map null: " + (map == null));
		if (map == null) {
			return;
		}
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key);

			DebugTools.d("map2 key: " + key + " value: " + value);
		}
	}
	
	public static void showMapData(Map<String, String> map, String tag) {
		DebugTools.d("map2 map null: " + (map == null));
		if (map == null) {
			return;
		}
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = map.get(key);

			DebugTools.d(tag + " key: " + key + " value: " + value);
		}
	}
	
	public static void showIntegerMapData(Map<Integer, Integer> map, String tag) {
		DebugTools.d("map2 map null: " + (map == null));
		if (map == null) {
			return;
		}
		Iterator<Integer> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			Integer key = iter.next();
			Integer value = map.get(key);

			DebugTools.d(tag + " key: " + key + " value: " + value);
		}
	}
	
	
	public static int getRandInt(int start, int end){
		int delta = end - start;
		if(delta <= 0){
			return end;
		}
		
		double random = Math.random();
		
		double value = random * (delta) + start;
		Long result = Math.round(value - 0.5);
		return result.intValue();
	}
	
	public static int getViewRealTop(View v){
		return getViewRealTopOrBottom(v, true);
	}
	
	public static int getViewRealBottom(View v){
		return getViewRealTopOrBottom(v, false);
	}
	
	private static Rect rect = new Rect();
	private static Point point = new Point();
	
	public static int getViewRealTopOrBottom(View v, boolean isTop){
		if(v == null){
			return 0;
		}
		
		v.getGlobalVisibleRect(rect, point);
		
		if(isTop){
			return rect.top;
		}else{
			return rect.bottom;
		}
	}
	
	
	public static String getBirthdayFormatTime(long time) {
//		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		
		
//		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
//		String result = format.format(date);
//		String result = format.format(time);
//		return date.getYear() + "." + date.getMonth() + "." + date.getDay();
		return cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static boolean isImageUrlValid(String url){
		if(TextUtils.isEmpty(url)){
			return false;
		}
		
		return true;
	}
	
	public static String getPrefixUrl(String url){
        if(TextUtils.isEmpty(url)){
            return url;
        }

        String[] urlArr = url.split("\\?");

        if(urlArr.length != 2 || TextUtils.isEmpty(urlArr[1])){
            return url;
        }

        StringBuffer buffer = new StringBuffer();
        
        String[] paramsArr = urlArr[1].split("&");

        for(String keyValue: paramsArr){
            if(TextUtils.isEmpty(keyValue)){
                continue;
            }

            String[] keyValueArr = keyValue.split("=");
            if(keyValueArr.length != 2){
                continue;
            }

            String key = keyValueArr[0];
            String value = keyValueArr[1];

            if("m".equals(key) || "c".equals(key) || "a".equals(key) 
            		|| "client".equals(key) || "device_id".equals(key) || "version".equals(key) || "create_time".equals(key) || "page_id".equals(key)){
            	if(!TextUtils.isEmpty(buffer)){
            	   	buffer.append("&");
            	}
               	buffer.append(key + "=" + value);
                continue;
            }
        }
        
        return urlArr[0] + "?" + buffer.toString();
	}
	
	public static HashMap<String, String> getMapFromUrlWithMd5(String url){
        HashMap<String, String> map = new HashMap<String, String>();
        if(TextUtils.isEmpty(url)){
            return map;
        }

        String[] urlArr = url.split("\\?");

        if(urlArr.length != 2 || TextUtils.isEmpty(urlArr[1])){
            return map;
        }

        map = getMapFromUrl(url);
        HashMap<String, String> md5Map = getMd5Map(url);
        
        map.put("sign", getSignature(md5Map));
        
        return map;
	}
	
	public static String convertUrl2Md5Url(String url){
		HashMap<String, String> md5Map = getMd5Map(url);
		
		return url + "&sign=" + getSignature(md5Map);
	}
	
	private static HashMap<String, String> getMd5Map(String url){
	    HashMap<String, String> md5Map = new HashMap<String, String>();
        if(TextUtils.isEmpty(url)){
            return md5Map;
        }

        String[] urlArr = url.split("\\?");

        if(urlArr.length != 2 || TextUtils.isEmpty(urlArr[1])){
            return md5Map;
        }

        String[] paramsArr = urlArr[1].split("&");
 
        
        for(String keyValue: paramsArr){
            if(TextUtils.isEmpty(keyValue)){
                continue;
            }

            String[] keyValueArr = keyValue.split("=");
            if(keyValueArr.length != 2){
                continue;
            }

            String key = keyValueArr[0];
            String value = keyValueArr[1];
            
            if("a".equals(key)){
            	md5Map.put("apiname", value);
            }
            
            if("uid".equals(key) || "time".equals(key) || "device_id".equals(key) ||
            		"post_id".equals(key)){
            	md5Map.put(key, value);
            }

        }
        return md5Map;
	}
	
	
	public static HashMap<String, String> getMapFromUrl(String url){
        HashMap<String, String> map = new HashMap<String, String>();
        if(TextUtils.isEmpty(url)){
            return map;
        }

        String[] urlArr = url.split("\\?");

        if(urlArr.length != 2 || TextUtils.isEmpty(urlArr[1])){
            return map;
        }
        String[] paramsArr = urlArr[1].split("&");
        
        for(String keyValue: paramsArr){
            if(TextUtils.isEmpty(keyValue)){
                continue;
            }

            String[] keyValueArr = keyValue.split("=");
            if(keyValueArr.length != 2){
                continue;
            }

            String key = keyValueArr[0];
            String value = keyValueArr[1];
            
            if("c".equals(key) || "a".equals(key) || "m".equals(key)){
                continue;
            }

            map.put(key, value);
        }
        
        return map;
	}
	
	private static final String UMENG_CHANNEL = "UMENG_CHANNEL";
	
	/**
	 * 获取渠道名
	 * 
	 * @param ctx
	 *            此处习惯性的设置为activity，实际上context就可以
	 * @return 如果没有获取成功，那么返回值为空
	 */
	public static String getChannelName(Context ctx) {
		if (ctx == null) {
			return null;
		}
		String channelName = null;
		try {
			PackageManager packageManager = ctx.getPackageManager();
			if (packageManager != null) {
				// 注意此处为ApplicationInfo 而不是
				// ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
				ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(),
								PackageManager.GET_META_DATA);
				DebugTools.d("ana2 applicationInfo null: " + (applicationInfo == null));
				if (applicationInfo != null) {
					DebugTools.d("ana2 applicationInfo.metaData null: " + (applicationInfo.metaData == null));
					if (applicationInfo.metaData != null) {
						channelName = applicationInfo.metaData.getString(UMENG_CHANNEL);
						
						if(TextUtils.isEmpty(channelName)){
							channelName = applicationInfo.metaData.getInt(UMENG_CHANNEL) + "";
						}
						
						DebugTools.d("ana2 getChannelName channelName: " + channelName);
//						showBundle(applicationInfo.metaData);
					}
				}

			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			DebugTools.d("ana2 exception: " + e.toString());
		}
		return channelName;
	}
	
	private static void showBundle(Bundle bundle) {
		Set<String> keySet = bundle.keySet();
		for (String key : keySet) {
			Object value = bundle.get(key);
//			String value2 = bundle.getString(key);
			DebugTools.d("ana2 key: " + key + " value: " + value + " value: " + (value instanceof String));
		}
	}
	
 
	/**
	 * 通过浏览器打开链接
	 * */	
	public static void openInBrowse(Activity a,String url){
		Intent intent = new Intent("android.intent.action.VIEW");        
		intent.setData(Uri.parse(url));  
		a.startActivity(intent);
	}
	
	public static String getFileNameFromUrl(String url){
		if(url == null){
			return null;
		}
		
		String dot = "\\u002E";
		
		int index = url.lastIndexOf("/");
		String name = url.substring(index + 1);
//		
//		String[] nameArr = name.split(dot);
//		
//		if(nameArr != null && nameArr.length > 1){
//			return nameArr[0];
//		}
		
		return name;
	}
	
	public static List<String> convertJSONArrayToStrList(JSONArray jsonArr) throws JSONException{
		List<String> list = new ArrayList<String>();
		if(jsonArr == null){
			return list;
		}
		
		int size = jsonArr.length();
		for(int i = 0; i < size; i++){
			list.add(jsonArr.getString(i));
		}
		
		return list;
	}
	
	  public static Bitmap getImageFromAssetsFile(Context context, String fileName) { 
		  if(context == null){
			  return null;
		  }
		  
	      Bitmap image = null;  
	      AssetManager am = context.getResources().getAssets();  
	      try{  
	          InputStream is = am.open(fileName);  
	          image = BitmapFactory.decodeStream(is);  
	          is.close();  
	      }  
	      catch (IOException e)  {  
	          e.printStackTrace();  
	      }  
	  
	      return image;  
	  } 
	  
	    
	  public static String getMediaSavePathByUrl(String url){
	      String name = CommonTools.getFileNameFromUrl(url);
	      return Constants.PATH_MEDIA + "/" + name;
	  }
}
