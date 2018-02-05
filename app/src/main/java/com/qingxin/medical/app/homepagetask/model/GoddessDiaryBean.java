package com.qingxin.medical.app.homepagetask.model;

import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.user.User;

import java.util.List;

/**
 * Created by zhikuo1 on 2018-01-31.
 */

public class GoddessDiaryBean {

    /**
     * code : 200
     * msg : ok
     * content : {"items":[{"id":"29fe42f0-4391-4438-936f-7341bdc84fae","name":"我的第一篇日记二恶","mem_id":"xxx","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/diary/3a3ed760-033f-11e8-9f53-2f701b8b47ec.jpg","created_at":"2018-1-26 23:54:56","summary":"这个真心不错呢","tags":"女人,美容","mem":{"id":"xxx","name":"青歆小妞","cover":"/banner/8e86fde0-033d-11e8-9f53-2f701b8b47ec.jpg"}}],"count":1}
     */

    private int count;
    private List<DiaryItemBean> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DiaryItemBean> getItems() {
        return items;
    }

    public void setItems(List<DiaryItemBean> items) {
        this.items = items;
    }

}
