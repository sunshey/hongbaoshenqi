package com.hb.hbsq.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;


import com.alibaba.fastjson.JSON;
import com.hb.hbsq.util.FileUtil;
import com.hb.hbsq.util.LogUtil;
import com.hb.hbsq.util.Md5;
import com.hb.hbsq.util.PathUtil;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by zhangkai on 16/9/19.
 */
public class GoagalInfo  {
    public static String publicKey = "-----BEGIN PUBLIC KEY-----" +
            "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA1zQ4FOFmngBVc05sg7X5" +
            "Z/e3GrhG4rRAiGciUCsrd/n4wpQcKNoOeiRahxKT1FVcC6thJ/95OgBN8jaDzKdd" +
            "cMUti9gGzBDpGSS8MyuCOBXc6KCOYzL6Q4qnlGW2d09blZSpFUluDBBwB86yvOxk" +
            "5oEtnf6WPw2wiWtm7JR1JrE1k+adYfy+Cx9ifJX3wKZ5X3n+CdDXbUCPBD63eMBn" +
            "dy1RYOgI1Sc67bQlQGoFtrhXOGrJ8vVoRNHczaGeBOev96/V0AiEY2f5Kw5PAWhw" +
            "NrAF94DOLu/4OyTVUg9rDC7M97itzBSTwvJ4X5JA9TyiXL6c/77lThXvX+8m/VLi" +
            "mLR7PNq4e0gUCGmHCQcbfkxZVLsa4CDg2oklrT4iHvkK4ZtbNJ2M9q8lt5vgsMkb" +
            "bLLqe9IuTJ9O7Pemp5Ezf8++6FOeUXBQTwSHXuxBNBmZAonNZO1jACfOzm83zEE2" +
            "+Libcn3EBgxPnOB07bDGuvx9AoSzLjFk/T4ScuvXKEhk1xqApSvtPADrRSskV0aE" +
            "G5F8PfBF//krOnUsgqAgujF9unKaxMJXslAJ7kQm5xnDwn2COGd7QEnOkFwqMJxr" +
            "DmcluwXXaZXt78mwkSNtgorAhN6fXMiwRFtwywqoC3jYXlKvbh3WpsajsCsbTiCa" +
            "SBq4HbSs5+QTQvmgUTPwQikCAwEAAQ==" +
            "-----END PUBLIC KEY-----";


    public static ChannelInfo channelInfo = null;
    public static PackageInfo packageInfo = null;


    public static String uuid = "";
    public static String channel = "default";
    public static String configPath = PathUtil.getConfigPath();
    public static LoginDataInfo loginDataInfo = null;

    public static String agent_id = 1 + "";

    public static void init(Context context) {
        String result1 = null;
        String result2 = null;
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zf = null;
        try {
            zf = new ZipFile(sourceDir);
            ZipEntry ze1 = zf.getEntry("META-INF/gamechannel.json");
            InputStream in1 = zf.getInputStream(ze1);
            result1 = FileUtil.readString(in1);
//            LogUtil.msg("渠道->" + result1);

            ZipEntry ze2 = zf.getEntry("META-INF/rsa_public_key.pem");
            InputStream in2 = zf.getInputStream(ze2);
            result2 = FileUtil.readString(in2);
            LogUtil.msg("公钥->" + result2);
        } catch (Exception e) {
            LogUtil.msg("apk中gamechannel或rsa_public_key文件不存在");
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
        }

        String name = "gamechannel.json";
        if (result1 != null) {
            FileUtil.writeInfoInSDCard(context, result1, configPath, name);
        } else {
            result1 = FileUtil.readInfoInSDCard(context, configPath, name);
        }

        if (result1 != null) {
            GoagalInfo.channel = result1;
        }

        name = "rsa_public_key.pem";
        if (result2 != null) {
            GoagalInfo.publicKey = getPublicKey(result2);
            FileUtil.writeInfoInSDCard(context, result2, configPath, name);
        } else {
            result2 = FileUtil.readInfoInSDCard(context, configPath, name);
            if (result2 != null) {
                GoagalInfo.publicKey = getPublicKey(result2);
            }
        }

        GoagalInfo.channelInfo = getChannelInfo();
        GoagalInfo.uuid = getUid(context);
        GoagalInfo.packageInfo = getPackageInfo(context);

    }


    private static ChannelInfo getChannelInfo() {
        try {
            ChannelInfo channelInfo = JSON.parseObject(GoagalInfo.channel, ChannelInfo.class);
            return channelInfo;
        } catch (Exception e) {
            LogUtil.msg("渠道信息解析错误->" + e.getMessage());
        }
        return null;
    }

    private static String getPublicKey(InputStream in) {
        String result = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                if (mLine.startsWith("----")) {
                    continue;
                }
                result += mLine;
            }
        } catch (Exception e) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e2) {
                }
            }
        }
        return result;
    }

    public static String getPublicKey() {
        GoagalInfo.publicKey = getPublicKey(GoagalInfo.publicKey);

        LogUtil.msg("pubickey:  " + Md5.md5(GoagalInfo.publicKey));
        return GoagalInfo.publicKey;
    }

    public static String getPublicKey(String key) {
        return key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
                .replace("-----END RSA PRIVATE KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\r", "")
                .replace("\n", "");
    }

    public static String getUid(Context context) {
        String uid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (uid == null || uid.isEmpty()) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            uid = wInfo.getMacAddress();

        }

        if (uid == null || uid.isEmpty() || uid.equals("02:00:00:00:00:00")) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            uid = telephonyManager.getDeviceId();
        }

        if (uid == null) {
            uid = "";
        }

        return uid;
    }


    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo;
        } catch (Exception e) {
        }
        return null;
    }


}
