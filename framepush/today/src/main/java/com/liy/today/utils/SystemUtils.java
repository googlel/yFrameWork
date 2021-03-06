package com.liy.today.utils;

import android.os.Build;

/**
 * Created by qj on 2017/4/27.
 */

public class SystemUtils {

    public static String getUUid() {

        String m_szDevIDShort = "35" + //we make this look like a valid IMEI

                Build.BOARD.length () % 10 +
                Build.BRAND.length () % 10 +
                Build.CPU_ABI.length () % 10 +
                Build.DEVICE.length () % 10 +
                Build.DISPLAY.length () % 10 +
                Build.HOST.length () % 10 +
                Build.ID.length () % 10 +
                Build.MANUFACTURER.length () % 10 +
                Build.MODEL.length () % 10 +
                Build.PRODUCT.length () % 10 +
                Build.TAGS.length () % 10 +
                Build.TYPE.length () % 10 +
                Build.USER.length () % 10; //13 digits

        return m_szDevIDShort;
    }
}
