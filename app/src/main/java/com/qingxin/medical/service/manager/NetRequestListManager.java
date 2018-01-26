package com.qingxin.medical.service.manager;

import android.content.Context;

import com.qingxin.medical.service.RetrofitHelper;
import com.qingxin.medical.service.RetrofitService;
import com.qingxin.medical.service.entity.Book;

import rx.Observable;

/**
 * Created by user on 2018-01-22.
 */

public class NetRequestListManager {

    public static Observable<Book> getSearchBooks(Context context,String name, String tag, int start, int count){
        return RetrofitHelper.getInstance(context).getServer().getSearchBooks(name,tag,start,count);
    }

}
