package com.qingxin.medical;

import com.vlee78.android.vl.VLUmengStatModel;

public class QingXinUmengModel extends VLUmengStatModel {
    @Override
    protected void onConfig() {
        String umengEnabled = getConcretApplication().getResources().getString(R.string.UMENG_ENABLED);
        boolean enabled = !(umengEnabled.equalsIgnoreCase("false"));
        setEnabled(enabled);

        String appKey = getConcretApplication().getResources().getString(R.string.UMENG_APPKEY);
        String channel = getConcretApplication().getResources().getString(R.string.APP_CHANNEL);
        setAppKey(appKey,channel);

        boolean debug = getConcretApplication().appIsDebug();
        setDebugMode(debug);
    }
}
