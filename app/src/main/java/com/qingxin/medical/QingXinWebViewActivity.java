package com.qingxin.medical;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.qingxin.medical.base.QingXinActivity;
import com.vlee78.android.vl.VLTitleBar;
import com.vlee78.android.vl.VLUtils;
import com.vlee78.android.vl.VLWebView;

/**
 * 公共加载网页界面
 */
public class QingXinWebViewActivity extends QingXinActivity {

    private VLWebView mWebView;
    public static final String URL = "url";
    public static final String TITLE = "title";

    public static void startSelf(Context context, String url, String title) {
        Intent intent = getIntent(context, url, title);
        context.startActivity(intent);
    }

    public static void startSelf(Context context, String url) {
        startSelf(context, url, null);
    }

    public static Intent getIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, QingXinWebViewActivity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        final VLTitleBar titleBar = findViewById(R.id.titleBar);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        mWebView = findViewById(R.id.webView);
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                String webTitle = getIntent().getStringExtra(TITLE);
                if (VLUtils.stringIsEmpty(webTitle)) {
                    webTitle = title;
                }
                QingXinTitleBar.init(titleBar, webTitle, 10, 10);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                    }
                    return;
                }
                if (progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
        };
        mWebView.getWebView().setWebChromeClient(webChromeClient);
        QingXinTitleBar.setLeftReturn(titleBar, this);
        String url = getIntent().getStringExtra(URL);
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.goBack()) {
            super.onBackPressed();
        }
    }
}
