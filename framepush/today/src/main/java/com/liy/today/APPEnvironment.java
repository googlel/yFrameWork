package com.liy.today;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import com.liy.today.base.BaseApplication;
import com.liy.today.utils.NetWorkUtils;


/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/23
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class APPEnvironment {

    private static String mAppName;
    private static String appVersion;
    private static String appVersionCode;
    private static String gpsCityId;
    private static String gpsCoordinate;
    private static String mAppChannel;
    private static boolean mRelease;
    private static String mResolution;
    private static Bitmap mIconBitmap;
    private static String mDeviceToken;
    private static String mPackageName;
    private static String mGpsApiType;
    private static String mPushType;
    private static String mPushToken;
    private static String mUserToken;
    private static String mUserId;
    private static String mUserKey;
    private static String mUserAgent;

    public static String getUserKey() {
        return mUserKey;
    }

    public static void setUserKey(String userKey) {
        if (TextUtils.isEmpty(userKey)) {
            return;
        }
        String match = "ymc_userkey=([^;]*)(.*)";
        if (!userKey.matches(match)) {
            return;
        }
        mUserKey = userKey.replaceAll(match, "$1");
    }

    /**
     * 是否Release版本
     * @param release release
     */
    public static void setRelease(boolean release) {
        mRelease = release;
    }

    /**
     * isRelease
     * @return boolean
     */
    public static boolean isRelease() {
        return mRelease;
    }

    /**
     * 获取APPName
     * @param name name
     */
    public static void setAppName(String name) {
        mAppName = name;
    }

    /**
     * 获取APP Name
     * @return String
     */
    public static String getAppName() {
        return mAppName == null ? "yuntu" : mAppName;
    }

    /**
     * 获取当前版本
     * @return VersionName
     */
    public static String getAppVersion() {
        return appVersion;
    }

    public static void setAppVersion(String appVersion) {
        APPEnvironment.appVersion = appVersion;
    }

    /**
     * 获取当前版本号
     * @return VersionName
     */
    public static String getAppVersionCode() {
        return appVersionCode;
    }

    public static void setAppVersionCode(String appVersionCode) {
        APPEnvironment.appVersionCode = appVersionCode;
    }

    /**
     * getGpsCityId
     * @return String
     */
    public static String getGpsCityId() {
        return gpsCityId;
    }

    /**
     * setGpsCityId
     * @param gpsCityId gpsCityId
     */
    public static void setGpsCityId(String gpsType, String gpsCityId) {
        APPEnvironment.gpsCityId = gpsCityId;
        APPEnvironment.mGpsApiType = gpsType;
    }

    /**
     * getGpsCoordinate
     * @return String
     */
    public static String getGpsCoordinate() {
        return gpsCoordinate;
    }

    /**
     * setGpsCoordinate
     * @param gpsCoordinate gpsCoordinate
     */
    public static void setGpsCoordinate(String gpsCoordinate) {
        APPEnvironment.gpsCoordinate = gpsCoordinate;
    }

    /**
     * getAppChannel
     */
    public static String getAppChannel() {
        return mAppChannel;
    }

    /**
     * setAppChannel
     */
    public static void setAppChannel(String appChannel) {
        APPEnvironment.mAppChannel = appChannel;
    }

    /**
     * setResolution
     * @param width width
     * @param height height
     */
    public static void setResolution(int width, int height) {
        mResolution = width + "x" + height;
    }

    /**
     * getIconDrawable
     * @return drawable
     */
    public static Bitmap getIconBitmap() {
        return mIconBitmap;
    }

    /**
     * setIconDrawable
     * @param icon icon
     */
    public static void setIconBitmap(Bitmap icon) {
        APPEnvironment.mIconBitmap = icon;
    }

    /**
     * getResolution
     * @return String
     */
    public static String getResolution() {
        return mResolution;
    }

    public static String getDeviceType() {
        return "Android";
    }

    /**
     * 获取设备识别码
     * @return String
     */
    public static String getDeviceImei() {
        String imei = "1122";
//        try {
//            TelephonyManager phoneMgr=(TelephonyManager) BaseApplication.getBaseApplication()
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//             imei = phoneMgr.getDeviceId();
//        } catch (SecurityException ex) {
//            ex.printStackTrace();
//        }
        return imei;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    public static String getDeviceOsVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }


    public static String getDeviceNetType() {
        return NetWorkUtils.getNetworkTypeName(BaseApplication.getBaseApplication());
    }

    public static String getDeviceCpu() {
        return Build.CPU_ABI;
    }


    /**
     * getDeviceToken
     * @return String
     */
    public static String getDeviceToken() {
        return mDeviceToken;
    }

    /**
     * setDeviceToken
     * @param deviceToken deviceToken
     */
    public static void setDeviceToken(String deviceToken) {
        mDeviceToken = deviceToken;
    }

    /**
     * getPackageName
     * @return String
     */
    public static String getPackageName() {
        return mPackageName;
    }

    /**
     * setPackageName
     * @param packageName packageName
     */
    public static void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    /**
     * getGpsApiType
     * @return String
     */
    public static String getGpsApiType() {
        return mGpsApiType;
    }

    /**
     * getPushType
     * @return String
     */
    public static String getPushType() {
        return mPushType;
    }

    /**
     * getPushToken
     * @return String
     */
    public static String getPushToken() {
        return mPushToken;
    }

    /**
     * setPushToken
     * @param pushToken pushToken
     */
    public static void setPushInfo(String pushType, String pushToken) {
        mPushToken = pushToken;
        mPushType = pushType;
    }

    /**
     * getUserToken
     * @return String
     */
    public static String getUserToken() {
        return mUserToken;
    }

    /**
     * setUserToken
     * @param userToken userToken
     */
    public static void setUserToken(String userToken) {
        mUserToken = userToken;
    }

    public static String getUserId() {
        return mUserId;
    }

    public static void setUserId(String userId) {
        mUserId = userId;
    }

    public static String getUserAgent() {
        return mUserAgent;
    }

    public static void setUserAgent(String userAgent) {
        mUserAgent = userAgent;
    }

    /**
     * 获取手机号码
     * @return String phone number
     */
    public static String getPhoneNumber() {
        String phone = "1234";
//        try {
//            TelephonyManager phoneMgr=(TelephonyManager) BaseApplication.getBaseApplication()
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            phone = phoneMgr.getLine1Number();
//        } catch (SecurityException ex) {
//            ex.printStackTrace();
//        }
        return phone;
    }


    /**
     * 清理保存的内容
     */
    public static void clear() {
        mAppName = null;
        mAppChannel = null;
        mDeviceToken = null;
        mGpsApiType = null;
        mRelease = false;
        mResolution = null;
        mPushToken = null;
        mPushType = null;
        mPackageName = null;
        mUserToken = null;
        mUserAgent = null;
        if (mIconBitmap != null) {
            mIconBitmap.recycle();
        }
        System.gc();
    }
}
