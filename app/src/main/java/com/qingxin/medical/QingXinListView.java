package com.qingxin.medical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.vlee78.android.vl.VLAnimation;
import com.vlee78.android.vl.VLAsyncHandler;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLListView;
import com.vlee78.android.vl.VLListView.VLListHeader;
import com.vlee78.android.vl.VLScheduler;

public class QingXinListView {

    public interface QingXinListViewDelegate {
        void onLoadMore(VLListView listView, boolean isClear, VLAsyncHandler<Object> asyncHandler);

        void onEmpty(VLListView listView);
    }

    private Context mContext;
    private VLListView mListView;
    private QingXinListViewDelegate mDelegate;

    public QingXinListView(VLListView listView, QingXinListViewDelegate delegate) {
        mContext = listView.getContext();
        mListView = listView;
        mDelegate = delegate;
        mListView.listView().setDivider(null);
        mListView.setListHeader(new I90AgentListHeader());
        mListView.setListFooter(new I90AgentListFooter());
        update();
    }

    public QingXinListView(VLListView listView, QingXinListViewDelegate delegate, boolean isHeadShow, boolean isFooterShow) {
        mContext = listView.getContext();
        mListView = listView;
        mDelegate = delegate;
        mListView.listView().setDivider(null);
        if (isHeadShow) mListView.setListHeader(new I90AgentListHeader());
        if (isFooterShow) mListView.setListFooter(new I90AgentListFooter());
        update();
    }

    public QingXinListView(VLListView listView, QingXinListViewDelegate delegate, boolean isShowLoading) {
        mContext = listView.getContext();
        mListView = listView;
        mDelegate = delegate;
        mListView.listView().setDivider(null);
        mListView.setListHeader(new I90AgentListHeader());
        mListView.setListFooter(new I90AgentListFooter());
        update(isShowLoading);
    }

    public void update() {
        update(true, true, VLListView.RELOAD_SCROLL_TOP, null);
    }

    public void update(boolean isShowLoding) {
        update(true, isShowLoding, VLListView.RELOAD_SCROLL_TOP, null);
    }

    private void update(final boolean clear, final boolean showLoading, final int reloadScrollType, final VLAsyncHandler<Object> asyncHandler) {
        if (showLoading) {
            View view = mListView.showOccupyView(R.layout.group_loading);
            VLAnimation.rotate(view.findViewById(R.id.loadingImage), 1000);
        }
        mDelegate.onLoadMore(mListView, clear, new VLAsyncHandler<Object>(this, VLScheduler.THREAD_MAIN) {
            @Override
            protected void handler(boolean succeed) {
                if (showLoading) mListView.hideOccupyView();
                if (!succeed) {
                    if (showLoading) {
                        View view = mListView.showOccupyView(R.layout.group_loadfailed);
                        view.findViewById(R.id.loadfailedImage).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mListView.hideOccupyView();
                                update(clear, true, reloadScrollType, null);
                            }
                        });
                    }
                    if (asyncHandler != null) asyncHandler.handlerError(getRes(), getStr());
                    return;
                }
                mListView.dataCommit(reloadScrollType);
                if (mListView.dataGetCount() == 0) {
                    mDelegate.onEmpty(mListView);
                }

                if (asyncHandler != null) asyncHandler.handlerSuccess(null);
            }
        });
    }

    private class I90AgentListHeader extends VLListHeader {
        private ImageView mImageView;
        private TextView mTextView;

        @Override
        public void onCreate(LayoutInflater inflater, ViewGroup root) {
            View view = inflater.inflate(R.layout.group_header_footer, root);
            mImageView = view.findViewById(R.id.headerFooterImage);
            mImageView.setImageResource(R.mipmap.ic_header_pull);
            mTextView = view.findViewById(R.id.headerFooterText);
            mTextView.setText("下拉可以刷新");
        }

        @Override
        public void onStateChanged(int from, int to) {
            if (from == VLListHeader.STATE_NORMAL && to == VLListHeader.STATE_READY) {
                mTextView.setText("释放立即刷新");
                mImageView.clearAnimation();
                mImageView.setImageResource(R.mipmap.ic_header_push);
                VLAnimation.rotate(mImageView, 0, 180, 400);
            }
            if (from == VLListHeader.STATE_READY && to == VLListHeader.STATE_NORMAL) {
                mTextView.setText("下拉可以刷新");
                mImageView.clearAnimation();
                mImageView.setImageResource(R.mipmap.ic_header_pull);
                VLAnimation.rotate(mImageView, -180, 0, 400);
            }
            if (to == VLListHeader.STATE_LOADING) {
                mTextView.setText("刷新加载中..");
                mImageView.clearAnimation();
                mImageView.setImageResource(R.mipmap.ic_header_loading);
                VLAnimation.rotate(mImageView, 1000);
                update(true, false, VLListView.RELOAD_SCROLL_TOP, new VLAsyncHandler<Object>(mContext, VLScheduler.THREAD_MAIN, 2000) {
                    @Override
                    protected void handler(boolean succeed) {
                        if (!succeed) {
                            mTextView.setText("数据加载失败,请检查网络重试");
                            mImageView.clearAnimation();
                            mImageView.setImageResource(R.mipmap.ic_header_fresh);
                        } else {
                            mTextView.setText("数据刷新成功");
                            mImageView.clearAnimation();
                            mImageView.setImageResource(R.mipmap.ic_header_fresh);
                        }
                        VLScheduler.instance.schedule(1000, VLScheduler.THREAD_MAIN, new VLBlock() {
                            @Override
                            protected void process(boolean canceled) {
                                reset();
                                mTextView.setText("下拉可以刷新");
                                mImageView.clearAnimation();
                                mImageView.setImageResource(R.mipmap.ic_header_pull);
                            }
                        });
                    }
                });
            }
        }
    }

    private class I90AgentListFooter extends VLListView.VLListFooter {
        private ImageView mImageView;
        private TextView mTextView;

        @Override
        public void onCreate(LayoutInflater inflater, ViewGroup root) {
            View view = inflater.inflate(R.layout.group_header_footer, root);
            mImageView = view.findViewById(R.id.headerFooterImage);
            mImageView.setImageResource(R.mipmap.ic_header_push);
            mTextView = view.findViewById(R.id.headerFooterText);
            mTextView.setText("上拉加载更多");
        }

        @Override
        public void onStateChanged(int from, int to) {
            if (from == VLListHeader.STATE_NORMAL && to == VLListHeader.STATE_READY) {
                mTextView.setText("释放加载更多");
                mImageView.clearAnimation();
                mImageView.setImageResource(R.mipmap.ic_header_pull);
                VLAnimation.rotate(mImageView, 0, 180, 400);
            }
            if (from == VLListHeader.STATE_READY && to == VLListHeader.STATE_NORMAL) {
                mTextView.setText("上拉加载更多");
                mImageView.clearAnimation();
                mImageView.setImageResource(R.mipmap.ic_header_push);
                VLAnimation.rotate(mImageView, -180, 0, 400);
            }
            if (to == VLListHeader.STATE_LOADING) {
                mTextView.setText("加载更多中..");
                mImageView.clearAnimation();
                mImageView.setImageResource(R.mipmap.ic_header_loading);
                VLAnimation.rotate(mImageView, 1000);
                update(false, false, VLListView.RELOAD_PRESERVE_LAST_POS, new VLAsyncHandler<Object>(mContext, VLScheduler.THREAD_MAIN, 1000) {
                    @Override
                    protected void handler(boolean succeed) {
                        if (!succeed) {
                            mTextView.setText("数据加载失败,请检查网络重试");
                            mImageView.clearAnimation();
                            mImageView.setImageResource(R.mipmap.ic_header_fresh);
                            VLScheduler.instance.schedule(1000, VLScheduler.THREAD_MAIN, new VLBlock() {
                                @Override
                                protected void process(boolean canceled) {
                                    reset();
                                    mTextView.setText("上拉加载更多");
                                    mImageView.clearAnimation();
                                    mImageView.setImageResource(R.mipmap.ic_header_push);
                                }
                            });
                        } else {
                            reset();
                            mTextView.setText("上拉加载更多");
                            mImageView.clearAnimation();
                            mImageView.setImageResource(R.mipmap.ic_header_push);
                        }
                    }
                });
            }
        }
    }
}
