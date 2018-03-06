package com.qingxin.medical.app.homepagetask.model;

import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.home.districtsel.StrictSelBean;

import java.io.Serializable;
import java.util.List;

/**
 * Date 2018-01-31
 *
 * @author zhikuo
 */
public class HomeBean implements Serializable{

    /**
     * code : 200
     * msg : ok
     * content : {"banners":[{"cover":"http://p36zly2vu.bkt.clouddn.com/banner/eacb5bd0-06a0-11e8-8218-39903d26fa93.gif","link":"https://pro.modao.cc/workspace/apps/p5E0DA22EBD1515479463494"},{"cover":"http://p36zly2vu.bkt.clouddn.com/banner/f14c65d0-06a0-11e8-8218-39903d26fa93.png","link":"xxx"}],"products":[{"id":"48929048-0bad-4449-80f0-aa651728e29c","name":"不错的","cover":"http://p36zly2vu.bkt.clouddn.com/product/2f978060-066d-11e8-b660-7ff7a8ac94b1.png","old_price":234,"price":12,"hospital":"meidi","isvip":"y","order":12,"ison":"y","created_at":"2018-1-31 17:57:47","updated_at":"2018-1-31 17:57:47"}],"diarys":[{"id":"17373a5e-1d90-422a-a139-fc6427d40804","name":"xxxx","product_id":"48929048-0bad-4449-80f0-aa651728e29c","mem_id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","oper_before_photo":"http://p36zly2vu.bkt.clouddn.com/diary/2e2044c0-036f-11e8-a325-cbf4f3071fd9.png","oper_after_photo":"http://p36zly2vu.bkt.clouddn.com/null","created_at":"2018-1-27 22:34:41","summary":"xxxx","tags":"xxx","visit_num":10,"collect_num":1,"mem":{"id":"b7d38cd8-a762-4fcc-bfc7-fde1e2efd424","name":"黄小琥","cover":"http://p36zly2vu.bkt.clouddn.com/banner/18ce1e00-036d-11e8-a325-cbf4f3071fd9.jpeg"},"product":{"id":"48929048-0bad-4449-80f0-aa651728e29c","name":"不错的"}}],"opencitys":[{"citycode":"028","name":"成都市","province":"四川省"},{"citycode":"0838","name":"德阳市","province":"四川省"},{"citycode":"029","name":"西安市","province":"陕西省"},{"citycode":"0915","name":"安康市","province":"陕西省"}],"preferrs":[{"id":"603ea43a-cf15-4ead-86a2-61053a03def1","type":"hospital","name":"成都铜雀台","video":"xxxxx","thumbnail":"http://p36zly2vu.bkt.clouddn.com/product/2520ecc0-082f-11e8-960c-591dc0e0fd80.jpeg","summary":"打造你的专属之美","order":76,"created_at":"2018-2-2 23:38:42","updated_at":"2018-2-2 23:38:42"},{"id":"xxx","type":"doctor","name":"黄小琥","video":null,"thumbnail":"http://p36zly2vu.bkt.clouddn.com/product/8a97eb90-082e-11e8-960c-591dc0e0fd80.png","summary":"不错哦这个艺术","order":11,"created_at":"2018-1-27 22:16:02","updated_at":"2018-1-27 22:16:02"}]}
     */

    private List<BannersBean> banners;
    private List<VipProductBean> products;
    private List<DiaryItemBean> diarys;
    private List<OpencitysBean> opencitys;
    private List<StrictSelBean> preferrs;

    public List<StrictSelBean> getPreferrs() {
        return preferrs;
    }

    public void setPreferrs(List<StrictSelBean> preferrs) {
        this.preferrs = preferrs;
    }

    public List<BannersBean> getBanners() {
        return banners;
    }

    public void setBanners(List<BannersBean> banners) {
        this.banners = banners;
    }

    public List<VipProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<VipProductBean> products) {
        this.products = products;
    }

    public List<DiaryItemBean> getDiarys() {
        return diarys;
    }

    public void setDiarys(List<DiaryItemBean> diarys) {
        this.diarys = diarys;
    }

    public List<OpencitysBean> getOpencitys() {
        return opencitys;
    }

    public void setOpencitys(List<OpencitysBean> opencitys) {
        this.opencitys = opencitys;
    }

    public static class BannersBean implements Serializable{
        /**
         * cover : http://p36zly2vu.bkt.clouddn.com/banner/eacb5bd0-06a0-11e8-8218-39903d26fa93.gif
         * link : https://pro.modao.cc/workspace/apps/p5E0DA22EBD1515479463494
         */

        private String cover;
        private String link;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        @Override
        public String toString() {
            return "BannersBean{" +
                    "cover='" + cover + '\'' +
                    ", link='" + link + '\'' +
                    '}';
        }
    }


    public static class OpencitysBean  implements Serializable{
        /**
         * citycode : 028
         * name : 成都市
         * province : 四川省
         */

        private String citycode;
        private String name;
        private String province;

        public String getCitycode() {
            return citycode;
        }

        public void setCitycode(String citycode) {
            this.citycode = citycode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        @Override
        public String toString() {
            return "OpencitysBean{" +
                    "citycode='" + citycode + '\'' +
                    ", name='" + name + '\'' +
                    ", province='" + province + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ContentBean{" +
                "banners=" + banners +
                ", products=" + products +
                ", diarys=" + diarys +
                ", opencitys=" + opencitys +
                ", preferrs=" + preferrs +
                '}';
    }
}
