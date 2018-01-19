package com.liy.today.statistic;


import com.liy.today.APPEnvironment;

import java.io.Serializable;
import java.util.List;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class UBTPageEvent implements Serializable {

    private String pageId;
    private String pageName;
    private String referPageId;
    private String referPageName;
    private String path;
    private String host;
    private String orderId;
    private String productId;
    private String pageSearchWord;
    private String channel;
    private String browser;
    private String parameters;
    private String refer;
    private String cityId;
    private String businessType = "1";

    private String requestId;
    private String appVersion;
    private String gpsCityId;
    private String gpsApiType;
    private String guid;
    private String requestTime;
    private String gpsCoordinate;
    private String deviceType = "Android";
    private String pushType;
    private String pushToken;
    private String userToken;
    private String appType;
    private String appVersionCode;
    private String appChannel;
    private String deviceOSVersion;
    private String deviceIMEI;
    private String deviceBrand;
    private String deviceProduct;
    private String deviceCPU;
    private String deviceNetType;
    private String deviceResolution;
    private String userAgent;
    private List<UBTActionEevnt> ubtData;

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public List<UBTActionEevnt> getUbtData() {
        return ubtData;
    }

    public void setUbtData(List<UBTActionEevnt> ubtData) {
        this.ubtData = ubtData;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getReferPageId() {
        return referPageId;
    }

    public void setReferPageId(String referPageId) {
        this.referPageId = referPageId;
    }

    public String getReferPageName() {
        return referPageName;
    }

    public void setReferPageName(String referPageName) {
        this.referPageName = referPageName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getGpsCoordinate() {
        return gpsCoordinate;
    }

    public void setGpsCoordinate(String gpsCoordinate) {
        this.gpsCoordinate = gpsCoordinate;
    }

    public String getGpsCityId() {
        return gpsCityId;
    }

    public void setGpsCityId(String gpsCityId) {
        this.gpsCityId = gpsCityId;
    }

    public String getGpsApiType() {
        return gpsApiType;
    }

    public void setGpsApiType(String gpsApiType) {
        this.gpsApiType = gpsApiType;
    }

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = String.format("%s:%s"
                , APPEnvironment.getAppName(), appVersion);
    }

    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppChannel() {
        return appChannel;
    }

    public void setAppChannel(String appChannel) {
        this.appChannel = appChannel;
    }

    public String getDeviceOSVersion() {
        return deviceOSVersion;
    }

    public void setDeviceOSVersion(String deviceOSVersion) {
        this.deviceOSVersion = deviceOSVersion;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceProduct() {
        return deviceProduct;
    }

    public void setDeviceProduct(String deviceProduct) {
        this.deviceProduct = deviceProduct;
    }

    public String getDeviceCPU() {
        return deviceCPU;
    }

    public void setDeviceCPU(String deviceCPU) {
        this.deviceCPU = deviceCPU;
    }

    public String getDeviceNetType() {
        return deviceNetType;
    }

    public void setDeviceNetType(String deviceNetType) {
        this.deviceNetType = deviceNetType;
    }

    public String getDeviceResolution() {
        return deviceResolution;
    }

    public void setDeviceResolution(String deviceResolution) {
        this.deviceResolution = deviceResolution;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPageSearchWord() {
        return pageSearchWord;
    }

    public void setPageSearchWord(String pageSearchWord) {
        this.pageSearchWord = pageSearchWord;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
