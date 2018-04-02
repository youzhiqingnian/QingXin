package com.qingxin.medical.app.homepagetask.model;

import com.qingxin.medical.app.goddessdiary.DiaryItemBean;
import com.qingxin.medical.base.BaseBean;
import com.qingxin.medical.home.districtsel.StrictSelBean;

import java.util.List;

/**
 * Date 2018-01-31
 *
 * @author zhikuo
 */
public class HomeBean extends BaseBean {

    private List<BannersBean> banners;
    private List<VipProductBean> products;
    private List<DiaryItemBean> diarys;
    private List<OpencitysBean> opencitys;
    private List<StrictSelBean> preferrs;
    private List<String> productimgs;

    public List<String> getProductimgs() {
        return productimgs;
    }

    public void setProductimgs(List<String> productimgs) {
        this.productimgs = productimgs;
    }

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

    public static class BannersBean extends BaseBean {

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
    }

    public static class OpencitysBean extends BaseBean {
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
    }
}
