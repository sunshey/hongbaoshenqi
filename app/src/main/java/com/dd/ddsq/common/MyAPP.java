package com.dd.ddsq.common;

import android.app.Application;

import com.dd.ddsq.R;
import com.dd.ddsq.bean.GoagalInfo;
import com.dd.ddsq.util.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by admin on 2017/2/24.
 */

public class MyAPP extends Application {

    {
        PlatformConfig.setWeixin("wxf7da473abb45cd6c", "c6fe4b397f035c6ef354135029e541cf");
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Config.DEBUG = true;
//        LogUtil.DEBUG = false;
        UMShareAPI.get(this);
        GoagalInfo.init(this);
        com.kk.securityhttp.domain.GoagalInfo.get().init(this);
//        XxBeiAPI.initSDK(this, Config.publicKey, true);

        String agent_id = "1";
        if (GoagalInfo.channelInfo != null && GoagalInfo.channelInfo.agent_id != null) {
            agent_id = GoagalInfo.channelInfo.agent_id;

        }

        String appId_agentId = "红包-渠道id" + agent_id + getResources().getString(R.string.installation);


        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),
                "58afed66aed17968b9001e01", appId_agentId));

    }
}
