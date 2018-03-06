package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class VLWebView extends FrameLayout {

    private WebView mWebView;

    public VLWebView(Context context) {
        this(context, null, 0);
    }

    public VLWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VLWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void init() {
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        };
        mWebView = new WebView(getContext());
        mWebView.setLayoutParams(VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);// 设置应用程序缓存模式
        webSettings.setSupportZoom(true); // 可以缩放
        webSettings.setJavaScriptEnabled(true);// 是否启用JavaScript
        webSettings.setBuiltInZoomControls(true); // 是否支持缩放（是否使用内置放大机制）
        webSettings.setUseWideViewPort(true); // WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
        webSettings.setBlockNetworkImage(false); // 把图片加载放在最后来加载渲染
        webSettings.setLoadsImagesAutomatically(true);// 是否自动加载图像资源
        webSettings.setSupportMultipleWindows(true);// 是否支持多窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 是否允许JavaScript自动打开窗口
        webSettings.setDomStorageEnabled(true);// 支持本地存储;
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);
        mWebView.setWebViewClient(webViewClient);
        mWebView.setWebChromeClient(new WebChromeClient());
        addView(mWebView);
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

    public boolean goBack() {
        if (!mWebView.canGoBack()) return false;
        mWebView.goBack();
        return true;
    }

    public WebView getWebView() {
        return mWebView;
    }

}
