package com.vlee78.android.vl;


public class VLVitamioModel extends VLModel {
  /*  private MediaPlayer mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private String mUrl;
    private FrameLayout mFrameLayout;
    private SurfaceView mSurfaceView;
    private boolean mIsPlaying;
    private boolean mIsVideoSizeKnown;
    private int mVideoWidth;
    private int mVideoHeight;
    private boolean mIsVideoToBePlayed;

    @Override
    protected void onCreate() {
        super.onCreate();
        mMediaPlayer = null;
        mSurfaceHolder = null;
        mUrl = null;
        mFrameLayout = null;
        mSurfaceView = null;
        mIsPlaying = false;
        mIsVideoSizeKnown = false;
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoToBePlayed = false;
        Vitamio.isInitialized(getConcretApplication());
    }

    public void stop() {
        dettach();
        if (mMediaPlayer != null) mMediaPlayer.release();
        mMediaPlayer = null;
        mIsPlaying = false;
        mUrl = null;
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    public void start(String url, FrameLayout frameLayout) {
        if (mIsPlaying) {
            if (mUrl.equals(url)) {//播放相同的源
                dettach();
                attach(frameLayout);
                return;
            }
            stop();//停止释放当前的播放
        }

        VLDebug.Assert(!mIsPlaying && mUrl == null && mMediaPlayer == null && mFrameLayout == null && mSurfaceView == null && mSurfaceHolder == null && !mIsVideoToBePlayed && !mIsVideoSizeKnown);
        mIsPlaying = true;
        mUrl = url;

        mMediaPlayer = new MediaPlayer(getConcretApplication());
        mFrameLayout = frameLayout;
        mSurfaceView = new SurfaceView(getConcretApplication());
        mSurfaceView.setLayoutParams(VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        mFrameLayout.addView(mSurfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mMediaPlayer.setDataSource(mUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mMediaPlayer.setDisplay(mSurfaceHolder);
                mMediaPlayer.prepareAsync();

                mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mIsVideoToBePlayed = true;
                        play();
                    }
                });

                mMediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        mIsVideoSizeKnown = true;
                        mVideoWidth = width;
                        mVideoHeight = height;
                        if (mSurfaceHolder != null)
                            mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
                        play();
                    }
                });
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void play() {
                if (mIsVideoSizeKnown && mIsVideoToBePlayed) {
                    mIsVideoToBePlayed = false;
                    if (mMediaPlayer != null) mMediaPlayer.start();
                }
            }
        });
    }

    public boolean isAttached() {
        return mFrameLayout != null;
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public boolean dettach() {
        if (!isPlaying()) return false;
        if (!isAttached()) return false;
        if (mMediaPlayer != null) mMediaPlayer.setDisplay(null);
        if (mFrameLayout != null) mFrameLayout.removeAllViews();
        mFrameLayout = null;
        mSurfaceView = null;
        mSurfaceHolder = null;
        return true;
    }

    public boolean attach(FrameLayout frameLayout) {
        if (!isPlaying()) return false;
        if (isAttached()) {
            if (mFrameLayout == frameLayout) return true;
            dettach();
        }

        VLDebug.Assert(mIsPlaying && mMediaPlayer != null && mFrameLayout == null && mSurfaceView == null && mSurfaceHolder == null);
        mFrameLayout = frameLayout;
        mSurfaceView = new SurfaceView(frameLayout.getContext());
        mSurfaceView.setLayoutParams(VLUtils.paramsFrame(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        frameLayout.addView(mSurfaceView);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceHolder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mMediaPlayer != null) mMediaPlayer.setDisplay(mSurfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        return true;
    }*/
}