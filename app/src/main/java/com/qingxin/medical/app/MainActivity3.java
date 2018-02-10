package com.qingxin.medical.app;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.VideoView;

import com.qingxin.medical.R;
import com.qingxin.medical.home.districtsel.video.VideoViewHolderControl;
import com.qingxin.medical.home.districtsel.video.tools.CommonTools;
import com.qingxin.medical.home.districtsel.video.tools.DebugTools;
import com.qingxin.medical.home.districtsel.video.tools.DisplayUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity3 extends Activity {

    public static final String URL_VIDEO = "http://static.wezeit.com/o_1a9jjk9021fkt7vs1mlo16i91gvn9.mp4";
    @Bind(R.id.webview)
    public WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initView();
        initWidget();
        setupListener();
        loadData();
    }

    private void initView(){
        ButterKnife.bind(this);
        initFakeStatusBarHeight(true);
    }

    private void initWidget(){
        initWebViewSetting(mWebView);
    }

    private void setupListener(){
        setupWebViewListener();
    }

    private void loadData(){
        View view = findViewById(R.id.activity_video_rl);
        initVideoMode(view);
        String url = "http://www.wezeit.com/wap/297121.html";
        loadWebviewUrl(url);
    }
    protected void loadWebviewUrl(String url){
        DebugTools.d("js2 discovery2 jump3 vote2 news2 current url: " + url);
        if(!TextUtils.isEmpty(url)){
            mWebView.loadUrl(url);
        }
    }

    protected int mPixelInsetTop;
    protected void initFakeStatusBarHeight(boolean isNewsPage){
        View statusbarBgLayout = (View)findViewById(R.id.statusbar_bg_layout);
        if(statusbarBgLayout == null){
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPixelInsetTop = CommonTools.getStatusbarHeight(this);
            LayoutParams params = (LayoutParams)statusbarBgLayout.getLayoutParams();
            params.height = mPixelInsetTop;
            statusbarBgLayout.setLayoutParams(params);
            statusbarBgLayout.setBackgroundResource(R.color.black);
        }else{
            mPixelInsetTop = 0;
            statusbarBgLayout.setVisibility(View.GONE);
        }
    }

    //----------videoview----------------
    private VideoViewHolderControl.VideoViewHolder mVideoHolder;
    private VideoView mVideoView;
    private VideoViewHolderControl mVideoControl;
    private void initVideoMode(View view){
        showFullScreen(false);
        mVideoView = (VideoView) view.findViewById(R.id.videoview);
        mVideoHolder = new VideoViewHolderControl.VideoViewHolder(view);
        mVideoHolder.imgIv.setImageResource(R.mipmap.index);
        mVideoControl = new VideoViewHolderControl(mVideoHolder, mVideoView, URL_VIDEO);
        setupVideoControlListener(mVideoControl);
        mVideoControl.setup();
        setVideoViewLayout(false);
    }

    private void setupVideoControlListener(VideoViewHolderControl control){
        control.setOnVideoControlListener(new VideoViewHolderControl.OnVideoControlProxy() {
            @Override
            public void onCompletion() {
                DebugTools.d("video2 onCompletion");
                setFullScreen(false);
            }

            @Override
            public void onClickHalfFullScreen() {
                boolean isFull = isFullScreen();
                setFullScreen(!isFull);
            }

            @Override
            public void onError(int code, String msg) {

            }

        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            initHalfFullState(true);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            initHalfFullState(false);
        }
    }

    private void initHalfFullState(boolean isFull){
        DebugTools.d("video2 initHalfFullState isFull: " + isFull);
        setVideoViewLayout(isFull);
        showFullScreen(isFull);
    }


    //---------videoview fullscreen---------
    private void showFullScreen(boolean isFullScreen){
        if(isFullScreen){
//		      //不显示程序的标题栏
            hideNavigationBar();
        }else{
            showNavigationBar();
        }
    }

    protected void setFullScreen(boolean isFull){
        if(isFull){
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }else{
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    protected boolean isFullScreen(){
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void showNavigationBar(){
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    private void setVideoViewLayout(boolean isFull){
        LayoutParams params = (LayoutParams)mVideoHolder.videoRl.getLayoutParams();
        LayoutParams controlParams = (LayoutParams)mVideoHolder.mediaControl.getLayoutParams();
        LayoutParams indexImageParams = (LayoutParams)mVideoHolder.imgIv.getLayoutParams();

        int videoMarginTop = (int)getResources().getDimension(R.dimen.library_video_video_margin_top) + mPixelInsetTop;
        if(isFull){
            params.height = LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(0, 0, 0, 0);

            controlParams.setMargins(0, 0, 0, 0);

            indexImageParams.height = LayoutParams.MATCH_PARENT;
            indexImageParams.setMargins(0, 0, 0, 0);
        }else{
            params.height = DisplayUtil.dip2px(this, 202);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.setMargins(0, videoMarginTop, 0, 0);

            controlParams.setMargins(0, 0, 0, 0);

            indexImageParams.height = DisplayUtil.dip2px(this, 202);
            indexImageParams.setMargins(0, 0, 0, 0);

        }
        mVideoHolder.videoRl.setLayoutParams(params);
        mVideoHolder.mediaControl.setLayoutParams(controlParams);
        mVideoHolder.imgIv.setLayoutParams(indexImageParams);
    }


    //--------------webview--------------
    protected void initWebViewStorage(WebView webview){
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAppCacheMaxSize(1024*1024*8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);

        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(appCachePath);
    }

    protected void initWebViewSetting(WebView webview){
        WebSettings webseting = webview.getSettings();
//        webseting.setRenderPriority(RenderPriority.HIGH);
        webseting.setJavaScriptEnabled(true);
//		webseting.setPluginsEnabled(true);
        webseting.setSupportZoom(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webseting.setUseWideViewPort(true);
        webseting.setLoadWithOverviewMode(true);


        initWebViewStorage(webview);
    }

    protected void setupWebViewListener(){
        mWebView.setWebViewClient(new WebViewClient() {

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            handleClickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void handleClickBack(){
        if(isFullScreen()){
            setFullScreen(false);
            return;
        }
        if (mWebView != null){
            mWebView.onPause();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
