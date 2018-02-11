package com.qingxin.medical.home.districtsel.video;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.VideoView;
import com.qingxin.medical.R;

public class WylVideoView extends VideoView {

	private boolean mIsFullScreen = false;
	private boolean mIsFullScreenMode = false;
	private int videoWidth;
	private int videoHeight;
	
	public WylVideoView(Context context) {
		super(context);
	}

	public WylVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		init(context, attrs);
	}

	public WylVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs){
		initAttrs(context, attrs);
		initGesture(context);
	}
	
	public void setIsFullScreenMode(boolean isFull){
		mIsFullScreenMode = isFull;
//		mIsFullScreen = isFull;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width;
		int height;
		if(mIsFullScreenMode){
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}else{
			if(mIsFullScreen){
		        width = getDefaultSize(0, widthMeasureSpec);  
		        height = getDefaultSize(0, heightMeasureSpec);  
		        setMeasuredDimension(width, height); 
			}else{
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}	
		}
	}
	
    private void initAttrs(Context context, AttributeSet attrs){
    	TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomVideoView);
    	mIsFullScreen = ta.getBoolean(R.styleable.CustomVideoView_fullScreen, false);
    	ta.recycle();
    }
    
	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int videoWidth) {
		this.videoWidth = videoWidth;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int videoHeight) {
		this.videoHeight = videoHeight;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		return true;
	}
	
    private GestureDetector gestureDetector;
	private void initGesture(Context context){
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
        	@Override
        	public boolean onDoubleTap(MotionEvent e) {
        		if(mGestureListener != null){
        			mGestureListener.onDoubleTap();
        		}
        		return super.onDoubleTap(e);
        	}
        	
        	@Override
        	public boolean onSingleTapConfirmed(MotionEvent e) {
        		if(mGestureListener != null){
        			mGestureListener.onSingleTapConfirmed();
        		}
        		return super.onSingleTapConfirmed(e);
        	}
        });
	}
	
	private OnGestureListener mGestureListener;
	public void setOnGestureListener(OnGestureListener listener){
		mGestureListener = listener;
	}
	
	public interface OnGestureListener{
		void onDoubleTap();
		void onSingleTapConfirmed();
	}
}
