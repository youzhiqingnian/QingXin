package com.qingxin.medical.service.view;

import com.qingxin.medical.service.entity.Book;

/**
 * Created by user on 2018-01-22.
 */

public interface BookView extends View {

    void onSuccess(Book mBook);
    void onError(String result);

}
