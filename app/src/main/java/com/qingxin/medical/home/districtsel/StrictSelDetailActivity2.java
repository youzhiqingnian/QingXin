package com.qingxin.medical.home.districtsel;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.qingxin.medical.R;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.home.districtsel.video.VideoViewHolderControl;
import com.qingxin.medical.home.districtsel.video.tools.DebugTools;
import com.vlee78.android.vl.VLUtils;

import butterknife.ButterKnife;

/**
 * 严选详情界面
 * Date 2018/2/8
 *
 * @author zhikuo
 */
public class StrictSelDetailActivity2 extends QingXinActivity {

    public static void startSelf(@NonNull Context context, @NonNull StrictSelBean strictSelBean) {
        Intent intent = new Intent(context, StrictSelDetailActivity2.class);
        intent.putExtra(STRICTSEL_BEAN, strictSelBean);
        context.startActivity(intent);
    }

    public static final String STRICTSEL_BEAN = "STRICTSEL_BEAN";

    public static final String URL_VIDEO = "http://static.wezeit.com/o_1a9jjk9021fkt7vs1mlo16i91gvn9.mp4";

    protected int mPixelInsetTop;

    private VideoViewHolderControl.VideoViewHolder mVideoHolder;
    private VideoView mVideoView;
    private VideoViewHolderControl mVideoControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strictsel_detail);
        /*TextView nameTv = findViewById(R.id.nameTv);
        TextView locationTv = findViewById(R.id.locationTv);
        TextView countTv = findViewById(R.id.countTv);
        TextView descrTv = findViewById(R.id.descrTv);
        StrictSelBean strictSelBean = (StrictSelBean) getIntent().getSerializableExtra(STRICTSEL_BEAN);
        nameTv.setText(strictSelBean.getName());
        descrTv.setText(strictSelBean.getSummary());
        countTv.setText(String.format("%s 次播放", strictSelBean.getOrder()));
        AMapLocation aMapLocation = getModel(GaoDeMapModel.class).getAMLocation();
        if (null != aMapLocation) {
            if (aMapLocation.getProvince().equals(aMapLocation.getCity())) {
                locationTv.setText(String.format("%s", aMapLocation.getProvince()));
            } else {
                locationTv.setText(String.format("%s%s", aMapLocation.getProvince(), aMapLocation.getCity()));
            }
        }*/
        initView();
        loadData();
    }

    private void initView() {
        ButterKnife.bind(this);
//        initFakeStatusBarHeight(true);
    }


    private void loadData() {
        View view = findViewById(R.id.strictVideoRl);
        initVideoMode(view);
    }

//    protected void initFakeStatusBarHeight(boolean isNewsPage) {
//        View statusbarBgLayout = (View) findViewById(R.id.statusbar_bg_layout);
//        if (statusbarBgLayout == null) {
//            return;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            mPixelInsetTop = CommonTools.getStatusbarHeight(this);
//            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) statusbarBgLayout.getLayoutParams();
//            params.height = mPixelInsetTop;
//            statusbarBgLayout.setLayoutParams(params);
//            statusbarBgLayout.setBackgroundResource(R.color.black);
//        } else {
//            mPixelInsetTop = 0;
//            statusbarBgLayout.setVisibility(View.GONE);
//        }
//    }

    private void initVideoMode(View view) {
        showFullScreen(false);
        mVideoView = (VideoView) view.findViewById(R.id.videoview);
        mVideoHolder = new VideoViewHolderControl.VideoViewHolder(view);
        mVideoHolder.imgIv.setImageResource(R.mipmap.index);
        mVideoControl = new VideoViewHolderControl(mVideoHolder, mVideoView, URL_VIDEO);
        setupVideoControlListener(mVideoControl);
        mVideoControl.setup();
        setVideoViewLayout(false);
    }

    private void setupVideoControlListener(VideoViewHolderControl control) {
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

    private void initHalfFullState(boolean isFull) {
        DebugTools.d("video2 initHalfFullState isFull: " + isFull);
        setVideoViewLayout(isFull);
        showFullScreen(isFull);
    }


    //---------videoview fullscreen---------
    private void showFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
//		      //不显示程序的标题栏
            hideNavigationBar();
        } else {
            showNavigationBar();
        }
    }

    protected void setFullScreen(boolean isFull) {
        if (isFull) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    protected boolean isFullScreen() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void showNavigationBar() {
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

    private void setVideoViewLayout(boolean isFull) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mVideoHolder.videoRl.getLayoutParams();
        RelativeLayout.LayoutParams controlParams = (RelativeLayout.LayoutParams) mVideoHolder.mediaControl.getLayoutParams();
        RelativeLayout.LayoutParams indexImageParams = (RelativeLayout.LayoutParams) mVideoHolder.imgIv.getLayoutParams();

        int videoMarginTop = (int) getResources().getDimension(R.dimen.library_video_video_margin_top) + mPixelInsetTop;
        if (isFull) {
            params.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(0, 0, 0, 0);

            controlParams.setMargins(0, 0, 0, 0);

            indexImageParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            indexImageParams.setMargins(0, 0, 0, 0);
        } else {
            params.height = VLUtils.dip2px(202);
//            params.addRule(RelativeLayout.);
            params.setMargins(0, videoMarginTop, 0, 0);

            controlParams.setMargins(0, 0, 0, 0);

            indexImageParams.height = VLUtils.dip2px(202);
            indexImageParams.setMargins(0, 0, 0, 0);

        }
        mVideoHolder.videoRl.setLayoutParams(params);
        mVideoHolder.mediaControl.setLayoutParams(controlParams);
        mVideoHolder.imgIv.setLayoutParams(indexImageParams);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            handleClickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void handleClickBack() {
        if (isFullScreen()) {
            setFullScreen(false);
            return;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
