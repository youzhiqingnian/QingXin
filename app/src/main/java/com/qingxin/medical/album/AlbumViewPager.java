package com.qingxin.medical.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLDebug;
import com.vlee78.android.vl.VLImagePlayView;
import com.vlee78.android.vl.VLScheduler;
import java.util.List;


public class AlbumViewPager extends ViewPager implements VLImagePlayView.OnMovingListener {

    public final static String TAG = "AlbumViewPager";

    /**
     * 当前子控件是否处理拖动状态
     */
    private boolean mChildIsBeingDragged = false;

    /**
     * 界面单击事件 用以显示和隐藏菜单栏
     */
    private VLImagePlayView.OnSingleTapListener onSingleTapListener;

    /**
     * 播放按钮点击事件
     */
    public AlbumViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return !mChildIsBeingDragged && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void startDrag() {
        mChildIsBeingDragged = true;
    }

    @Override
    public void stopDrag() {
        mChildIsBeingDragged = false;
    }

    public void setOnSingleTapListener(VLImagePlayView.OnSingleTapListener onSingleTapListener) {
        this.onSingleTapListener = onSingleTapListener;
    }

    public class LocalViewPagerAdapter extends PagerAdapter {
        private List<LocalImageHelper.LocalFile> paths;//大图地址 如果为网络图片 则为大图url

        LocalViewPagerAdapter(List<LocalImageHelper.LocalFile> paths) {
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int position) {
            //注意，这里不可以加inflate的时候直接添加到viewGroup下，而需要用addView重新添加
            //因为直接加到viewGroup下会导致返回的view为viewGroup
            View imageLayout = inflate(getContext(), R.layout.adapter_album_pager, null);
            viewGroup.addView(imageLayout);
            assert imageLayout != null;
            VLImagePlayView imageView = imageLayout.findViewById(R.id.image);
            imageView.setOnMovingListener(AlbumViewPager.this);
            imageView.setOnSingleTapListener(onSingleTapListener);
            LocalImageHelper.LocalFile path = paths.get(position);

            ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path.getOriginalUri())).setRotationOptions(RotationOptions.autoRotate()).build();
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, imageView);
            dataSource.subscribe(new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
                @Override
                protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                    CloseableReference<CloseableImage> imageReference = dataSource.getResult();
                    if (imageReference != null) {
                        try {
                            CloseableImage image = imageReference.get();
                            if (image != null && image instanceof CloseableStaticBitmap) {
                                CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
                                final Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
                                VLDebug.logD("displayVLImageView result=" + bitmap);
                                if (bitmap != null) {
                                    VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                                        protected void process(boolean canceled) {
                                            imageView.setImageDrawable(bitmap, path.getOriginalUri());
                                        }
                                    });
                                }
                            }
                        } finally {
                            imageReference.close();
                            CloseableReference.closeSafely(imageReference);
                        }
                    }
                }

                @Override
                protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {

                }
            }, CallerThreadExecutor.getInstance());
            return imageLayout;
        }

        @Override
        public int getItemPosition(Object object) {
            //在notifyDataSetChanged时返回None，重新绘制
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int arg1, Object object) {
            container.removeView((View) object);
            VLImagePlayView imageView = ((View) object).findViewById(R.id.image);
            imageView.release();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}