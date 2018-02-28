package com.vlee78.android.vl;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.qingxin.medical.base.QingXinApplication;
import com.qingxin.medical.fresco.zoomable.DoubleTapGestureListener;
import com.qingxin.medical.fresco.zoomable.ZoomableDraweeView;
import com.vlee78.android.vl.VLAsyncHandler.VLAsyncRes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("UseSparseArrays")
public final class VLUtils {
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;

    public static void setControllerListener(@NonNull final ZoomableDraweeView simpleDraweeView, @NonNull Uri imagePath) {
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        final int imageWidth = getScreenWidth(QingXinApplication.getInstance());
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable anim) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                simpleDraweeView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }
        };
        simpleDraweeView.setAllowTouchInterceptionWhileZoomed(true);
        simpleDraweeView.setIsLongpressEnabled(false);
        simpleDraweeView.setTapListener(new DoubleTapGestureListener(simpleDraweeView));
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(imagePath).build();
        simpleDraweeView.setController(controller);
    }


    public static int getPageCount(int count, int pageSize) {
        return (count - 1) / pageSize + 1;
    }

    public static int pageSize(int count, int pageSize, int pageIndex) {
        int pageCount = getPageCount(count, pageSize);
        if (pageIndex < 0 || pageIndex >= pageCount) return 0;
        if (pageIndex < pageCount - 1) return pageSize;
        int mode = count % pageSize;
        return (mode == 0 ? pageSize : mode);
    }

    public static String sizeBKMGDesc(long size) {
        String res = "";
        long kb = 1024;
        long mb = 1024 * 1024;
        long gb = 1024 * 1024 * 1024;
        if (size >= gb) {
            float f = (float) size / gb;
            res = String.format(Locale.getDefault(), "%.1f GB", f, Locale.getDefault());
        } else if (size >= mb) {
            float f = (float) size / mb;
            res = String.format(Locale.getDefault(), f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            res = String.format(Locale.getDefault(), f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else res = String.format(Locale.getDefault(), "%d B", size);
        return res;
    }

    /**
     * 设置字体颜色大小的span
     */
    public static SpannableString androidSizeSpan(String text, int textColor, int backColor, int size) {
        SpannableString span = new SpannableString(text);
        int len = 0;
        if (!TextUtils.isEmpty(text)) {
            len = text.length();
        }
        if (textColor != 0)
            span.setSpan(new ForegroundColorSpan(textColor), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (backColor != 0)
            span.setSpan(new BackgroundColorSpan(backColor), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (size != 0)
            span.setSpan(new AbsoluteSizeSpan(size), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public static SpannableString androidClickSpan(String text, final int textColor, int backColor, final Object tag, final View.OnClickListener listener) {
        SpannableString span = new SpannableString(text);
        int len = text.length();
        if (textColor != 0)
            span.setSpan(new ForegroundColorSpan(textColor), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (backColor != 0)
            span.setSpan(new BackgroundColorSpan(backColor), 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (listener != null) {
            span.setSpan(new ClickableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(textColor);
                }

                @Override
                public void onClick(View view) {
                    view.setTag(tag);
                    listener.onClick(view);
                }
            }, 0, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return span;
    }

    public static String androidLineNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return V(tm.getLine1Number());
    }

    public static String androidIMEI(Context app) {
        String imei = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (imei == null)
            imei = "";
        return imei;
    }

    public static String androidPseudoUniqId() {
        String devIDShort = "35"; //we make this look like a valid IMEI
        int uniqNum = Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return devIDShort + uniqNum;
    }

    public static String getMacAddress(Context app) {
        try {
            WifiManager wm = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
            return wm.getConnectionInfo().getMacAddress();
        } catch (Exception e) {
            return "NotFoundMacAddress";
        }
    }

    public static String getBluetoothAddress(Context app) {
        try {
            BluetoothAdapter m_BluetoothAdapter; // Local Bluetooth adapter
            m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            return m_BluetoothAdapter.getAddress();
        } catch (Exception e) {
            return "NotFoundBluetoothAddress";
        }
    }

    public static String androidId(Context app) {
        try {
            return Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return "NotFoundAndroidId";
        }
    }

    public static String getCombinedDeviceId(Context app) {
        String imei = androidIMEI(app);
        if (!TextUtils.isEmpty(imei)) return imei;
        String devIDShort = androidPseudoUniqId();
        String androidId = androidId(app);
        String mac = getMacAddress(app);
        String bmac = getBluetoothAddress(app);
        String combDeviceId = imei + devIDShort + androidId + mac + bmac;
        combDeviceId = stringMd5(combDeviceId);
        return combDeviceId;
    }

    public static String androidIMSI(Context app) {
        String imsi = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (imsi == null)
            imsi = "";
        return imsi;
    }

    public static String androidTelInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ("DeviceId(IMEI) = " + tm.getDeviceId() + "\n") +
                "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n" +
                "Line1Number = " + tm.getLine1Number() + "\n" +
                "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n" +
                "NetworkOperator = " + tm.getNetworkOperator() + "\n" +
                "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n" +
                "NetworkType = " + tm.getNetworkType() + "\n" +
                "PhoneType = " + tm.getPhoneType() + "\n" +
                "SimCountryIso = " + tm.getSimCountryIso() + "\n" +
                "SimOperator = " + tm.getSimOperator() + "\n" +
                "SimOperatorName = " + tm.getSimOperatorName() + "\n" +
                "SimSerialNumber = " + tm.getSimSerialNumber() + "\n" +
                "SimState = " + tm.getSimState() + "\n" +
                "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n" +
                "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
    }

    public static final int NETOP_UNKNOWN = 0;
    public static final int NETOP_CHINAMOBILE = 1;
    public static final int NETOP_CHINAUNICOM = 2;
    public static final int NETOP_CHINATELECOM = 3;

    public static int androidNetOp(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String netOp = tm.getNetworkOperator();
        if (netOp != null && (netOp.equals("46000") || netOp.startsWith("46002")))
            return NETOP_CHINAMOBILE;
        else if (netOp != null && netOp.startsWith("46001"))
            return NETOP_CHINAUNICOM;
        else if (netOp != null && netOp.startsWith("46003"))
            return NETOP_CHINATELECOM;
        else
            return NETOP_UNKNOWN;
    }

    @SuppressLint("NewApi")
    public static String androidBuildInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("android.os.Build.BRAND = ").append(Build.BRAND).append("\n");
        sb.append("android.os.Build.MANUFACTURER = ").append(Build.MANUFACTURER).append("\n");
        sb.append("android.os.Build.MODEL = ").append(Build.MODEL).append("\n");
        sb.append("android.os.Build.FINGERPRINT = ").append(Build.FINGERPRINT).append("\n");
        sb.append("android.os.Build.VERSION.RELEASE = ").append(Build.VERSION.RELEASE).append("\n");
        sb.append("android.os.Build.VERSION.CODENAME = ").append(Build.VERSION.CODENAME).append("\n");
        sb.append("android.os.Build.VERSION.INCREMENTAL = ").append(Build.VERSION.INCREMENTAL).append("\n");
        sb.append("android.os.Build.VERSION.SDK_INT = ").append(Build.VERSION.SDK_INT).append("\n");
        sb.append("android.os.Build.BOARD = ").append(Build.BOARD).append("\n");
        sb.append("android.os.Build.BOOTLOADER = ").append(Build.BOOTLOADER).append("\n");
        sb.append("android.os.Build.DEVICE = ").append(Build.DEVICE).append("\n");
        sb.append("android.os.Build.DISPLAY = ").append(Build.DISPLAY).append("\n");
        sb.append("android.os.Build.HARDWARE = ").append(Build.HARDWARE).append("\n");
        sb.append("android.os.Build.HOST = ").append(Build.HOST).append("\n");
        sb.append("android.os.Build.ID = ").append(Build.ID).append("\n");
        sb.append("android.os.Build.PRODUCT = ").append(Build.PRODUCT).append("\n");
        if (Build.VERSION.SDK_INT >= 9)
            sb.append("android.os.Build.SERIAL = " + Build.SERIAL + "\n");
        sb.append("android.os.Build.TAGS = " + Build.TAGS + "\n");
        sb.append("android.os.Build.TIME = " + Build.TIME + "\n");
        sb.append("android.os.Build.TYPE = " + Build.TYPE + "\n");
        sb.append("android.os.Build.USER = " + Build.USER + "\n");
        if (Build.VERSION.SDK_INT >= 14)
            sb.append("android.os.Build.getRadioVersion() = " + Build.getRadioVersion() + "\n");
        return sb.toString();
    }

    public static final boolean stringCompare(String str1, String str2) {
        return str1 == null && str2 == null || !(str1 == null || str2 == null) && str1.equals(str2);
    }

    public static String stringNullDefault(String str, String def) {
        return (str == null ? def : str);
    }

    public static boolean stringIsEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    public static boolean stringIsNotEmpty(String str) {
        return (str != null && str.length() > 0);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() < 1;
    }

    public static void httpGetUrl(final String url, final VLAsyncHandler<byte[]> asyncHandler) {
        VLScheduler.instance.schedule(0, VLScheduler.THREAD_BG_NORMAL, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                HttpURLConnection conn = null;
                InputStream is = null;
                try {
                    conn = (HttpURLConnection) (new URL(url)).openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int len = is.read(buffer);
                        if (len < 0)
                            break;
                        baos.write(buffer, 0, len);
                    }
                    byte[] bytes = baos.toByteArray();
                    if (asyncHandler != null)
                        asyncHandler.handlerSuccess(bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (asyncHandler != null)
                        asyncHandler.handlerError(VLAsyncRes.VLAsyncResFailed, e.getMessage());
                } finally {
                    if (is != null)
                        try {
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    if (conn != null)
                        conn.disconnect();
                }
            }
        });
    }

    public static void E(String msg) throws Exception {
        throw new Exception(msg);
    }

    public static void RE(String msg) {
        throw new RuntimeException(msg);
    }

    private static View.OnTouchListener gFilterTouchListener = null;

    public static void viewFilterTouch(View view) {
        if (gFilterTouchListener == null) {
            gFilterTouchListener = new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            };
        }
        view.setOnTouchListener(gFilterTouchListener);
    }

    public static int[] viewMeasureSize(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    public static View viewCreate(Context context, int width, int height, int bgcolor) {
        View view = new View(context);
        view.setBackgroundColor(bgcolor);
        view.setLayoutParams(VLUtils.paramsGroup(width, height));
        return view;
    }

    public static <T extends Serializable> String objectToHexString(T object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return VLUtils.bytesToHexString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T extends Serializable> T hexStringToObject(String hexString) {
        try {
            byte[] bytes = VLUtils.hexStringToBytes(hexString);
            if (bytes == null)
                return null;
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            T object = (T) ois.readObject();
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public static final String dumpBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder("{");
        for (String key : bundle.keySet()) {
            if (sb.length() > 1)
                sb.append(',');
            sb.append(key).append('=').append(bundle.get(key).toString());
        }
        sb.append('}');
        return sb.toString();
    }

    public static final DisplayMetrics androidDisplayMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static final RunningAppProcessInfo androidProcessInfo(Context context, int pid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appInfos = am.getRunningAppProcesses();
        for (RunningAppProcessInfo appInfo : appInfos) {
            if (appInfo.pid == pid)
                return appInfo;
        }
        return null;
    }

    /**
     * 是否后台运行
     *
     * @param context
     * @return
     */
    public static boolean isBackgroundRun(Context context) {//  home或者正常退出其实都再运行
        // 返回true
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (am == null)
            return false;

        List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
        if (runningTaskInfos == null || runningTaskInfos.size() == 0)
            return false;

        String topAppName;
        topAppName = runningTaskInfos.get(0).topActivity.getPackageName();
        return !topAppName.contains(context.getPackageName());

    }

    public static final ApplicationInfo androidApplicationInfo(Context context, int flag) {
        try {
            String packageName = context.getPackageName();
            return context.getPackageManager().getApplicationInfo(packageName, flag);
        } catch (Exception e) {
            return null;
        }
    }

    public static final boolean androidDail(Context context, String phoneNum) {
        if (!androidCheckUsePermission(context, new String[]{permission.CALL_PHONE}))
            return false;
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
        return true;
    }

    public static void gotoDail(@NonNull Context context, String phoneNum) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != telephonyManager && telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel:%s", phoneNum)));
            context.startActivity(intent);
        }
    }

    public static final String androidExternalHome(Context context) {
        File desDir = context.getExternalFilesDir(null);
        if (desDir == null)
            desDir = context.getFilesDir();
        if (desDir == null)
            return null;
        desDir = desDir.getParentFile();
        if (desDir == null)
            return null;
        return desDir.getAbsolutePath();
    }

    public static PackageInfo androidPackageInfo(Context context, int flags) {
        return androidPackageInfo(context, context.getPackageName(), flags);
    }

    public static PackageInfo androidPackageInfo(Context context, String packageName, int flags) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static final boolean androidCheckUsePermission(Context context, String[] permissions) {
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;
            for (String permission : permissions) {
                boolean ok = false;
                for (String requestedPermission : requestedPermissions) {
                    if (permission.equals(requestedPermission)) {
                        ok = true;
                        break;
                    }
                }
                if (!ok)
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static final LinearLayout.LayoutParams paramsLinear(int width, int height) {
        return paramsLinear(width, height, 0);
    }

    public static RelativeLayout.LayoutParams paramsRelative(int width, int height) {
        return new RelativeLayout.LayoutParams(width, height);
    }

    public static RelativeLayout.LayoutParams paramsRelative(int width, int height, int verb) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(verb);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        return params;
    }

    public static final RelativeLayout.LayoutParams paramsRelative(int width, int height, int verb0, int verb1) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(verb0);
        params.addRule(verb1);
        return params;
    }

    public static final RelativeLayout.LayoutParams paramsRelativeAnchor(int width, int height, int[][] anchorVerbs) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        for (int[] anchorVerb : anchorVerbs) {
            int anchor = anchorVerb[0];
            for (int i = 1; i < anchorVerb.length; i++)
                params.addRule(anchorVerb[i], anchor);
        }
        return params;
    }

    public static final LinearLayout.LayoutParams paramsLinear(int width, int height, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.weight = weight;
        return params;
    }

    public static final LinearLayout.LayoutParams paramsLinear(int width, int height, float weight, int gravity) {
        LinearLayout.LayoutParams params = paramsLinear(width, height, weight);
        params.gravity = gravity;
        return params;
    }

    public static final FrameLayout.LayoutParams paramsFrame(int width, int height, int x, int y) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.leftMargin = x;
        params.topMargin = y;
        return params;
    }

    public static final ViewGroup.LayoutParams paramsGroup(int width, int height) {
        return new ViewGroup.LayoutParams(width, height);
    }

    public static final FrameLayout.LayoutParams paramsFrame(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    public static final FrameLayout.LayoutParams paramsFrame(int width, int height, int gravity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.gravity = gravity;
        return params;
    }

    public static final void notificationSend(Context context, int id, int iconRes, String title, boolean autoCancel, boolean sound, String text, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(iconRes);
        builder.setContentTitle(title);
        builder.setAutoCancel(autoCancel);
        builder.setContentText(text);
        if (sound)
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        if (intent != null) {
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pi);
        }
        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, notification);
    }

    public static final void notificationSend(Context context, int id, int iconRes, String title, boolean autoCancel, boolean sound, int total, int progress, boolean indetermined, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(iconRes);
        builder.setContentTitle(title);
        builder.setAutoCancel(autoCancel);
        builder.setProgress(total, progress, indetermined);
        if (sound)
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        if (intent != null) {
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentIntent(pi);
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, builder.build());
    }

    public static final void notificationCancel(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    public static final void promptSms(Context context, String receiver, String content) {
        Uri uri = Uri.parse("smsto:" + receiver);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    public static final void promptInternalBrowser(Context context, String url) {
        VLBrowserActivity.startActivity(context, url);
    }

    public static final void promptExternalBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    public static final boolean stringValidatePhoneNumber(String value) {
        if (value == null || value.length() != 11)
            return false;
        if (value.charAt(0) != '1')
            return false;
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if ("0123456789".indexOf(ch) < 0)
                return false;
        }
        return true;
    }

    public static final String V(String str) {
        if (str == null)
            return "";
        return str;
    }

    public static <T> T cast(Object obj, Class<T> type) {
        if (!type.isInstance(obj)) {
            VLDebug.logE("cast failed");
            return null;
        }
        return type.cast(obj);
    }

    public static <T> T classNew(String className) {
        try {
            Class<?> cls = Class.forName(className);
            @SuppressWarnings("unchecked")
            T t = (T) cls.newInstance();
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T classNew(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    public static final boolean threadSleep(int sleepInMs) {
        try {
            Thread.sleep(sleepInMs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean threadJoin(Thread thread) {
        try {
            thread.join();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static final boolean threadInMain() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static final String stringLastToken(String string, char divider) {
        if (string == null)
            return null;
        int index = string.lastIndexOf(divider);
        return (index < 0 ? string : string.substring(index + 1));
    }

    public static final String stringUrlEncode(String string, String encoding) {
        try {
            return URLEncoder.encode(string, encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String stringUrlDecode(String string, String encoding) {
        try {
            return URLDecoder.decode(string, encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String[] stringListToArray(List<String> list) {
        if (list == null)
            return null;
        String[] res = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            res[i] = list.get(i);
        return res;
    }

    public static final List<String> stringArrayToList(String[] array) {
        if (array == null)
            return null;
        List<String> res = new ArrayList<>();
        Collections.addAll(res, array);
        return res;
    }

    public static final String[] stringArrayDecode(String string, char seperator) {
        List<String> list = stringListDecode(string, seperator);
        return stringListToArray(list);
    }

    public static final String stringArrayEncode(String[] strings, char seperator) {
        if (strings == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0)
                sb.append(seperator);
            sb.append(strings[i]);
        }
        return sb.toString();
    }

    public static final String stringListEncode(List<String> strings, char seperator) {
        if (strings == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            if (i > 0)
                sb.append(seperator);
            sb.append(strings.get(i));
        }
        return sb.toString();
    }

    public static final ArrayList<String> stringListDecode(String string, char seperator) {
        if (string == null)
            return null;
        ArrayList<String> list = new ArrayList<>();
        int head = 0;
        while (true) {
            int tail = string.indexOf(seperator, head);
            if (tail < 0) {
                String item = string.substring(head);
                list.add(item);
                break;
            }
            String item = string.substring(head, tail);
            list.add(item);
            head = tail + 1;
        }
        return list;
    }

    public static final String stringUnicodeEncode(String String) {
        char aChar;
        int len = String.length();
        StringBuilder outBuffer = new StringBuilder(len);

        for (int x = 0; x < len; ) {
            aChar = String.charAt(x++);
            if (aChar == '\\') {
                aChar = String.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;

                    for (int i = 0; i < 4; i++) {
                        aChar = String.charAt(x++);

                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';

                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }

        return outBuffer.toString();
    }

    public static String stringUnicodeEncode2(String theString) {

        char aChar;
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '%') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;

                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);

                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    else
                        outBuffer.append('%');

                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }

        return outBuffer.toString();
    }

    public static final String stringUnicodeDecode(String String) {
        char aChar;
        int len = String.length();
        StringBuilder outBuffer = new StringBuilder(len);

        for (int x = 0; x < len; ) {
            aChar = String.charAt(x++);
            if (aChar == '%') {
                aChar = String.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;

                    for (int i = 0; i < 4; i++) {
                        aChar = String.charAt(x++);

                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';

                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }

        return outBuffer.toString();
    }

    public static final float density() {
        return VLApplication.instance().getResources().getDisplayMetrics().density;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getAppInScreenheight(Context context) {
        return getScreenHeight(context) - getStatusBarHeight(context);
    }

    public static final int dip2px(float dipValue) {
        final float scale = VLApplication.instance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static final int px2dip(float pxValue) {
        final float scale = VLApplication.instance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static final int random(int maxExclude, long seed) {
        Random rand = new Random(seed);
        return rand.nextInt(maxExclude);
    }

    public static final String randomText(int minLength, int maxLength, String tokens) {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random(System.currentTimeMillis());
        int len = minLength + rand.nextInt(maxLength - minLength);
        int tokenLen = tokens.length();
        for (int i = 0; i < len; i++) {
            char ch = tokens.charAt(rand.nextInt(tokenLen));
            sb.append(ch);
        }
        return sb.toString();
    }

    public static final int randomInt(int[] source, int[] weights) {
        int total = 0;
        for (int weight : weights)
            total += weight;
        int rand = random(total, System.currentTimeMillis());
        int acc = 0;
        for (int i = 0; i < weights.length; i++) {
            acc += weights[i];
            if (acc > rand)
                return source[i];
        }
        return source[weights.length - 1];
    }

    public static final String getDateString1(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return "" + year + (month < 10 ? "0" + month : month) + (day < 10 ? "0" + day : day)
                + (hour < 10 ? "0" + hour : hour) + (min < 10 ? "0" + min : min) + (sec < 10 ? "0" + sec : sec);
    }

    public static boolean externalStorageExist() {
        return Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED);
    }

    public static String getIMSI(Context app) {
        String imsi = ((TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (imsi == null)
            imsi = "";
        return imsi;
    }

    public static boolean copyDb2SdCard(Application application, String externalHomeDir) {
        if (!externalStorageExist())
            return false;
        try {
            File desDir = new File(externalHomeDir + "/databases_" + System.currentTimeMillis());
            if (!desDir.exists())
                if (!desDir.mkdirs())
                    return false;

            File databaseDir = new File(application.getFilesDir().getAbsolutePath().replace("files", "databases"));
            if (databaseDir.exists() && databaseDir.isDirectory()) {
                for (File srcFile : databaseDir.listFiles()) {
                    if (srcFile.isDirectory())
                        continue;
                    File desFile = new File(desDir.getAbsoluteFile() + "/" + srcFile.getName());
                    if (!desFile.createNewFile())
                        return false;
                    copyFileByChannel(srcFile, desFile);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long copyFileByChannel(File f1, File f2) throws Exception {
        long time = new Date().getTime();
        int length = 128000;
        FileInputStream in = new FileInputStream(f1);
        FileOutputStream out = new FileOutputStream(f2);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        ByteBuffer b = null;
        while (true) {
            if (inC.position() == inC.size()) {
                in.close();
                inC.close();
                out.close();
                outC.close();
                return new Date().getTime() - time;
            }
            if ((inC.size() - inC.position()) < length) {
                length = (int) (inC.size() - inC.position());
            } else
                length = 128000;
            b = ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }

    public static final String stringMd5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            byte b[] = md.digest();

            StringBuilder buf = new StringBuilder("");
            int i;
            for (byte aB : b) {
                i = aB;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String bytesMd5(byte[] data) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(data);
            byte[] result = mDigest.digest();
            return bytesToHexString(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            int val = b & 0xff;
            if (val < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString();
    }

    public static final byte[] hexStringToBytes(String string) {
        if (string == null)
            return null;
        int len = string.length();
        if (len % 2 != 0)
            return null;
        len = len / 2;
        byte[] res = new byte[len];
        for (int i = 0; i < len; i++) {
            int bt = 0;
            int ch0 = string.charAt(i * 2);
            int ch1 = string.charAt(i * 2 + 1);

            if (ch0 >= '0' && ch0 <= '9')
                bt += ((ch0 - '0') << 4);
            else if (ch0 >= 'a' && ch0 <= 'f')
                bt += ((ch0 - 'a' + 10) << 4);
            else if (ch0 >= 'A' && ch0 <= 'F')
                bt += ((ch0 - 'A' + 10) << 4);
            else
                return null;

            if (ch1 >= '0' && ch1 <= '9')
                bt += (ch1 - '0');
            else if (ch1 >= 'a' && ch1 <= 'f')
                bt += (ch1 - 'a' + 10);
            else if (ch1 >= 'A' && ch1 <= 'F')
                bt += (ch1 - 'A' + 10);
            else
                return null;
            res[i] = (byte) bt;
        }
        return res;
    }

    public static final String intArrayEncode(int[] array, char seperator) {
        if (array == null)
            return null;
        StringBuilder sb = new StringBuilder();
        for (int anArray : array) {
            if (sb.length() > 0)
                sb.append(seperator);
            sb.append(String.valueOf(anArray));
        }
        return sb.toString();
    }

    public static final int[] intArrayDecode(String string, char seperator, int defValue) {
        List<String> list = stringListDecode(string, seperator);
        if (list == null)
            return null;
        int[] res = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            String item = list.get(i);
            res[i] = VLUtils.stringToInt(item, defValue);
        }
        return res;
    }

    @SafeVarargs
    public static final <T> ArrayList<T> listWithValues(T... vals) {
        ArrayList<T> res = new ArrayList<>();
        Collections.addAll(res, vals);
        return res;
    }

    public static final <K, V> List<Pair<K, V>> mapToPairList(HashMap<K, V> map) {
        List<Pair<K, V>> list = new ArrayList<>();
        for (K key : map.keySet()) {
            V value = map.get(key);
            Pair<K, V> pair = new Pair<>(key, value);
            list.add(pair);
        }
        return list;
    }

    public static final <T> int listContainsAt(List<T> list, T val) {
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            if (t.equals(val))
                return i;
        }
        return -1;
    }

    public static final <T> void listReverse(List<T> list) {
        for (int i = 0, j = list.size() - 1; i < j; i++, j--) {
            T ti = list.get(i);
            T tj = list.get(j);
            list.set(i, tj);
            list.set(j, ti);
        }
    }

    public static final <T> List<T> listDuplicate(List<T> list) {
        List<T> res = new ArrayList<>(list.size());
        res.addAll(list);
        return res;
    }

    public static final <T> void listAssertSize(List<T> list, int assertSize, Class<T> cls) throws RuntimeException {
        int listSize = list.size();
        if (listSize > assertSize) {
            for (int i = assertSize; i < listSize; i++)
                list.remove(list.size() - 1);
        } else if (listSize < assertSize) {
            for (int i = listSize; i < assertSize; i++) {
                try {
                    T t = cls.newInstance();
                    list.add(t);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    public static final boolean stringToBoolean(String str, boolean def) {
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static final int stringToInt(String str, int def) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static byte[] stringToBytes(String str, String encoding) {
        try {
            return str.getBytes(encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static double stringToDouble(String str, double def) {
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static long stringToLong(String str, long def) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return def;
        }
    }

    public static float stringToFloat(String str, float def) {
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {
            return def;
        }
    }

    private static final Random sRand = new Random();

    public static String stringRandomGenerate(char[] tokens, int len) {
        StringBuilder buf = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            buf.append(tokens[sRand.nextInt(tokens.length)]);
        return buf.toString();
    }

    public static boolean fileWrite(String filePath, byte[] data) {
        if (filePath == null)
            return false;
        int i = filePath.lastIndexOf('/');
        if (i < 0)
            return false;
        String dirPath = filePath.substring(0, i);
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs())
                return false;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            if (data != null)
                fos.write(data);
            fos.close();
        } catch (Exception e) {
            if (fos != null)
                try {
                    fos.close();
                } catch (Exception ee) {
                }
            return false;
        }
        return true;
    }

    public static boolean fileExisted(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean fileMakeDirs(String filePath) {
        File file = new File(filePath);
        return file.mkdirs();
    }

    public static long fileSize(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file.length() : 0;
    }

    public static byte[] fileRead(String filePath) {
        if (filePath == null)
            return null;
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory())
            return null;
        int fileLen = (int) file.length();
        byte[] bts = new byte[fileLen];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            int readLen = 0;
            while (readLen < fileLen)
                readLen += fis.read(bts, readLen, fileLen - readLen);
            fis.close();
            return bts;
        } catch (Exception e) {
            if (fis != null)
                try {
                    fis.close();
                } catch (Exception ee) {
                }
            return null;
        }
    }

    public static void fileStreamClose(FileInputStream fis) {
        try {
            if (fis != null)
                fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void streamWriteString(DataOutput dop, String str) throws IOException {
        if (str == null)
            dop.writeByte(0);
        else {
            dop.writeByte(1);
            dop.writeUTF(str);
        }
    }

    public static boolean streamTransfer(InputStream is, OutputStream os, int buflen) {
        byte[] buffer = new byte[buflen];
        while (true) {
            try {
                int len = is.read(buffer);
                if (len == -1)
                    break;
                os.write(buffer, 0, len);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    public static String streamReadString(DataInput dip) throws IOException {
        if (dip.readByte() == 0)
            return null;
        return dip.readUTF();
    }

    public static void streamWriteBytes(DataOutput dop, byte[] bts) throws IOException {
        if (bts == null)
            dop.writeByte(0);
        else {
            dop.writeByte(1);
            dop.writeInt(bts.length);
            dop.write(bts);
        }
    }

    public static byte[] streamReadBytes(DataInput dip) throws IOException {
        if (dip.readByte() == 0)
            return null;
        int len = dip.readInt();
        byte[] bts = new byte[len];
        dip.readFully(bts);
        return bts;
    }

    public static byte[] bitmapToPngBytes(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] bitmapToJpegBytes(Bitmap bitmap, int quality) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        return baos.toByteArray();
    }

    public static byte[] bitmapToJpegBytes(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        return baos.toByteArray();
    }

    public static Bitmap bitmapRoundCorner(Bitmap bitmap, int roundPixels) {
        if (bitmap == null)
            return null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float radius = (width > height ? height / 2 : width / 2);

        Bitmap circleBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        return circleBitmap;
    }

    public static Bitmap bitmapBlur(Bitmap bmp, int delta) {// 楂樻柉鐭╅樀
        if (bmp == null)
            return null;
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        int pixR;
        int pixG;
        int pixB;
        int pixColor;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int idx;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static Bitmap bitmapRound(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public static FileOutputStream fileOpenToWrite(String filepath) {
        try {
            File file = new File(filepath);
            if (file.isDirectory())
                return null;
            if (file.exists() && !file.delete())
                return null;
            File parent = file.getParentFile();
            if (parent != null && !parent.exists() && !parent.mkdirs())
                return null;
            return new FileOutputStream(file);
        } catch (Exception e) {
            return null;
        }
    }

    public static String bytesToString(byte[] bytes, String encoding) {
        if (bytes == null)
            return null;
        try {
            return new String(bytes, encoding);
        } catch (Exception e) {
            return null;
        }
    }

    public static final String formatDate1 = "yyyy年MM月dd日";
    public static final String formatDate2 = "yyyy-MM-dd";
    public static final String formatDate3 = "yyyy.MM.dd";
    public static final String formatDate4 = "MM.dd";
    public static final String formatDate5 = "yyyyMMdd";
    public static final String formatDate6 = "MM月dd日";
    public static final String formatDate7 = "MM-dd HH:mm";
    public static final String formatDate8 = "MM/dd/yyyy HH:mm";
    public static final String formatDate9 = "MM-dd";
    public static final String formatDate10 = "yy-MM-dd HH:mm:ss";
    public static final String formatDate11 = "yyyy年MM月";
    public static final String formatDate12 = "HH:mm:ss";
    public static final String formatDate13 = formatDate6 + "HH:mm";
    public static final String formatDate14 = formatDate1 + "HH:00";
    public static final String formatDate15 = "yyyy-MM-dd HH:mm";
    public static final String formatDate16 = formatDate3 + " HH:mm";
    public static final String formatDate17 = formatDate2 + " HH:00";
    public static final String formatDate18 = formatDate6 + " HH:00";

    public static String dateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String timeMillisToString(long timeMillis, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(new Date(timeMillis));
    }

    public static long dateTimeStringToMillis(String str, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        long res;
        try {
            Date date = dateFormat.parse(str);
            res = date.getTime();
        } catch (Exception e) {
            res = 0;
        }
        return res;
    }

    public static String fileMd5(String filePath, int bufSize) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            byte[] buffer = new byte[bufSize];
            int readCount;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            while ((readCount = fis.read(buffer)) != -1) {
                md5.update(buffer, 0, readCount);
            }
            return bytesToHexString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            fileStreamClose(fis);
        }
    }

    public static String floatToString(float val, int maxFractionDigis) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(maxFractionDigis);
        return numberFormat.format(val);
    }

    public static String jsonGetString(JSONObject jsonObject, String name, String def) {
        try {
            if (jsonObject == null)
                return def;
            String value = jsonObject.getString(name);
            if (value.equals("null"))
                return "";
            return V(value);
        } catch (Exception e) {
            return def;
        }
    }

    public static int jsonGetInt(JSONObject jsonObject, String name, int def) {
        try {
            if (jsonObject == null)
                return def;
            return jsonObject.getInt(name);
        } catch (Exception e) {
            return def;
        }
    }

    public static long jsonGetLong(JSONObject jsonObject, String name, long def) {
        try {
            if (jsonObject == null)
                return def;
            return jsonObject.getLong(name);
        } catch (Exception e) {
            return def;
        }
    }

    public static JSONArray jsonGetArray(JSONObject jsonObject, String name, JSONArray def) {
        try {
            if (jsonObject == null)
                return def;
            return jsonObject.getJSONArray(name);
        } catch (Exception e) {
            return def;
        }
    }

    public static JSONObject jsonGetObject(JSONObject jsonObject, String name, JSONObject def) {
        try {
            if (jsonObject == null)
                return def;
            return jsonObject.getJSONObject(name);
        } catch (Exception e) {
            return def;
        }
    }

    public static JSONObject jsonArrayGetObject(JSONArray jsonArray, int index, JSONObject def) {
        try {
            if (jsonArray == null)
                return def;
            return jsonArray.getJSONObject(index);
        } catch (Exception e) {
            return def;
        }
    }

    public static JSONObject jsonParseFrom(String content) {
        try {
            if (content == null)
                return null;
            return new JSONObject(content);
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONArray jsonParseArrayFrom(String content) {
        try {
            if (content == null)
                return null;
            return new JSONArray(content);
        } catch (Exception e) {
            return null;
        }
    }

    public static final int dateMillisToDayOffset(long dateMillis) {
        return (int) ((dateMillis + TimeZone.getDefault().getRawOffset()) / (24L * 60 * 60 * 1000));
    }

    /**
     * 所有用户自己安装的apk信息
     */
    public static List<PackageInfo> getAllInstalledPageages(Context context) {
        ArrayList<PackageInfo> appInstalledByUser = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> allPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : allPackages) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {//由用户安装
                appInstalledByUser.add(packageInfo);
            }
        }
        return appInstalledByUser;
    }

    /**
     * 字符串是否包含中文
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 是否隐藏键盘
     */
    public static boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            //分别获取四个点的位置
            int left = rect.left, top = rect.top, bottom = rect.bottom, right = rect.right;
            // 保留点击EditText的事件
            return !((event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom));
        }
        return false;
    }


    /**
     * string转成bitmap
     */
    public static Bitmap stringToBitmap(String st) {
        Bitmap bitmap;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static String millisToString(long millis) {
        return millisToString(millis, false);
    }

    public static String millisToString(long millis, boolean text) {
        boolean negative = millis < 0;
        millis = Math.abs(millis);

        millis /= 1000;
        int sec = Math.round((int) (millis % 60));
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        if (text) {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
            else if (min > 0)
                time = (negative ? "-" : "") + min + "min";
            else
                time = (negative ? "-" : "") + sec + "s";
        } else {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec);
            else
                time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public final static Bitmap getNetWorkBitmap(String urlString) {
        if (urlString == null) {
            return null;
        }
        VLDebug.Assert(urlString.startsWith("http://") || urlString.startsWith("https://"));
        URL imgUrl;
        Bitmap bitmap = null;
        HttpURLConnection urlConn;
        InputStream is = null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
            urlConn = (HttpURLConnection) imgUrl.openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            VLDebug.logD("[getNetWorkBitmap->]MalformedURLException");
        } catch (IOException e) {
            VLDebug.logD("[getNetWorkBitmap->]IOException");
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                }
        }
        return bitmap;
    }

    public static int getAge(Date birthDay) throws Exception {
        //获取当前系统时间
        Calendar calendar = Calendar.getInstance();
        //如果出生日期大于当前时间，则抛出异常
        if (calendar.before(birthDay)) {
            throw new IllegalArgumentException("出生日期不能大于当前日期!");
        }
        //取出系统当前时间的年、月、日部分
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        //将日期设置为出生日期
        calendar.setTime(birthDay);
        //取出出生日期的年、月、日部分
        int yearBirth = calendar.get(Calendar.YEAR);
        int monthBirth = calendar.get(Calendar.MONTH);
        int dayOfMonthBirth = calendar.get(Calendar.DAY_OF_MONTH);
        //当前年份与出生年份相减，初步计算年龄
        int age = yearNow - yearBirth;
        //当前月份与出生日期的月份相比，如果月份小于出生月份，则年龄上减1，表示不满多少周岁
        if (monthNow <= monthBirth) {
            //如果月份相等，在比较日期，如果当前日，小于出生日，也减1，表示不满多少周岁
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        return age;
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMIUI() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            return properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取缓存文件夹
     *
     * @param context 上下文
     * @return 缓存文件夹
     */
    public static File getCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return context.getExternalCacheDir();
        }
        return context.getCacheDir();
    }

    @SuppressLint("PrivateApi")
    public static int getStatusbarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x, sbar = 38;
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
}
