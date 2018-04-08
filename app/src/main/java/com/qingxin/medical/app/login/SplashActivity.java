package com.qingxin.medical.app.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;

import com.qingxin.medical.R;
import com.qingxin.medical.app.homepagetask.HomeActivity;
import com.qingxin.medical.base.QingXinActivity;
import com.qingxin.medical.common.DistrictContract;
import com.qingxin.medical.common.DistrictItemData;
import com.qingxin.medical.common.DistrictPresenter;
import com.qingxin.medical.common.ProvinceData;
import com.qingxin.medical.common.QingXinError;
import com.qingxin.medical.config.ConfigBean;
import com.qingxin.medical.config.ConfigContract;
import com.qingxin.medical.config.ConfigModel;
import com.qingxin.medical.config.ConfigPresenter;
import com.qingxin.medical.guide.GuideActivity;
import com.vlee78.android.vl.VLBlock;
import com.vlee78.android.vl.VLScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 启动页
 * Date 2018-02-05
 *
 * @author zhikuo1
 */
public class SplashActivity extends QingXinActivity implements ConfigContract.View {

    private static final String FIRST_START = "isFirstStart";
    private ConfigPresenter mPresenter;
    private DistrictPresenter mDistrictPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        mPresenter = new ConfigPresenter(this);
        mPresenter.getConfigBean();
        boolean isFirstStart = getSharedPreferences().getBoolean(FIRST_START, true);
        VLScheduler.instance.schedule(3000, VLScheduler.THREAD_MAIN, new VLBlock() {
            @Override
            protected void process(boolean canceled) {
                if (isFirstStart) {
                    GuideActivity.startSelf(SplashActivity.this);
                    getSharedPreferences().edit().putBoolean(FIRST_START, false).apply();
                    finish();
                } else {
                    // 进入主页
                    HomeActivity.startSelf(SplashActivity.this, 0);
                    finish();
                }
            }
        });

        if (null == getModel(ConfigModel.class).getProviceData() || getModel(ConfigModel.class).getProviceData().size() == 0) {
            mDistrictPresenter = new DistrictPresenter(new DistrictContract.View() {
                @Override
                public void onSuccess(List<DistrictItemData> districtBeans) {
                    resetDistrictData(districtBeans);
                }

                @Override
                public void onError(QingXinError error) {
                }

                @Override
                public void setPresenter(DistrictContract.Presenter presenter) {
                }
            });
            mDistrictPresenter.getDistrictData("province,city");
        }
    }

    private void resetDistrictData(@NonNull List<DistrictItemData> districtBeans) {
        long startTime = System.currentTimeMillis();
        List<ProvinceData> proviceDatas = new ArrayList<>();
        Map<String, ProvinceData> provinceDataMap = new HashMap<>();
        Map<String, DistrictItemData> cityDataMap = new HashMap<>();
        int index = 0;
        for (DistrictItemData districtItemData : districtBeans) {
            if ("province".equals(districtItemData.getLevel())) {
                districtItemData.setIndex(index);
                ProvinceData provinceData = new ProvinceData();
                provinceData.setId(districtItemData.getId());
                provinceData.setAdcode(districtItemData.getAdcode());
                provinceData.setCitycode(districtItemData.getCitycode());
                provinceData.setParent(districtItemData.getParent());
                provinceData.setLevel(districtItemData.getLevel());
                provinceData.setName(districtItemData.getName());
                provinceData.setIndex(index);
                proviceDatas.add(provinceData);
                index++;
            }
        }

        int cityIndex = 0;
        for (ProvinceData provinceData : proviceDatas) {
            List<DistrictItemData> cities = new ArrayList<>();
            for (DistrictItemData districtItemData : districtBeans) {
                if (provinceData.getId().equals(districtItemData.getParent()) && "city".equals(districtItemData.getLevel())) {
                    cities.add(districtItemData);
                    districtItemData.setProvinceData(provinceData);
                    districtItemData.setIndex(cityIndex);
                    cityDataMap.put(districtItemData.getId(), districtItemData);
                    cityIndex++;
                }
            }
            provinceData.setCities(cities);
            provinceDataMap.put(provinceData.getId(), provinceData);
        }
        getModel(ConfigModel.class).setCitiesDataMap(cityDataMap);
        getModel(ConfigModel.class).setProviceData(proviceDatas);
        getModel(ConfigModel.class).setProvinceDataMap(provinceDataMap);
        long endTime = System.currentTimeMillis();
        Log.e("SplashActivity", "" + (endTime - startTime));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.subscribe();
        if (null != mDistrictPresenter) {
            mDistrictPresenter.subscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
        if (null != mDistrictPresenter) {
            mDistrictPresenter.unsubscribe();
        }
    }

    @Override
    public void setPresenter(ConfigContract.Presenter presenter) {
    }

    @Override
    public void onSuccess(ConfigBean configBean) {
    }

    @Override
    public void onError(QingXinError error) {
    }
}
