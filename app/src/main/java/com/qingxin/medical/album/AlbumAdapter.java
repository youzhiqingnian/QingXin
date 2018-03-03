package com.qingxin.medical.album;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.qingxin.medical.R;
import com.vlee78.android.vl.VLDebug;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 作品集数据适配器
 */
public class AlbumAdapter<T> extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context mContext;
    private List<AlbumItemData<T>> mItemDataList = new ArrayList<>();
    private List<AlbumItemData<T>> mUmItemDataList = Collections.unmodifiableList(mItemDataList);
    private String mTag;
    private int mMaxSize;
    private boolean mShowAddOperation;
    private OnClickListener mOnClickListener;
    public static final String AFTER_PHOTO = "AFTER_PHOTO";
    public static final String BEFORE_PHOTO = "BEFORE_PHOTO";

    public AlbumAdapter(@NonNull Context context, String tag, int maxSize, boolean showAddOperation, OnClickListener onClickListener) {
        VLDebug.Assert(maxSize >= 0);
        this.mContext = context;
        this.mTag = tag;
        this.mMaxSize = maxSize;
        this.mShowAddOperation = showAddOperation;
        this.mOnClickListener = onClickListener;
    }

    public AlbumAdapter(Context context, String tag, int maxSize, OnClickListener onClickListener) {
        this(context, tag, maxSize, true, onClickListener);
    }

    public List<AlbumItemData<T>> getItems() {
        return mUmItemDataList;
    }

    public List<T> getItemDataList() {
        List<T> dataList = new ArrayList<>(mUmItemDataList.size());
        for (AlbumItemData<T> item : mUmItemDataList) {
            dataList.add(item.getData());
        }
        return dataList;
    }

    public List<String> getItemUrls() {
        List<String> urls = new ArrayList<>(mUmItemDataList.size());
        for (AlbumItemData<T> photo : mUmItemDataList) {
            urls.add(photo.getImageUrl());
        }
        return urls;
    }

    public void addItem(@NonNull AlbumItemData<T> itemData) {
        mItemDataList.add(itemData);
        this.notifyDataSetChanged();
    }

    public void clear() {
        mItemDataList.clear();
        this.notifyItemRangeRemoved(0, mItemDataList.size());
    }

    public void addItems(@NonNull List<AlbumItemData<T>> itemDataList) {
        mItemDataList.addAll(itemDataList);
        this.notifyItemRangeInserted(mItemDataList.size() - 1, itemDataList.size());
    }

    public AlbumItemData<T> removeItem(int position) {
        AlbumItemData<T> removedElement = null;
        if (position < mItemDataList.size()) {
            removedElement = mItemDataList.remove(position);
            this.notifyItemRemoved(position);
        }
        return removedElement;
    }

    public int removeByItemId(long... itemIds) {
        int delSize = 0;
        if (itemIds == null || itemIds.length == 0) return delSize;
        Set<Long> ids = new HashSet<>(itemIds.length);
        for (long id : itemIds) {
            ids.add(id);
        }
        for (Iterator<AlbumItemData<T>> iter = mItemDataList.iterator(); iter.hasNext(); ) {
            AlbumItemData<T> next = iter.next();
            if (ids.contains(next.getItemId())) {
                iter.remove();
                delSize++;
            }
        }
        if (delSize > 0)
            this.notifyDataSetChanged();
        return delSize;
    }

    public int getItemSize() {
        return mItemDataList.size();
    }

    public void setShowAddOperation(boolean showAddOperation) {
        this.mShowAddOperation = showAddOperation;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mShowAddOperation ? Math.min(mItemDataList.size() + 1, mMaxSize) : mItemDataList.size();
    }

    public AlbumItemData<T> getItemData(int position) {
        AlbumItemData<T> itemData = null;
        if (position < mItemDataList.size()) {
            itemData = mItemDataList.get(position);
        }
        return itemData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_diary_album, parent, false));
    }

    @Override
    public void onBindViewHolder(AlbumAdapter.ViewHolder holder, int position) {
        AlbumItemData<T> itemData = getItemData(position);
        // 最后一个元素为添加操作
        if (itemData == null) {
            if (mShowAddOperation) {
                if (holder.mTitleTv.getVisibility() == View.VISIBLE) {
                    holder.mTitleTv.setVisibility(View.GONE);
                }
                holder.mImageView.setImageResource(AFTER_PHOTO.equals(mTag) ? R.mipmap.ic_after_add : R.mipmap.ic_before_add);
                holder.mImageView.setOnClickListener(v -> {
                    if (mOnClickListener != null)
                        mOnClickListener.onAddItemClicked(v);
                });
            }
        } else {
            holder.mImageView.setImageBitmap((Bitmap) itemData.getData());
            if (position == 0) {
                if (holder.mTitleTv.getVisibility() == View.GONE) {
                    holder.mTitleTv.setVisibility(View.VISIBLE);
                }
                holder.mTitleTv.setText(String.format("封面-%s", AFTER_PHOTO.equals(mTag) ? "After" : "Before"));
            } else {
                if (holder.mTitleTv.getVisibility() == View.VISIBLE) {
                    holder.mTitleTv.setVisibility(View.GONE);
                }
            }

            holder.mImageView.setOnClickListener(v -> {
                if (mOnClickListener != null)
                    mOnClickListener.onItemClicked(v, position);
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private TextView mTitleTv;

        ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTitleTv = itemView.findViewById(R.id.titleTv);
        }
    }

    public interface OnClickListener {
        void onAddItemClicked(View view);

        void onItemClicked(View view, int position);
    }
}
