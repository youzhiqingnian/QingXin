package com.vlee78.android.vl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

public class VLImagePlayView extends FrameLayout {

    /**
     * 监听ViewPager屏幕单击事件，本质是监听子控件MatrixImageView的单击事件
     */
    public interface OnSingleTapListener {
        void onSingleTap();
    }

    /**
     * MatrixImageView移动监听接口, 用以组织ViewPager对Move操作的拦截
     */
    public interface OnMovingListener {
        void startDrag();

        void stopDrag();
    }

    public void setOnMovingListener(OnMovingListener onMovingListener) {
        if (mVLPhotoView != null) {
            mVLPhotoView.setOnMovingListener(onMovingListener);
        }
    }

    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        if (mVLPhotoView != null) {
            mVLPhotoView.setOnSingleTapListener(onSingleTapListener);
        }
    }

    private VLPhotoView mVLPhotoView;
    private ImageView mPlaceImageView;

    public VLImagePlayView(Context context) {
        super(context);
        mPlaceImageView = new ImageView(context);
        addView(mPlaceImageView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public VLImagePlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPlaceImageView = new ImageView(context);
        addView(mPlaceImageView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void setPlaceImage(Drawable drawable) {
        mPlaceImageView.setImageDrawable(drawable);
    }

    String mUriKey;

    public void setImageDrawable(Bitmap drawable, String key) {
        mUriKey = key;
        if (null != drawable) {
            VLDebug.logD("VLImagePlayView setImageDrawable drawable=" + drawable.isRecycled());
            if (drawable.isRecycled()) {
                return;
            }
        }
        mVLPhotoView = new VLPhotoView(getContext());
        release();
        removeAllViews();
        addView(mVLPhotoView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mVLPhotoView.setImageBitmap(drawable);
    }

    public void release() {
        View view = getChildAt(0);
        removeView(view);
        if (view != null) {
            ImageView imageView = (ImageView) view;

            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            if (bitmapDrawable != null) {
                imageView.setImageDrawable(null);
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.evictFromMemoryCache(Uri.parse(mUriKey));
                bitmapDrawable.getBitmap().recycle();
                VLDebug.logD("VLImagePlayView >> recycle");
            }
        }
    }
}
