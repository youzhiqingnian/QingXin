package com.vlee78.android.vl;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VLListView extends RelativeLayout {

    public VLListView(Context context) {
        super(context);
        init();
    }

    public VLListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VLListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public View showOccupyView(int layoutResId) {
        if (mOccupyView != null) hideOccupyView();
        mOccupyView = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
        mOccupyView.setLayoutParams(VLUtils.paramsRelative(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        addView(mOccupyView);
        return mOccupyView;
    }

    public View showOccupyView(View view) {
        if (mOccupyView != null) hideOccupyView();
        mOccupyView = view;
        mOccupyView.setLayoutParams(VLUtils.paramsRelative(VLUtils.MATCH_PARENT, VLUtils.MATCH_PARENT));
        addView(mOccupyView);
        return mOccupyView;
    }

    public void hideOccupyView() {
        if (mOccupyView == null) return;
        removeView(mOccupyView);
        mOccupyView = null;
    }

    private void init() {
        mLayoutInflater = LayoutInflater.from(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mListView = new ListViewWrapper(getContext());
        addView(mListView, params);
        mListView.setTag(this);
        mListViewItems = new ArrayList<>();
        mListViewItemsTemp = new ArrayList<>();
        mListViewTypeClasses = new ArrayList<>();
        mListViewTypes = new ArrayList<>();
        mListViewAdapter = new VLListViewAdapter();
        mResetAdapterFlag = true;
        mListHeader = null;
        mListFooter = null;

        params = VLUtils.paramsRelative(VLUtils.MATCH_PARENT, VLUtils.WRAP_CONTENT);
        params.alignWithParent = true;
        params.addRule(ALIGN_PARENT_TOP);
        mFloatItem = new RelativeLayout(getContext());
        addView(mFloatItem, params);

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.alignWithParent = true;
        params.addRule(ALIGN_PARENT_TOP);
        mFloatHeader = new RelativeLayout(getContext());
        addView(mFloatHeader, params);

        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.alignWithParent = true;
        params.addRule(ALIGN_PARENT_BOTTOM);
        mFloatFooter = new RelativeLayout(getContext());
        addView(mFloatFooter, params);

        mOccupyView = null;
    }

    public void setFloatItemPositions(final int[] floatItemPositions) {
        mListView.setOnScrollListener(new OnScrollListener() {
            private int mFloatItemPosition = -1;
            private int[] mFloatItemPositions = floatItemPositions;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                VLDebug.logE("onScroll firstVisibleItem=" + firstVisibleItem + ", visibleItemCount=" + visibleItemCount + ", totalItemCount=" + totalItemCount);
                int floatPos = -1;
                int headerCount = mListView.getHeaderViewsCount();
                int curPos = firstVisibleItem - headerCount;
                if (curPos >= 0) {
                    for (int i = 0; i < mFloatItemPositions.length; i++) {
                        int pos = mFloatItemPositions[i];
                        if (curPos < pos) {
                            floatPos = (i < 1 ? -1 : mFloatItemPositions[i - 1]);
                            break;
                        }
                        if (i == mFloatItemPositions.length - 1)
                            floatPos = mFloatItemPositions[mFloatItemPositions.length - 1];
                    }
                }
                if (floatPos < 0) {
                    if (mFloatItemPosition >= 0) {
                        setFloatItem(null);
                        mFloatItemPosition = -1;
                    }
                } else {
                    if (mFloatItemPosition != floatPos) {
                        setFloatItem(null);
                        mFloatItemPosition = floatPos;
                        View v = listView().getAdapter().getView(floatPos + 1, null, VLListView.this);
                        setFloatItem(v);
                    }
                }
            }
        });
    }

    public View setFloatHeader(int layoutId) {
        return mLayoutInflater.inflate(layoutId, mFloatHeader);
    }

    public void setFloatItem(View view) {
        if (view == null) mFloatItem.removeAllViews();
        else mFloatItem.addView(view);
    }

    public View setFloatFooter(int layoutId) {
        return mLayoutInflater.inflate(layoutId, mFloatFooter);
    }

    public interface OnComputeScrollListener {
        void onComputeScroll();
    }

    public static class ListViewWrapper extends ListView {
        private OnComputeScrollListener mHeaderOnComputeScrollListener = null;
        private OnComputeScrollListener mFooterOnComputeScrollListener = null;
        private OnTouchListener mHeaderOnTouchListener = null;
        private OnTouchListener mFooterOnTouchListener = null;
        boolean mIsWrapContent = false;

        public void setWrapContent(boolean isWrapContent) {
            mIsWrapContent = isWrapContent;
        }

        @SuppressLint("ClickableViewAccessibility")
        public ListViewWrapper(Context context) {
            super(context);
            setOnTouchListener(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (mHeaderOnTouchListener != null)
                        mHeaderOnTouchListener.onTouch(view, motionEvent);
                    if (mFooterOnTouchListener != null)
                        mFooterOnTouchListener.onTouch(view, motionEvent);
                    return view.onTouchEvent(motionEvent);
                }
            });
        }

        public void setHeaderOnTouchListener(OnTouchListener headerTouchListener) {
            mHeaderOnTouchListener = headerTouchListener;
        }

        public void setFooterOnTouchListener(OnTouchListener footerTouchListener) {
            mFooterOnTouchListener = footerTouchListener;
        }

        public ListViewWrapper(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ListViewWrapper(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (mIsWrapContent) {
                int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);
            } else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        @Override
        public void computeScroll() {
            super.computeScroll();
            if (mHeaderOnComputeScrollListener != null)
                mHeaderOnComputeScrollListener.onComputeScroll();
            if (mFooterOnComputeScrollListener != null)
                mFooterOnComputeScrollListener.onComputeScroll();
        }

        public void setHeaderOnComputeScrollListener(OnComputeScrollListener headerOnComputeScrollListener) {
            mHeaderOnComputeScrollListener = headerOnComputeScrollListener;
        }

        public void setFooterOnComputeScrollListener(OnComputeScrollListener footerOnComputeScrollListener) {
            mFooterOnComputeScrollListener = footerOnComputeScrollListener;
        }
    }

    private LayoutInflater mLayoutInflater;
    private ListViewWrapper mListView;
    private List<VLListViewItem<?>> mListViewItems;
    private List<VLListViewItem<?>> mListViewItemsTemp;
    private List<Class<? extends VLListViewType<?>>> mListViewTypeClasses;
    private List<VLListViewType<?>> mListViewTypes;
    private VLListViewAdapter mListViewAdapter;
    private boolean mResetAdapterFlag;
    private VLListHeader mListHeader;
    private VLListFooter mListFooter;
    private RelativeLayout mFloatHeader;
    private RelativeLayout mFloatFooter;
    private RelativeLayout mFloatItem;
    private View mOccupyView;
    private VLListViewTypeAction mVLListViewTypeAction;


    public void setVLListViewTypeAction(VLListViewTypeAction listViewTypeAction) {
        mVLListViewTypeAction = listViewTypeAction;
    }

    public void notifyAction(int action, int position, Object... args) {
        mVLListViewTypeAction.onAction(action, position, args);
    }


    public interface VLListViewType<T> {
        View onViewCreate(VLListView listView, int position, LayoutInflater inflater, ViewGroup parent, T data);

        void onViewUpdate(VLListView listView, int position, View view, T data, Object id);
    }

    public interface VLListViewTypeAction {
        /**
         * @param action   关注与业务逻辑的指令 如打电话, 发email，点赞 等谓词；
         * @param position 点击的位置
         * @param args     扩展参数
         */
        void onAction(int action, int position, Object... args);
    }


    public static class VLListViewItem<T> {
        public int mTypeId;
        public T mData;
        public Object mId;
    }

    public VLListHeader getListHeader() {
        return mListHeader;
    }

    public VLListFooter getListFooter() {
        return mListFooter;
    }

    public void setListHeader(VLListHeader listHeader) {
        VLDebug.Assert(mListHeader == null);
        mListHeader = listHeader;
        mListHeader.init(getContext(), this);
    }

    public void setListFooter(VLListFooter listFooter) {
        VLDebug.Assert(mListFooter == null);
        mListFooter = listFooter;
        mListFooter.init(getContext(), this);
    }

    public ListViewWrapper listView() {
        return mListView;
    }

    public int registerType(Class<? extends VLListViewType<?>> cls) {
        mListViewTypeClasses.add(cls);
        VLListViewType<?> type = VLUtils.classNew(cls);
        VLDebug.Assert(type != null);
        mListViewTypes.add(type);
        mResetAdapterFlag = true;
        return mListViewTypes.size() - 1;
    }

    public void dataClear() {
        mListViewItemsTemp.clear();
    }

    public int dataGetCount() {
        return mListViewItemsTemp.size();
    }

    public void dataClearAndRestAdapter() {
        mListViewItemsTemp.clear();
        mListViewAdapter = new VLListViewAdapter();
        mResetAdapterFlag = true;
    }

    public List<VLListViewItem<?>> datGetAll() {
        return mListViewItemsTemp;
    }

    public <V> V dataGetAt(int index) {
        if (index < 0 || index >= mListViewItems.size()) return null;
        VLListViewItem<?> item = mListViewItems.get(index);
        @SuppressWarnings("unchecked")
        V data = (V) item.mData;
        return data;
    }

    public <V> V dataGetHead() {
        if (mListViewItems.size() == 0) return null;
        VLListViewItem<?> item = mListViewItems.get(0);
        @SuppressWarnings("unchecked")
        V data = (V) item.mData;
        return data;
    }

    public <V> V dataGetTail() {
        if (mListViewItems.size() == 0) return null;
        VLListViewItem<?> item = mListViewItems.get(mListViewItems.size() - 1);
        @SuppressWarnings("unchecked")
        V data = (V) item.mData;
        return data;
    }

    public <K extends VLListViewType<V>, V> void dataAddTail(Class<K> type, V data) {
        VLListViewItem<V> item = new VLListViewItem<>();
        item.mTypeId = getViewTypeId(type);
        item.mData = data;
        item.mId = null;
        mListViewItemsTemp.add(item);
    }

    public <K extends VLListViewType<V>, V> void dataAddTailAt(Class<K> type, V data, int index) {
        VLListViewItem<V> item = new VLListViewItem<>();
        item.mTypeId = getViewTypeId(type);
        item.mData = data;
        item.mId = null;
        mListViewItemsTemp.add(index, item);
    }

    public <K extends VLListViewType<V>, V> void dataSetTailAt(Class<K> type, V data, int index) {
        @SuppressWarnings("unchecked")
        VLListViewItem<V> item = (VLListViewItem<V>) mListViewItemsTemp.get(index);
        if (item != null) {
            item.mData = data;
            mListViewItemsTemp.set(index, item);
        }
    }

    public <K extends VLListViewType<V>, V> void dataAddListTail(Class<K> type, List<V> data) {
        for (int i = 0; i < data.size(); i++) {
            VLListViewItem<V> item = new VLListViewItem<>();
            item.mTypeId = getViewTypeId(type);
            item.mData = data.get(i);
            item.mId = null;
            mListViewItemsTemp.add(item);
        }
    }

    public <K extends VLListViewType<V>, V> void dataSetAt(Class<K> type, V data, int index) {
        if (index < 0 || index >= mListViewItemsTemp.size())
            return;
        @SuppressWarnings("unchecked")
        VLListViewItem<V> item = (VLListViewItem<V>) mListViewItemsTemp.get(index);
        item.mData = data;
        mListViewItemsTemp.set(index, item);
    }

    public void dataDelAt(int index) {
        if (index < 0 || index >= mListViewItemsTemp.size())
            return;
        mListViewItemsTemp.remove(index);
    }

    public <K extends VLListViewType<V>, V> void dataAddTail(Class<K> type, V data, Object id) {
        VLListViewItem<V> item = new VLListViewItem<>();
        item.mTypeId = getViewTypeId(type);
        item.mData = data;
        item.mId = id;
        mListViewItemsTemp.add(item);
    }

    public static final int RELOAD_SCROLL_TOP = 0;
    public static final int RELOAD_SCROLL_BOTTOM = 1;
    public static final int RELOAD_PRESERVE_FIRST_POS = 2;
    public static final int RELOAD_PRESERVE_LAST_POS = 3;

    private int mReloadItemTop;
    private List<Object> mReloadItemIds = new ArrayList<>();

    private boolean preserveItemIndexHeight() {
        mReloadItemTop = 0;
        mReloadItemIds.clear();

        int childCount = mListView.getChildCount();
        if (childCount == 0)
            return false;
        for (int i = 0; i < childCount; i++) {
            View child = mListView.getChildAt(i);
            int itemTop = child.getTop();
            if (itemTop < 0)
                continue;

            int firstIndex = mListView.getFirstVisiblePosition();// 获取当前可见的第一个Item的position
            int headerCount = mListView.getHeaderViewsCount();
            int itemIndex = i + firstIndex - headerCount;
            if (itemIndex < 0 || itemIndex >= mListViewItems.size())
                continue;

            VLListViewItem<?> item = mListViewItems.get(itemIndex);
            if (item.mId == null)
                continue;

            mReloadItemTop = itemTop;
            mReloadItemIds.add(item.mId);
            for (int j = itemIndex + 1; j < mListViewItems.size(); j++) {
                item = mListViewItems.get(itemIndex);
                if (item.mId == null)
                    continue;
                mReloadItemIds.add(item.mId);
            }
            return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean revertItemIndexHeight() {
        for (Object reloadItemId : mReloadItemIds) {
            for (int i = 0; i < mListViewItems.size(); i++) {
                VLListViewItem<?> item = mListViewItems.get(i);
                if (item.mId == null || !item.mId.equals(reloadItemId))
                    continue;

                int position = i + mListView.getHeaderViewsCount();
                mListView.setSelectionFromTop(position, mReloadItemTop);
                mReloadItemIds.clear();
                mReloadItemTop = 0;
                return true;
            }
        }
        mReloadItemIds.clear();
        mReloadItemTop = 0;
        return false;
    }

    public void dataCommit(final int reloadType) {
        if (reloadType == RELOAD_PRESERVE_FIRST_POS || reloadType == RELOAD_PRESERVE_LAST_POS)
            preserveItemIndexHeight();

        if (VLUtils.threadInMain()) {
            mListViewItems.clear();
            mListViewItems.addAll(mListViewItemsTemp);
            mListViewAdapter.notifyDataSetChanged();
            if (mResetAdapterFlag) {
                mListView.setAdapter(mListViewAdapter);
                mResetAdapterFlag = false;
            }
            if (reloadType == RELOAD_PRESERVE_FIRST_POS || reloadType == RELOAD_PRESERVE_LAST_POS)
                revertItemIndexHeight();
            else if (reloadType == RELOAD_SCROLL_BOTTOM)
                scrollToEnd();
            else if (reloadType == RELOAD_SCROLL_TOP)
                mListView.setAdapter(mListViewAdapter);
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    mListViewItems.clear();
                    mListViewItems.addAll(mListViewItemsTemp);
                    mListViewAdapter.notifyDataSetChanged();
                    if (mResetAdapterFlag) {
                        mListView.setAdapter(mListViewAdapter);
                        mResetAdapterFlag = false;
                    }
                    if (reloadType == RELOAD_PRESERVE_FIRST_POS || reloadType == RELOAD_PRESERVE_LAST_POS)
                        revertItemIndexHeight();
                    else if (reloadType == RELOAD_SCROLL_BOTTOM)
                        scrollToEnd();
                    else if (reloadType == RELOAD_SCROLL_TOP)
                        mListView.setAdapter(mListViewAdapter);
                }
            });
        }
    }

    public void setStringItems(int count) {
        dataClear();
        for (int i = 0; i < count; i++)
            dataAddTail(VLDummyStringType.class, "string item " + i);
        dataCommit(RELOAD_PRESERVE_FIRST_POS);
    }

    public void notifyDataSetChanged() {
        if (VLUtils.threadInMain()) {
            mListViewAdapter.notifyDataSetChanged();
        } else {
            VLScheduler.instance.schedule(0, VLScheduler.THREAD_MAIN, new VLBlock() {
                @Override
                protected void process(boolean canceled) {
                    mListViewAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private <K extends VLListViewType<V>, V> int getViewTypeId(Class<K> cls) {
        for (int i = mListViewTypeClasses.size() - 1; i >= 0; i--) {
            if (mListViewTypeClasses.get(i) == cls) {
                return i;
            }
        }
        return registerType(cls);
    }

    private class VLListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mListViewItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mListViewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            int count = mListViewTypes.size();
            if (count <= 0)
                count = 1;
            return count;
        }

        @Override
        public int getItemViewType(int position) {
            return mListViewItems.get(position).mTypeId;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VLListViewItem item = mListViewItems.get(position);
            VLListViewType type = mListViewTypes.get(item.mTypeId);
            if (convertView == null)
                convertView = type.onViewCreate(VLListView.this, position, mLayoutInflater, parent, item.mData);
            type.onViewUpdate(VLListView.this, position, convertView, item.mData, item.mId);
            return convertView;
        }
    }

    public static class VLRawDelegateType implements VLListViewType<VLListViewType<Object>> {
        @Override
        public View onViewCreate(VLListView listView, int position, LayoutInflater inflater, ViewGroup parent, VLListViewType<Object> data) {
            View view = null;
            if (data != null)
                view = data.onViewCreate(listView, position, inflater, parent, data);
            return view;
        }

        @Override
        public void onViewUpdate(VLListView listView, int position, View view, VLListViewType<Object> data, Object id) {
            if (data != null)
                data.onViewUpdate(listView, position, view, data, id);
        }
    }

    public static class VLDummyHeightType implements VLListViewType<Integer> {
        @Override
        public View onViewCreate(VLListView listView, int position, LayoutInflater inflater, ViewGroup parent, Integer data) {
            return new View(listView.getContext());
        }

        @Override
        public void onViewUpdate(VLListView listView, int position, View view, Integer data, Object id) {
            int height = data;
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, height);
            view.setLayoutParams(params);
        }
    }

    @SuppressLint("RtlHardcoded")
    public static class VLDummyStringType implements VLListViewType<String> {
        private int mLayoutHeight = AbsListView.LayoutParams.WRAP_CONTENT;
        private int mGravity = Gravity.LEFT;

        public void setHeight(int height) {
            mLayoutHeight = height;
        }

        public void setGravity(int gravity) {
            mGravity = gravity;
        }

        @Override
        public View onViewCreate(VLListView listView, int position, LayoutInflater inflater, ViewGroup parent, String data) {
            TextView tv = new TextView(listView.getContext());
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, mLayoutHeight);
            tv.setLayoutParams(params);
            tv.setGravity(mGravity);
            return tv;
        }

        @Override
        public void onViewUpdate(VLListView listView, int position, View view, String data, Object id) {
            TextView tv = (TextView) view;
            tv.setText(data);
        }
    }

    public static abstract class VLListHeader {
        private Context mContext;
        private VLListView mVLListView;
        private ListViewWrapper mListView;
        private boolean mContentOccupy;
        private boolean mContentDisplay;
        private boolean mInited;

        private Scroller mScroller;
        private LinearLayout mListHeader;
        private LinearLayout mContent;
        private int mContentHeight;
        private LinearLayout mContainer;
        private int mState;
        private boolean mScrolling;

        public static final int STATE_INIT = 0;
        public static final int STATE_NORMAL = 1;
        public static final int STATE_READY = 2;
        public static final int STATE_LOADING = 3;

        public abstract void onCreate(LayoutInflater inflater, ViewGroup root);

        public abstract void onStateChanged(int from, int to);

        protected Context getContext() {
            return mContext;
        }

        public void onPushHeight(int height) {
        }

        public int getContentHeight() {
            return mContentHeight;
        }

        public VLListHeader() {
            mContext = null;
            mListView = null;
            mContentOccupy = false;
            mContentDisplay = true;
            mInited = false;
            mScroller = null;
            mListHeader = null;
            mContent = null;
            mContentHeight = 0;
            mContainer = null;
            mState = STATE_INIT;
            mScrolling = false;
        }

        @SuppressLint("ClickableViewAccessibility")
        protected void init(Context context, VLListView vllistview) {
            VLDebug.Assert(!mInited);
            mContext = context;
            mVLListView = vllistview;
            mListView = vllistview.mListView;

            mScroller = new Scroller(mContext, new DecelerateInterpolator());
            mListHeader = new LinearLayout(mContext);

            LinearLayout.LayoutParams params;

            mContent = new LinearLayout(context);
            mContent.setOrientation(LinearLayout.VERTICAL);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mContent.setLayoutParams(params);

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            onCreate(layoutInflater, mContent);

            mContent.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mContentHeight = mContent.getMeasuredHeight();

            int containerHeight = 0;
            if (mContentOccupy)
                containerHeight = mContentHeight;
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, containerHeight);
            mContainer = new LinearLayout(context);
            mContainer.setOrientation(LinearLayout.VERTICAL);
            mContainer.setGravity(Gravity.BOTTOM);
            mListHeader.addView(mContainer, params);

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mContentHeight);
            mContainer.addView(mContent, params);

            mListView.addHeaderView(mListHeader);
            mListView.setHeaderOnComputeScrollListener(new OnComputeScrollListener() {
                @Override
                public void onComputeScroll() {
                    if (mScroller.computeScrollOffset()) {
                        if (mScrolling) {
                            int containerHeight = mScroller.getCurrY();
                            setContainerHeight(containerHeight);
                            mListView.postInvalidate();
                        }
                    } else
                        mScrolling = false;
                }
            });

            mListView.setHeaderOnTouchListener(new OnTouchListener() {
                private int mLastY = -1;

                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    int rawY = (int) ev.getRawY();
                    if (mLastY == -1)
                        mLastY = rawY;
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastY = rawY;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int deltaY = (rawY - mLastY);
                            mLastY = rawY;
                            int firstVisiblePosition = mListView.getFirstVisiblePosition();
                            int firstContentPosition = (mVLListView.mListHeader == null ? 0 : 1);
                            if (firstVisiblePosition <= firstContentPosition) {
                                int containerHeight = getContainerHeight();
                                if (!mContentOccupy) {// header隐藏
                                    VLDebug.Assert(containerHeight >= 0);
                                    containerHeight += deltaY;
                                    if (containerHeight < 0) {
                                        setContainerHeight(0);
                                        if (mState != STATE_LOADING)
                                            setState(STATE_NORMAL);
                                    } else {
                                        onPushHeight(containerHeight);
                                        setContainerHeight(containerHeight);
                                        if (mState != STATE_LOADING) {
                                            if (containerHeight > mContentHeight)
                                                setState(STATE_READY);
                                            else
                                                setState(STATE_NORMAL);
                                        }
                                    }
                                } else {// header常显示占位，containerHeight一定>=mContentHeight
                                    VLDebug.Assert(containerHeight >= mContentHeight);
                                    containerHeight += deltaY;
                                    if (containerHeight < mContentHeight) {
                                        setContainerHeight(mContentHeight);
                                        if (mState != STATE_LOADING)
                                            setState(STATE_NORMAL);
                                    } else {
                                        onPushHeight(containerHeight);
                                        setContainerHeight(containerHeight);
                                        if (mState != STATE_LOADING) {
                                            if (containerHeight > mContentHeight)
                                                setState(STATE_READY);
                                            else
                                                setState(STATE_NORMAL);
                                        }
                                    }
                                }
                            } else {
                                if (!mContentOccupy)
                                    setContainerHeight(0);
                                else
                                    setContainerHeight(mContentHeight);
                            }
                            break;
                        default:
                            mLastY = -1;// reset
                            firstVisiblePosition = mListView.getFirstVisiblePosition();
                            firstContentPosition = (mVLListView.mListHeader == null ? 0 : 1);
                            if (firstVisiblePosition <= firstContentPosition) {
                                int containerHeight = getContainerHeight();
                                if (!mContentOccupy) {
                                    VLDebug.Assert(containerHeight >= 0);
                                    if (containerHeight > 0) {
                                        if (containerHeight > mContentHeight) {
                                            mScroller.startScroll(0, containerHeight, 0, mContentHeight - containerHeight, 400);
                                            mScrolling = true;
                                            mListView.invalidate();
                                            if (mState != STATE_LOADING)
                                                setState(STATE_LOADING);
                                        } else {
                                            mScroller.startScroll(0, containerHeight, 0, 0 - containerHeight, 400);
                                            mScrolling = true;
                                            mListView.invalidate();
                                            if (mState != STATE_LOADING)
                                                setState(STATE_NORMAL);
                                        }
                                    }
                                } else {
                                    VLDebug.Assert(containerHeight >= mContentHeight);
                                    if (containerHeight > mContentHeight) {
                                        mScroller.startScroll(0, containerHeight, 0, mContentHeight - containerHeight, 400);
                                        mScrolling = true;
                                        mListView.invalidate();
                                        if (mState != STATE_LOADING)
                                            setState(STATE_LOADING);
                                    } else {
                                        if (mState != STATE_LOADING)
                                            setState(STATE_NORMAL);
                                    }
                                }
                            } else {
                                if (!mContentOccupy)
                                    setContainerHeight(0);
                                else
                                    setContainerHeight(mContentHeight);
                                if (mState != STATE_LOADING)
                                    setState(STATE_NORMAL);
                            }
                            break;
                    }
                    return false;
                }
            });
            setState(STATE_NORMAL);
            mInited = true;
        }

        public void setState(int state) {
            if (mState == state)
                return;
            int fromState = mState;
            mState = state;
            onStateChanged(fromState, mState);
        }

        private void setContainerHeight(int containerHeight) {
            VLDebug.Assert(containerHeight >= 0);
            if (mContentOccupy) {
                if (containerHeight < mContentHeight)
                    VLDebug.Assert(containerHeight >= mContentHeight);
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
            params.height = containerHeight;
            mContainer.setLayoutParams(params);
        }

        private int getContainerHeight() {
            return mContainer.getHeight();
        }

        public void setContentOccupy(boolean contentOccupy) {
            mContentOccupy = contentOccupy;
            reset();
        }

        public void setContentDisplay(boolean contentDisplay) {
            if (mContentDisplay == contentDisplay)
                return;
            mContentDisplay = contentDisplay;
            if (mContentDisplay) {
                mContent.setVisibility(View.VISIBLE);
                mContent.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mContentHeight = mContent.getMeasuredHeight();
            } else {
                mContent.setVisibility(View.GONE);
                mContentHeight = 0;
            }
            reset();
        }

        public void reset() {
            if (!mInited)
                return;
            setState(STATE_NORMAL);
            if (!mContentOccupy) {
                int containerHeight = getContainerHeight();
                if (containerHeight > 0) {
                    mScroller.startScroll(0, containerHeight, 0, 0 - containerHeight, 400);
                    mScrolling = true;
                    mListView.invalidate();
                } else
                    setContainerHeight(0);
            } else {
                int containerHeight = getContainerHeight();
                if (containerHeight > mContentHeight) {
                    mScroller.startScroll(0, containerHeight, 0, mContentHeight - containerHeight, 400);
                    mScrolling = true;
                    mListView.invalidate();
                } else
                    setContainerHeight(mContentHeight);
            }
        }
    }

    public void setEmptyView(View empty) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        empty.setVisibility(View.GONE);
        empty.setLayoutParams(params);
        ((ViewGroup) mListView.getParent()).addView(empty);
        mListView.setEmptyView(empty);
    }

    public static abstract class VLListFooter {
        private Context mContext;
        private VLListView mVLListView;
        private ListViewWrapper mListView;
        private boolean mContentOccupy;
        private boolean mContentDisplay;
        private boolean mInited;
        private Scroller mScroller;
        private LinearLayout mListFooter;
        private LinearLayout mContent;
        private int mContentHeight;
        private LinearLayout mContainer;
        private int mState;
        private boolean mScrolling;
        private boolean mVisibility;
        public static final int STATE_INIT = 0;
        public static final int STATE_NORMAL = 1;
        public static final int STATE_READY = 2;
        public static final int STATE_LOADING = 3;


        public abstract void onCreate(LayoutInflater inflater, ViewGroup root);

        public abstract void onStateChanged(int from, int to);

        public void onPushHeight(int height) {
        }

        public int getContentHeight() {
            return mContentHeight;
        }

        public VLListFooter() {
            mContext = null;
            mListView = null;
            mContentOccupy = false;
            mContentDisplay = true;
            mInited = false;
            mScroller = null;
            mListFooter = null;
            mContent = null;
            mContentHeight = 0;
            mContainer = null;
            mState = STATE_INIT;
            mScrolling = false;
        }

        @SuppressLint("ClickableViewAccessibility")
        protected void init(Context context, VLListView vllistview) {
            VLDebug.Assert(!mInited);
            mContext = context;
            mVLListView = vllistview;
            mListView = vllistview.mListView;
            mScroller = new Scroller(mContext, new DecelerateInterpolator());
            mListFooter = new LinearLayout(mContext);
            mContent = new LinearLayout(context);
            mContent.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mContent.setLayoutParams(params);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            onCreate(layoutInflater, mContent);
            mContent.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            mContentHeight = mContent.getMeasuredHeight();
            mVisibility = true;
            int containerHeight = 0;
            if (mContentOccupy)
                containerHeight = mContentHeight;
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, containerHeight);
            mContainer = new LinearLayout(context);
            mContainer.setOrientation(LinearLayout.VERTICAL);
            mContainer.setGravity(Gravity.TOP);
            mListFooter.addView(mContainer, params);

            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mContentHeight);
            mContainer.addView(mContent, params);

            mListView.addFooterView(mListFooter);
            mListView.setFooterOnComputeScrollListener(new OnComputeScrollListener() {
                @Override
                public void onComputeScroll() {
                    if (mScroller.computeScrollOffset()) {
                        if (mScrolling && mVisibility) {
                            int containerHeight = mScroller.getCurrY();
                            setContainerHeight(containerHeight);
                            mListView.postInvalidate();
                        }
                    } else
                        mScrolling = false;
                }
            });

            mListView.setFooterOnTouchListener(new OnTouchListener() {
                private int mLastY = -1;

                @Override
                public boolean onTouch(View v, MotionEvent ev) {
                    if (!mVisibility) {
                        return false;
                    }
                    int rawY = (int) ev.getRawY();
                    if (mLastY == -1)
                        mLastY = rawY;
                    switch (ev.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastY = rawY;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int deltaY = (rawY - mLastY);
                            mLastY = rawY;
                            int lastVisiblePosition = mListView.getLastVisiblePosition();
                            int lastContentPosition = mListView.getCount() - 1 - (mVLListView.mListHeader == null ? 0 : 1)
                                    - (mVLListView.mListFooter == null ? 0 : 1);
                            if (lastVisiblePosition >= lastContentPosition) {
                                int containerHeight = getContainerHeight();
                                if (!mContentOccupy) {
                                    VLDebug.Assert(containerHeight >= 0);
                                    containerHeight += -deltaY;
                                    if (containerHeight < 0) {
                                        setContainerHeight(0);
                                        if (mState != STATE_LOADING)
                                            setState(STATE_NORMAL);
                                    } else {
                                        onPushHeight(containerHeight);
                                        setContainerHeight(containerHeight);
                                        if (mState != STATE_LOADING) {
                                            if (containerHeight > mContentHeight)
                                                setState(STATE_READY);
                                            else
                                                setState(STATE_NORMAL);
                                        }
                                    }
                                } else {
                                    VLDebug.Assert(containerHeight >= mContentHeight);
                                    containerHeight += -deltaY;
                                    if (containerHeight < mContentHeight) {
                                        setContainerHeight(mContentHeight);
                                        if (mState != STATE_LOADING)
                                            setState(STATE_NORMAL);
                                    } else {
                                        onPushHeight(containerHeight);
                                        setContainerHeight(containerHeight);
                                        if (mState != STATE_LOADING) {
                                            if (containerHeight > mContentHeight)
                                                setState(STATE_READY);
                                            else
                                                setState(STATE_NORMAL);
                                        }
                                    }
                                }
                            } else {
                                if (!mContentOccupy)
                                    setContainerHeight(0);// ?0
                                else
                                    setContainerHeight(mContentHeight);
                            }
                            break;
                        default:
                            mLastY = -1;// reset
                            lastVisiblePosition = mListView.getLastVisiblePosition();
                            lastContentPosition = mListView.getCount() - 1 - (mVLListView.mListHeader == null ? 0 : 1) - (mVLListView.mListFooter == null ? 0 : 1);
                            if (lastVisiblePosition >= lastContentPosition) {
                                int containerHeight = getContainerHeight();
                                if (!mContentOccupy) {
                                    VLDebug.Assert(containerHeight >= 0);
                                    if (containerHeight > 0) {
                                        if (containerHeight > mContentHeight) {
                                            mScroller.startScroll(0, containerHeight, 0, mContentHeight - containerHeight, 400);
                                            mScrolling = true;
                                            mListView.invalidate();
                                            if (mState != STATE_LOADING)
                                                setState(STATE_LOADING);
                                        } else {
                                            mScroller.startScroll(0, containerHeight, 0, 0 - containerHeight, 400);
                                            mScrolling = true;
                                            mListView.invalidate();
                                            if (mState != STATE_LOADING)
                                                setState(STATE_NORMAL);
                                        }
                                    }
                                } else {
                                    VLDebug.Assert(containerHeight >= mContentHeight);
                                    if (containerHeight > mContentHeight) {
                                        mScroller.startScroll(0, containerHeight, 0, mContentHeight - containerHeight, 400);
                                        mScrolling = true;
                                        mListView.invalidate();
                                        if (mState != STATE_LOADING)
                                            setState(STATE_LOADING);
                                    } else {
                                        if (mState != STATE_LOADING)
                                            setState(STATE_NORMAL);
                                    }
                                }
                            } else {
                                if (!mContentOccupy)
                                    setContainerHeight(0);
                                else
                                    setContainerHeight(mContentHeight);
                                if (mState != STATE_LOADING)
                                    setState(STATE_NORMAL);
                            }
                            break;
                    }
                    return false;
                }
            });
            setState(STATE_NORMAL);
            mInited = true;
        }

        public void setState(int state) {
            if (mState == state)
                return;
            int fromState = mState;
            mState = state;
            onStateChanged(fromState, mState);
        }

        public void setVisibility(boolean isVisibility) {
//            mVisibility = isVisibility;
//            if (!mVisibility) {
//                setContainerHeight(0);
//            } else {
//                setContainerHeight(mContentHeight);
//            }
        }


        private void setContainerHeight(int containerHeight) {
            VLDebug.Assert(containerHeight >= 0);
            if (mContentOccupy) {
                if (containerHeight < mContentHeight)
                    VLDebug.Assert(containerHeight >= mContentHeight);
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
            params.height = containerHeight;
            mContainer.setLayoutParams(params);
        }

        private int getContainerHeight() {
            return mContainer.getHeight();
        }

        public void setContentOccupy(boolean contentOccupy) {
            mContentOccupy = contentOccupy;
            reset();
        }

        public void setContentDisplay(boolean contentDisplay) {
            if (mContentDisplay == contentDisplay)
                return;
            mContentDisplay = contentDisplay;
            if (mContentDisplay) {
                mContent.setVisibility(View.VISIBLE);
                mContent.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mContentHeight = mContent.getMeasuredHeight();
            } else {
                mContent.setVisibility(View.GONE);
                mContentHeight = 0;
            }
            reset();
        }

        public void reset() {
            if (!mInited)
                return;
            setState(STATE_NORMAL);
            if (!mContentOccupy) {
                int containerHeight = getContainerHeight();
                if (containerHeight > 0) {
                    mScroller.startScroll(0, containerHeight, 0, 0 - containerHeight, 400);
                    mScrolling = true;
                    mListView.invalidate();
                } else
                    setContainerHeight(0);
            } else {
                int containerHeight = getContainerHeight();
                if (containerHeight > mContentHeight) {
                    mScroller.startScroll(0, containerHeight, 0, mContentHeight - containerHeight, 400);
                    mScrolling = true;
                    mListView.invalidate();
                } else
                    setContainerHeight(mContentHeight);
            }
        }
    }

    public void scrollToEnd() {
        if (mListViewItems.size() > 0) {
            mListView.setSelection(mListView.getCount() - 1);
            mListView.smoothScrollToPosition(mListView.getCount() - 1);
        }
    }

    public void scrollToFirst() {
        mListView.setStackFromBottom(false);
        if (mListViewItemsTemp.size() > 0) {
            mListView.setSelection(0);
        }
    }

    public BaseAdapter getAdapter() {
        return mListViewAdapter;
    }
}
