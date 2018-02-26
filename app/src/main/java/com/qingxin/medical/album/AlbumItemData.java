package com.qingxin.medical.album;

import android.util.SparseArray;
import com.qingxin.medical.base.BaseBean;
import com.vlee78.android.vl.VLDebug;
import java.util.Date;

/**
 * 作品集Grid的一个Item的数据
 * Date 2016/10/26
 *
 * @param <D>
 * @author zhikuo
 */
public abstract class AlbumItemData<D> extends BaseBean {
    public final static int TAG_DEFAULT_KEY = 0;
    private AlbumItemType type;
    private D data;
    transient SparseArray<Object> tags;

    public AlbumItemData(D data) {
        this(AlbumItemType.PHOTO, data);
    }

    public AlbumItemData(AlbumItemType type, D data) {
        this.type = type;
        this.data = data;
    }

    public long getItemId() {
        return -1;
    }

    public D getData() {
        return data;
    }

    public Date getCtime() {
        return null;
    }

    public void setTag(int k, Object tag) {
        VLDebug.Assert(k != TAG_DEFAULT_KEY && tag != null);
        if (tags == null) tags = new SparseArray<>();
        tags.put(k, tag);
    }

    public void setTag(Object tag) {
        if (tags == null) tags = new SparseArray<>();
        tags.put(TAG_DEFAULT_KEY, tag);
    }

    public void removeTag(int k) {
        if (tags == null) return;
        tags.remove(k);
    }

    public Object getTag(int k) {
        if (tags == null) return null;
        return tags.get(k);
    }

    public boolean hasPrivilege(AlbumItemPrivilege privilege) {
        return true;
    }

    public AlbumItemType getType() {
        return type;
    }

    public abstract String getImageUrl();

    public String getTitle() {
        return null;
    }

    @Override
    public int hashCode() {
        String url = getImageUrl();
        if (url != null) url.hashCode();
        return data != null ? data.hashCode() : super.hashCode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            return ((AlbumItemData<D>) obj).getImageUrl().equals(this.getImageUrl()) || super.equals(obj);
        }
        return super.equals(obj);
    }

    public enum AlbumItemPrivilege {
        ALL,
        DELETE,
        READ,
    }

    /**
     * 作品集Grid的一个Item的类型
     */
    public enum AlbumItemType {
        /**
         * 图片
         */
        PHOTO,

        /**
         * 视频
         */
        VIDEO,
    }


    public interface AlbumItemDataBuilder<T> {
        AlbumItemData<T> build(T data);
    }
}
