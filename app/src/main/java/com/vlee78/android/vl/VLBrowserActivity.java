package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ZoomButtonsController;

import java.lang.reflect.Field;

@SuppressLint("SetJavaScriptEnabled")
public class VLBrowserActivity extends VLActivity {
    private static final String KEY_URL = "key_url";

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, VLBrowserActivity.class);
        intent.putExtra(KEY_URL, url);
        context.startActivity(intent);
    }

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(KEY_URL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        WebViewClient webViewClient = new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };

        mWebView = new WebView(this);
        mWebView.setLayoutParams(params);
        mWebView.setWebViewClient(webViewClient);

        WebSettings webSettings = mWebView.getSettings();
        mWebView.setInitialScale(37);
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setBuiltInZoomControls(true); // 是否支持缩放（是否使用内置放大机制）
        webSettings.setUseWideViewPort(true); // WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
        webSettings.setJavaScriptEnabled(true); // 是否启用JavaScript
        webSettings.setBlockNetworkImage(false); // 把图片加载放在最后来加载渲染
        webSettings.setLoadsImagesAutomatically(true);// 是否自动加载图像资源
        webSettings.setSupportMultipleWindows(true);// 是否支持多窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 是否允许JavaScript自动打开窗口
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置应用程序缓存模式
        webSettings.setDomStorageEnabled(true);// 支持本地存储;
        webSettings.setLoadWithOverviewMode(true);
        if (android.os.Build.VERSION.SDK_INT >= 11) {// 用于判断是否为Android 3.0系统,
            webSettings.setDisplayZoomControls(false);
        } else {
            setZoomControlGone(mWebView); // Android 3.0(11) 以下使用以下方法
        }

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(url);

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && mWebView.canGoBack()) {
                        mWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });

        setContentView(mWebView);
    }

    // 实现放大缩小控件隐藏
    public void setZoomControlGone(View view) {
        Class<WebView> classType;
        Field field;
        try {
            classType = WebView.class;
            field = classType.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
                    view);
            mZoomButtonsController.getZoomControls().setVisibility(View.GONE);
            try {
                field.set(view, mZoomButtonsController);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
