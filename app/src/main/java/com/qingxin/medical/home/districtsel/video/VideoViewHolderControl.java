package com.qingxin.medical.home.districtsel.video;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.qingxin.medical.QingXinConstants;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLUtils;


public class VideoViewHolderControl {
	
	private VideoViewHolder mHolder;
	private VideoView mVideoView;
	private String mUrl;
	private boolean mIsFullScreen = false;
	private boolean mIsFullScreenMode = false;
	public VideoViewHolderControl(VideoViewHolder holder, VideoView videoView, String url){
        mHolder = holder;		
        mVideoView = videoView;
        mUrl = url;
//        mUrl = "http://img.owspace.com/V_tnh2100_1453346133.2098116.m3u8";
	}
	
	public void setup(){
		setupPlayListener(mHolder);
		showInitStateState(mHolder);
	}
	
	public void play(){
		if(mHolder != null){
			handleStartPlayVideo(mHolder);
		}
	}
	
	public void setIsFullScreenMode(boolean isFullMode){
		mIsFullScreenMode = isFullMode;
		if(mIsFullScreenMode){
			if(mHolder != null){
				mHolder.halfFullIb.setImageResource(R.mipmap.library_video_mediacontroller_to_half);
				hideFullHalIb(mHolder);
			}
		}
	}
	
	private void hideFullHalIb(VideoViewHolder holder){
		if(holder == null){
			return;
		}
		
		holder.halfFullIb.setVisibility(View.GONE);
		RelativeLayout.LayoutParams totalTimeParams = (RelativeLayout.LayoutParams)holder.totalTimeTv.getLayoutParams();
		totalTimeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		totalTimeParams.setMargins(0, 0, VLUtils.dip2px(10), 0);
		holder.totalTimeTv.setLayoutParams(totalTimeParams);
	}
	
	private void setupPlayListener(final VideoViewHolder holder){
		holder.playPauseIb.setOnClickListener(v -> handleStartPlayVideo(holder));
		
		holder.imgIv.setOnClickListener(v -> handleStartPlayVideo(holder));
		
		holder.playSymbolIv.setOnClickListener(v -> handleStartPlayVideo(holder));
		
		if(holder.halfFullIb != null){
			holder.halfFullIb.setOnClickListener(v -> {
                if(mIsFullScreenMode){
                    if(mOnVideoControlListener != null){
                        mOnVideoControlListener.onClickHalfFullScreen();
                    }
                }else{
                    if(mOnVideoControlListener != null){
                        mOnVideoControlListener.onClickHalfFullScreen();
                        mIsFullScreen = !mIsFullScreen;
                        setHalfFullScreenState(holder, mIsFullScreen);
                    }
                }
            });
		}
		
		if(holder.backIb != null){
			holder.backIb.setOnClickListener(v -> {
                if(mIsFullScreenMode){
                    if(mOnVideoControlListener != null){
                        mOnVideoControlListener.onClickHalfFullScreen();
                    }
                }
            });
		}
	}
	
	private void setHalfFullScreenState(VideoViewHolder holder, boolean isFull){
		if(isFull){
			holder.halfFullIb.setImageResource(R.mipmap.library_video_mediacontroller_to_half);
		}else{
			holder.halfFullIb.setImageResource(R.mipmap.library_video_mediacontroller_to_full);
		}
	}
	
	private void handleStartPlayVideo(VideoViewHolder holder){
		if(mPlayState == PlayState.INIT){
			if(mVideoView == null){
				return;
			}
			if(TextUtils.isEmpty(mUrl)){
				if(mOnVideoControlListener != null){
					mOnVideoControlListener.onError(0, "视频地址为空");
				}
				return;
			}
				
			if(mVideoView.getVisibility() != View.VISIBLE){
				mVideoView.setVisibility(View.VISIBLE);
			}
		
			initVideoView(holder, mVideoView);
			handlePrepareVideo(holder);
			setupVideoViewListener(mVideoView, holder);
			mVideoView.setVideoPath(mUrl);
			if(mOnVideoControlListener != null){
				mOnVideoControlListener.onClickPlay();
			}
		}else if(mPlayState == PlayState.PAUSE){
			handlePlayVideo(holder);
			if(mOnVideoControlListener != null){
				mOnVideoControlListener.onClickResume();
			}
		}else if(mPlayState == PlayState.PLAYING){
			handlePauseVideo(holder);
			if(mOnVideoControlListener != null){
				mOnVideoControlListener.onClickPause();
			}
		}
	}

	private void handlePrepareVideo(final VideoViewHolder holder){
		showPreparePlayState(holder);
	}
	
	private void handlePlayVideo(final VideoViewHolder holder){
		showPlayingState(holder);;
		mVideoView.start();
	}
	
	private void handlePauseVideo(final VideoViewHolder holder){
		showPauseState(holder);
		mVideoView.pause();
	}
	
	public void pause(){
		if(mPlayState == PlayState.PLAYING){
			handlePauseVideo(mHolder);
			if(mOnVideoControlListener != null){
				mOnVideoControlListener.onClickPause();
			}
		}else if(mPlayState == PlayState.PREPARE){
			stop();
		}
	}
	
	public void stop(){
		if(!isInIdleState()){
			stopVideo(mHolder, mVideoView);
		}
	}
	
	private void stopVideo(VideoViewHolder holder, VideoView videoView){
		if(videoView != null){
			videoView.setVisibility(View.GONE);
			videoView.stopPlayback();
			holder.pb.setVisibility(View.GONE);
			showInitStateState(holder);
		}
	}
	
	private boolean isInIdleState(){
		return mPlayState == PlayState.INIT;
	}
	
    private PlayState mPlayState = PlayState.INIT;
	
	private enum PlayState{
		INIT, PREPARE, PLAYING, PAUSE
	}
	
	private void showPlayingState(VideoViewHolder holder){
		mPlayState = PlayState.PLAYING;
		
		updateViewVisibleState(holder.pb, View.GONE);
		updateViewVisibleState(holder.imgIv, View.INVISIBLE);
		updateViewVisibleState(holder.seekBar, View.VISIBLE);
		updateViewVisibleState(holder.playSymbolIv, View.INVISIBLE);
		
		holder.playPauseIb.setImageResource(R.mipmap.video_pause_logo);
		sendDismissVideoControlBarDelay(holder);
	}
	
	private void showPauseState(VideoViewHolder holder){
		mPlayState = PlayState.PAUSE;
		
		updateViewVisibleState(holder.pb, View.GONE);
		updateViewVisibleState(holder.imgIv, View.INVISIBLE);
		updateViewVisibleState(holder.seekBar, View.VISIBLE);
		updateViewVisibleState(holder.mediaControl, View.VISIBLE);
		updateViewVisibleState(holder.backIb, View.VISIBLE);
		updateViewVisibleState(holder.playSymbolIv, View.VISIBLE);
		
		holder.playPauseIb.setImageResource(R.mipmap.library_video_mediacontroller_play);
		mHandler.removeMessages(QingXinConstants.MSG_DISMISS_VIDEO_CONTROL_BAR);
	}
	
	private void showPreparePlayState(VideoViewHolder holder){
		mPlayState = PlayState.PREPARE;
		if(!mIsFullScreenMode){
			updateViewVisibleState(holder.imgIv, View.INVISIBLE);
		}

		holder.playPauseIb.setImageResource(R.mipmap.library_video_mediacontroller_pause);
		
		updateViewVisibleState(holder.pb, View.VISIBLE);
		updateViewVisibleState(holder.seekBar, View.VISIBLE);
		updateViewVisibleState(holder.mediaControl, View.INVISIBLE);
		updateViewVisibleState(holder.backIb, View.INVISIBLE);
		updateViewVisibleState(holder.playSymbolIv, View.INVISIBLE);
	}
	
	private void showInitStateState(VideoViewHolder holder){
		mPlayState = PlayState.INIT;
		holder.seekBar.setProgress(0);
		holder.playPauseIb.setImageResource(R.mipmap.library_video_mediacontroller_play);
		
		updateViewVisibleState(holder.pb, View.GONE);
		updateViewVisibleState(holder.imgIv, View.VISIBLE);
		updateViewVisibleState(holder.seekBar, View.INVISIBLE);
		updateViewVisibleState(holder.mediaControl, View.INVISIBLE);
		updateViewVisibleState(holder.backIb, View.INVISIBLE);
		updateViewVisibleState(holder.playSymbolIv, View.VISIBLE);
		
		mHandler.removeMessages(QingXinConstants.MSG_DISMISS_VIDEO_CONTROL_BAR);
	}
	
	private void updateViewVisibleState(View view, int visibleState){
		if(!mIsFullScreenMode && view == mHolder.backIb){
			return;
		}

		if(view == null){
			return;
		}
		
		if(view.getVisibility() != visibleState){
			view.setVisibility(visibleState);
		}
	}
	
	private void initVideoView(VideoViewHolder holder, VideoView videoView){
		videoView.setMediaController(holder.mediaControl); 
		holder.mediaControl.setMediaPlayer(videoView); 
        //让VideiView获取焦点 
		videoView.requestFocus();
	}
	
	private void setupVideoViewListener(final VideoView videoView, final VideoViewHolder holder){
		videoView.setOnCompletionListener(mp -> {
            if(videoView != null){
                if(mp != null){
                    mp.reset();
                }
                videoView.seekTo(0);
                videoView.stopPlayback();
                showInitStateState(holder);
            }
            if(mOnVideoControlListener != null){
                mOnVideoControlListener.onCompletion();
            }
        });
		videoView.setOnPreparedListener(mp -> {
            mVideoView.start();
            showPlayingState(holder);
            holder.mediaControl.show();
            updateViewVisibleState(holder.backIb, View.VISIBLE);
            mHandler.sendEmptyMessageDelayed(QingXinConstants.MSG_SET_VIDEO_VIEW_TRANSPARENT, QingXinConstants.DELAY_MSG_SET_VIDEO_VIEW_TRANSPARENT);
            sendDismissVideoControlBarDelay(holder);
            if(mIsFullScreenMode && mProgress > 1000){
                seekTo(mProgress);
            }
            if(mOnVideoControlListener != null){
                mOnVideoControlListener.onPrepared();
            }
        });
		

		((WylVideoView)videoView).setOnGestureListener(new WylVideoView.OnGestureListener() {
			@Override
			public void onSingleTapConfirmed() {
				boolean isShow = (holder.mediaControl.getVisibility() == View.VISIBLE);
				showVideoControlBar(holder, !isShow);
			}
				
			@Override
			public void onDoubleTap() {
				handleStartPlayVideo(holder);
			}
		});
		
		if(holder.videoRl != null){
			holder.videoRl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean isShow = (holder.mediaControl.getVisibility() == View.VISIBLE);
					showVideoControlBar(holder, !isShow);
				}
			});
		}
		
		holder.mediaControl.setAdditionalSeekBarListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				sendDismissVideoControlBarDelay(holder);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mHandler.removeMessages(QingXinConstants.MSG_DISMISS_VIDEO_CONTROL_BAR);
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			}
		});
	}
	
	private void showVideoControlBar(final VideoViewHolder holder, final boolean isShow){
		mHandler.removeMessages(QingXinConstants.MSG_DISMISS_VIDEO_CONTROL_BAR);
		
		updateViewVisibleState(holder.mediaControl, View.VISIBLE);
		updateViewVisibleState(holder.backIb, View.VISIBLE);
		Animation anim = null;
		
		if(isShow){
			anim = new TranslateAnimation(0, 0, 100, 0);
		}else{
			anim = new TranslateAnimation(0, 0, 0, 100);
		}
		
		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				if(isShow){
					updateViewVisibleState(holder.mediaControl, View.VISIBLE);
					updateViewVisibleState(holder.backIb, View.VISIBLE);
					sendDismissVideoControlBarDelay(holder);
				}else{
					updateViewVisibleState(holder.mediaControl, View.INVISIBLE);
					updateViewVisibleState(holder.backIb, View.INVISIBLE);
				}
			}
		});
		anim.setDuration(300);
		holder.mediaControl.startAnimation(anim);
		
		if(holder.backIb != null){
			Animation backAnim = null;
			if(isShow){
				backAnim = new AlphaAnimation(0, 1);
			}else{
				backAnim = new AlphaAnimation(1, 0);
			}
			backAnim.setDuration(300);
			holder.backIb.startAnimation(backAnim);
		}
	}
	
	private void sendDismissVideoControlBarDelay(VideoViewHolder holder){
		Message msg = mHandler.obtainMessage(QingXinConstants.MSG_DISMISS_VIDEO_CONTROL_BAR, holder);
		mHandler.sendMessageDelayed(msg, QingXinConstants.DELAY_MSG_DISMISS_VIDEO_CONTROL_BAR);
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
			case QingXinConstants.MSG_SET_VIDEO_VIEW_TRANSPARENT:
				setVideoViewTranslate();
				break;

			case QingXinConstants.MSG_DISMISS_VIDEO_CONTROL_BAR:
				VideoViewHolder holder = (VideoViewHolder)msg.obj;
				showVideoControlBar(holder, false);
				break;
			}
		};
	};
	
	private void setVideoViewTranslate(){
		if(mVideoView != null){
			mVideoView.setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	private int mProgress;
	public void setProgress(int pos){
		mProgress = pos;
	}
	
	private void seekTo(int pos){
		if(mHolder != null && mHolder.mediaControl != null){
			mHolder.mediaControl.seekTo(pos);
		}
	}

	public static class VideoViewHolder{
		public WylMediaControl mediaControl;
		public ImageView playPauseIb;
		public SeekBar seekBar;
		public ProgressBar pb;
		public SimpleDraweeView imgIv;
		public ImageView playSymbolIv;
		public RelativeLayout videoRl;
		public ImageButton halfFullIb;
		public ImageView backIb;
		public TextView currentTimeTv,totalTimeTv;
		
		public VideoViewHolder(View convertView){
			mediaControl = convertView.findViewById(R.id.media_control);
			playPauseIb = convertView.findViewById(R.id.mediacontroller_play_pause);
			seekBar =  convertView.findViewById(R.id.mediacontroller_seekbar);
			pb =  convertView.findViewById(R.id.loading_pb);
			imgIv = convertView.findViewById(R.id.video_image_iv);
			playSymbolIv = convertView.findViewById(R.id.play_symbol_iv);
			videoRl = convertView.findViewById(R.id.video_rl);
			halfFullIb = convertView.findViewById(R.id.mediacontroller_half_full_ib);
			currentTimeTv = convertView.findViewById(R.id.mediacontroller_time_current);
			totalTimeTv = convertView.findViewById(R.id.mediacontroller_time_total);
		}
	}
	
	private OnVideoControlListener mOnVideoControlListener;
	public void setOnVideoControlListener(OnVideoControlListener listener){
		mOnVideoControlListener = listener;
	}
	public interface OnVideoControlListener{
		void onClickPlay();
		void onPrepared();
		void onClickResume();
		void onClickPause();
		void onCompletion();
		void onClickHalfFullScreen();
		void onError(int code, String msg);
	}

	public static class OnVideoControlProxy implements OnVideoControlListener{
		@Override
		public void onClickPlay() {
		}
		@Override
		public void onPrepared() {
		}
		@Override
		public void onClickResume() {
		}
		@Override
		public void onClickPause() {
		}
		@Override
		public void onCompletion() {
		}
		@Override
		public void onClickHalfFullScreen() {
		}
		@Override
		public void onError(int code, String msg) {
		}
	}
}
