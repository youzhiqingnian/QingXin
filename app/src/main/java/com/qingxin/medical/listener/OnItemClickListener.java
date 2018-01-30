package com.qingxin.medical.listener;

/**
 * recyclerView click事件回调
 *
 * @author zhikuo
 */
public interface OnItemClickListener<T> {

    void onItemClick(T t, int position);

}
