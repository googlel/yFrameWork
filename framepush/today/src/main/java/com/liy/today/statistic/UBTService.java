package com.liy.today.statistic;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.liy.today.APPEnvironment;
import com.liy.today.base.BaseApplication;

import java.util.ArrayList;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class UBTService extends Service {

    private UBTFileSaver mSaver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException(
                "Cannot bind to UBT Service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(UBTService.this);
        mSaver = new UBTFileSaver();
        mSaver.start();

        flushEventAppStart(getPackageName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return Service.START_NOT_STICKY;
    }

    /**
     * 统计App启动/前台/后台的事件
     */
    private void flushEventAppStart(@NonNull String packName){
        UBTPageEvent event = new UBTPageEvent();
        event.setPageId(packName);
        event.setPageName(APPEnvironment.getAppName());
        event.setUbtData(new ArrayList<>());

        UBTActionEevnt actionEevnt = new UBTActionEevnt();
        actionEevnt.setName(EventKey.APP_START);
        actionEevnt.setAction(EventKey.APP_START);
        actionEevnt.setTimemills(String.valueOf(System.currentTimeMillis()));
        event.getUbtData().add(actionEevnt);

        UBTActionEevnt frontEvent = new UBTActionEevnt();
        frontEvent.setName(EventKey.APP_FRONT);
        frontEvent.setAction(EventKey.APP_FRONT);
        frontEvent.setTimemills(String.valueOf(System.currentTimeMillis()));
        event.getUbtData().add(frontEvent);

        onEvent(event);
    }

    /**
     * 具体处理相关事宜, 判断是否废弃该Event
     * @param event event
     */
    public void onEvent(UBTPageEvent event) {
        if (event == null) {
            return;
        }

        if (TextUtils.isEmpty(event.getPageId()) || TextUtils.isEmpty(event.getPageName())) {
            return;
        }

        event.setRequestId(String.valueOf(UUID.randomUUID()));
        event.setRequestTime(String.valueOf(System.currentTimeMillis()));
        event.setAppChannel(APPEnvironment.getAppChannel());
        event.setAppType(BaseApplication.getBaseApplication().getPackageName());
        event.setUserToken(APPEnvironment.getUserToken());
        event.setGpsApiType(APPEnvironment.getGpsApiType());
        event.setGpsCityId(APPEnvironment.getGpsCityId());
        event.setGpsCoordinate(APPEnvironment.getGpsCoordinate());
        event.setPushType(APPEnvironment.getPushType());
        event.setPushToken(APPEnvironment.getDeviceToken());
        event.setDeviceResolution(APPEnvironment.getResolution());
        event.setAppVersionCode(APPEnvironment.getAppVersionCode());
        event.setAppVersion(APPEnvironment.getAppVersion());
        event.setGuid(APPEnvironment.getUserKey());
        event.setDeviceOSVersion(APPEnvironment.getDeviceOsVersion());
        event.setDeviceIMEI(APPEnvironment.getDeviceImei());
        event.setDeviceBrand(APPEnvironment.getDeviceBrand());
        event.setDeviceProduct(APPEnvironment.getDeviceProduct());
        event.setDeviceCPU(APPEnvironment.getDeviceCpu());
        event.setDeviceNetType(APPEnvironment.getDeviceNetType());
        event.setUserAgent(APPEnvironment.getUserAgent());
        mSaver.enqueueEvent(event);
    }

    /**
     * 统计App启动/前台/后台的事件
     */
    private void flushEventAppEnd(@NonNull String packName){
        UBTPageEvent event = new UBTPageEvent();
        event.setPageId(packName);
        event.setPageName(APPEnvironment.getAppName());
        event.setUbtData(new ArrayList<>());

        UBTActionEevnt frontEvent = new UBTActionEevnt();
        frontEvent.setName(EventKey.APP_END);
        frontEvent.setAction(EventKey.APP_END);
        frontEvent.setTimemills(String.valueOf(System.currentTimeMillis()));
        event.getUbtData().add(frontEvent);

        onEvent(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        flushEventAppEnd(getPackageName());
        EventBus.getDefault().unregister(UBTService.this);
        if (mSaver != null) {
            mSaver.shutDown();
        }
    }

}
