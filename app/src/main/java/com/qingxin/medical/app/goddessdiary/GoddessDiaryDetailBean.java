package com.qingxin.medical.app.goddessdiary;

/**
 *
 * Date 2018-02-02
 * @author zhikuo1
 */

public class GoddessDiaryDetailBean {

    /**
     * code : 200
     * msg : ok
     * content : {"item":{"id":"17373a5e-1d90-422a-a139-fc6427d40804","name":"xxxx","product_id":"48929048-0bad-4449-80f0-aa651728e29c","mem_id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/diary/2e2044c0-036f-11e8-a325-cbf4f3071fd9.png","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/null","summary":"xxxx","tags":"xxx","words":"","ison":"y","created_at":"2018-1-27 22:34:41","updated_at":"2018-01-27T14:34:41.000Z","visit_num":9,"collect_num":0,"mem":{"id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","name":"黄小琥","cover":"http://p36zly2vu.bkt.clouddn.com/banner/18ce1e00-036d-11e8-a325-cbf4f3071fd9.jpeg"},"product":{"id":"48929048-0bad-4449-80f0-aa651728e29c","name":"不错的","cover":"http://p36zly2vu.bkt.clouddn.com/product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png","old_price":234,"price":12,"hospital":"meidi","isvip":"y","order":12,"ison":"y","created_at":"2018-1-31 17:57:47","updated_at":"2018-1-31 17:57:47"},"is_collect":"n"}}
     */
    private DiaryItemBean item;

    public DiaryItemBean getItem() {
        return item;
    }

    public void setItem(DiaryItemBean item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "GoddessDiaryDetailBean{" +
                "item=" + item +
                '}';
    }
}
