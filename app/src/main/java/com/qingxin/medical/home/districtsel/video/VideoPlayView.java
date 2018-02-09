package com.qingxin.medical.home.districtsel.video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLApplication;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLScheduler;
import com.vlee78.android.vl.VLUtils;

/**
 * 视频播放界面
 */
public class VideoPlayView extends FrameLayout implements View.OnClickListener {

    public VideoPlayView(Context context) {
        this(context, null, 0);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    private ImageView mPlayIv, mPauseIv;
    private ProgressBar mProgressBar;
    private LinearLayout mPlayController;
    private TextView mStartTimeTv, mTotalTimeTv;
    private SeekBar mSeekBar;
    private SimpleDraweeView mImageView;
    private FrameLayout mFrameLayout;
    private VLKsyMediaPlayerModel mI90KsyMediaPlayerModel;
    private boolean mIsControllerShow;
    private boolean isPause;
    private int mVideoProgress;
    private Context mContext;
    private VLKsyMediaPlayerModel.PlayerHolder mPlayerHolder;

    public void pause() {
        if (mPlayerHolder != null) mI90KsyMediaPlayerModel.pause(mPlayerHolder);
    }

    public void release() {
        if (mPlayerHolder != null) mI90KsyMediaPlayerModel.release(mPlayerHolder);
    }

    public void initView(String videoPath, String coverPath) {
        initView(videoPath, coverPath, null, true);
    }

    public void initView(String videoPath, String coverPath, String group) {
        initView(videoPath, coverPath, group, true);
    }

    @SuppressLint("InflateParams")
    public void initView(String videoPath, String coverPath, String group, boolean useUrlDecorder) {
        VLDebug.Assert(!VLUtils.stringIsEmpty(videoPath) && !VLUtils.stringIsEmpty(coverPath));
        mI90KsyMediaPlayerModel = VLApplication.instance().getModel(VLKsyMediaPlayerModel.class);
        View view = LayoutInflater.from(mContext).inflate(R.layout.group_videoplay_view, null);
        RelativeLayout pauseRl = view.findViewById(R.id.pauseRl);
        mImageView = view.findViewById(R.id.imageView);
        mPlayIv = view.findViewById(R.id.playIv);
        mProgressBar = view.findViewById(R.id.progressBar);
        mPlayController = view.findViewById(R.id.playController);
        mPauseIv = view.findViewById(R.id.pauseIv);
        mTotalTimeTv = view.findViewById(R.id.totalTimeTv);
        mFrameLayout = view.findViewById(R.id.frameLayout1);
        mSeekBar = view.findViewById(R.id.seekBar);
        mStartTimeTv = view.findViewById(R.id.startTimeTv);
        pauseRl.setOnClickListener(this);
        mPlayIv.setOnClickListener(this);
        mVideoProgress = 0;
        isPause = true;
        mIsControllerShow = false;
        mPlayController.setVisibility(GONE);
        //TODO
        //mPauseIv.setImageResource(R.drawable.ic_video_start);

        mPlayerHolder = new VLKsyMediaPlayerModel.PlayerHolder();
        mPlayerHolder.setFrameLayout(mFrameLayout);
        if (group != null) mPlayerHolder.setTag("group", group);
        mPlayerHolder.setUrl(videoPath);

        initSeekBar();
        addView(view);
    }

    public void stopPlay() {
        mSeekBar.setProgress(0);
        mPlayIv.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mImageView.setVisibility(View.VISIBLE);
        //TODO
        //mPauseIv.setImageResource(R.drawable.ic_video_start);
        mPlayController.setVisibility(View.GONE);
        isPause = true;
        mIsControllerShow = false;
    }

    public void stopPlayAndRelease() {
        stopPlay();
        mI90KsyMediaPlayerModel.release(mPlayerHolder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playIv:
                startPlay();
                break;
            case R.id.pauseRl:
                isPause = !isPause;
                if (isPause) {//暂停
                    //TODO
                    //mPauseIv.setImageResource(R.drawable.ic_video_pause);
                    mI90KsyMediaPlayerModel.pause(mPlayerHolder);
                } else {//播放
                    //TODO
                    //mPauseIv.setImageResource(R.drawable.ic_video_start);
                    mI90KsyMediaPlayerModel.resume(mPlayerHolder);
                    if (mPlayIv.getVisibility() == View.VISIBLE) {
                        mPlayIv.setVisibility(View.GONE);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void initSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoProgress = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MediaPlayer mediaPlayer = mPlayerHolder.getMediaPlayer();
                if (mediaPlayer != null) {
                    if (mPlayIv.getVisibility() == View.VISIBLE) {
                        mPlayIv.setVisibility(View.GONE);
                    }
                    mediaPlayer.seekTo(mVideoProgress);
                    setVideoProgress(mVideoProgress);
                }
            }
        });
    }

    public void startPlay() {
        mPlayerHolder.setOnVideoPlayListener(new VLKsyMediaPlayerModel.OnVideoPlayListener() {

            @Override
            public void onRelease() {
                stopPlay();
            }

            @Override
            public void onPrepare() {
                mPlayIv.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStart(int width, int height) {
                isPause = false;
                mIsControllerShow = true;
                mProgressBar.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                mPlayController.setVisibility(View.VISIBLE);
                //TODO
                //mPauseIv.setImageResource(R.drawable.ic_video_start);
                int screenWidth = VLUtils.getScreenWidth(mContext);
                if (width < screenWidth) {//视频宽度小于屏幕宽度  将高度按 视频宽度/屏幕宽度 比率拉伸
                    height = height * screenWidth / width;
                }
                mFrameLayout.setLayoutParams(VLUtils.paramsFrame(screenWidth, height, Gravity.CENTER));
                mFrameLayout.setOnTouchListener(mOnTouchListener);
                setVideoProgress(0);
                VLScheduler.instance.schedule(4000, VLScheduler.THREAD_MAIN, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        mIsControllerShow = false;
                        mPlayController.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onComplete() {
                mPlayIv.setVisibility(View.VISIBLE);
                mIsControllerShow = false;
                isPause = true;
                //TODO
                //mPauseIv.setImageResource(R.drawable.ic_video_pause);
            }

            @Override
            public void onSeekComplete() {
                mPlayIv.setVisibility(View.GONE);
                isPause = false;
                //TODO
                //mPauseIv.setImageResource(R.drawable.ic_video_start);
            }

            @Override
            public void onError() {
            }

            @Override
            public void onPause() {

            }
        });
        mI90KsyMediaPlayerModel.start(mPlayerHolder);
    }

    /**
     * 视频进度条
     *
     * @param currentProgress 拖拽视频位置
     */
    private int setVideoProgress(int currentProgress) {
        if (mSeekBar == null)
            return -1;
        long time = currentProgress > 0 ? currentProgress : (int) mI90KsyMediaPlayerModel.getCurrentPosition(mPlayerHolder);
        long length = (int) mI90KsyMediaPlayerModel.getDuration(mPlayerHolder);

        mSeekBar.setMax((int) length);
        mSeekBar.setProgress((int) time);
        if (time >= 0) {
            if (mStartTimeTv != null)
                mStartTimeTv.setText(VLUtils.millisToString(time));
            if (mTotalTimeTv != null)
                mTotalTimeTv.setText(VLUtils.millisToString(length));
        }
        VLScheduler.instance.schedule(1000, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                setVideoProgress(0);
            }
        });
        return (int) time;
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mIsControllerShow = !mIsControllerShow;
            if (mIsControllerShow) {
                mPlayController.setVisibility(View.VISIBLE);
                VLScheduler.instance.schedule(4000, VLScheduler.THREAD_MAIN, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        mIsControllerShow = false;
                        mPlayController.setVisibility(View.GONE);
                    }
                });
            } else {
                VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                    @Override
                    protected void process(boolean canceled) {
                        mPlayController.setVisibility(View.GONE);
                    }
                });
            }
            return false;
        }
    };
}
