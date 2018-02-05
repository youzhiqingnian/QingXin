package com.qingxin.medical.app;

import android.content.Context;
import android.widget.BaseAdapter;
import java.util.List;
/**
 *
 * Date 2018-01-31
 * @author zhikuo1
 */

public abstract class CommonAdapter<T> extends BaseAdapter
{
    protected Context mContext;
    protected List<T> mData;

    public CommonAdapter(Context context, List<T> mDatas)
    {
        this.mContext = context;
        this.mData = mDatas;
    }

    @Override
    public int getCount()
    {
        return mData==null?0:mData.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mData==null?null:mData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

}

