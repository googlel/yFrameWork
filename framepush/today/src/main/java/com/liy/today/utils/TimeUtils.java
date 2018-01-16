package com.liy.today.utils;

import android.os.Handler;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/12/3
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class TimeUtils {
    public static final Date NetDate = null;

    public static final int TimeType_Value_Second = 0;
    public static final int TimeType_Value_Min = 1;
    public static final int TimeType_Value_Hour = 2;
    public static final int TimeType_Value_Day = 3;
    public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

    private static String[] timeUnits = new String[]{"秒", "分", "小时", "天", "年"};
    private static int[] timeUnitsSize = new int[]{60, 60, 24, 365};

    public static class TimeDiv {
        long[] values;//以TimeType排序的值
        long diff;
        String format;

        public TimeDiv(long[] values, long diff, String format) {
            this.values = values;
            this.diff = diff;
            this.format = format;
        }

        public long getValue(int filed) {
            if (filed < 0 || filed > 3) return 0;
            return values[filed];
        }

        public long[] getValues() {
            return values;
        }

        public long getDiff() {
            return diff;
        }

        public String getFormat() {
            return format;
        }
    }

    /**
     * 比较时间 date - date2
     *
     * @param date          要被比较的时间
     * @param timeValueType 精确到的时间类型
     * @return key:按秒/分/小时/分排序的时间组， value：格式化好的内容。
     */
    public static TimeDiv getFriendlyDateDiv(Date date, Date date2, int timeValueType) {
        if (date == NetDate) date = getNetDate();
        if (date2 == NetDate) date2 = getNetDate();

        long nd = 1000l * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {获得两个时间的毫秒时间差异
        long diff = date.getTime() - date2.getTime();
        long[] values = new long[4];
        values[TimeType_Value_Day] = diff / nd;//计算差多少天
        values[TimeType_Value_Hour] = diff % nd / nh;//计算差多少小时
        values[TimeType_Value_Min] = diff % nd % nh / nm;//计算差多少分钟
        values[TimeType_Value_Second] = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        StringBuilder sb = new StringBuilder();
        int index = values.length;
        boolean skipValue = true;
        while (timeValueType < index) {
            index--;
            if (skipValue && values[index] == 0) continue;
            String value = "" + values[index];
            if (value.length() <= 1) {
                value = "0" + value;
            }
            sb.append(value).append(timeUnits[index]);
            skipValue = false;//只能跳过所有开头的0
        }
        return new TimeDiv(values, diff, sb.toString());
    }

    public interface TimeLoopListener {
        /**
         * @param values      按秒/分/小时/分排序的时间组
         * @param format      格式化好的时间
         * @param isTimeReach 时间是否已到达
         * @return 是否需要继续循环
         */
        public boolean OnTimeLoop(long[] values, String format, boolean isTimeReach);
    }

    /**
     * 循环刷新时间，比较时间 date - date2，用TimeUtils.NetDate代表当前网络时间
     *
     * @param handler      用来循环
     * @param duration     间隔
     * @param timeType     精确到的时间类型
     * @param loopListener 监听
     */
    public static void checkTimeLoop(final Handler handler, final Date date, final Date date2, final int duration, final int timeType, final TimeLoopListener loopListener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (loopListener != null) {
                    TimeDiv timeDiv = getFriendlyDateDiv(date, date2, timeType);
                    if (loopListener.OnTimeLoop(timeDiv.values, timeDiv.format, timeDiv.diff < 0))
                        handler.postDelayed(this, duration);
                }
            }
        });
    }

    /**
     * 获得与当前时间与这个时间的差距的友好显示,
     *
     * @param date 输入的时间
     * @return key:这个时间-当前时间， value：显示内容(x年、x天、x小时、x分、x秒中的一个)
     */
    public static Map.Entry<Long, String> getFriendlyDateDiv(Date date) {
        Date netDate = getNetDate();
        long timeDiv = date.getTime() - netDate.getTime();
        long temp = Math.abs(timeDiv) / 1000;
        String value = "";
        for (int i = 0; i < timeUnitsSize.length; i++) {
            if (temp > timeUnitsSize[i]) {
                temp /= timeUnitsSize[i];
            } else {
                value = temp + timeUnits[i];
                break;
            }
        }
        return new AbstractMap.SimpleEntry<Long, String>(timeDiv, value);
    }

    private static long timeDivWithNet;

    /**
     * 立刻获得当前服务器时间。（利用每次请求服务器都会返回的时间与本地时间做对比）
     */
    public static Date getNetDate() {
        return new Date(System.currentTimeMillis() + timeDivWithNet);
    }

    public static void checkNetDate(Long timeMillis) {
        if (timeMillis == null) return;
        timeDivWithNet = timeMillis - System.currentTimeMillis();
    }


    private static ThreadLocal<SimpleDateFormat> dayEndFormatPool = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
    };
    private static ThreadLocal<SimpleDateFormat> minEndFormatPool = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        }
    };
    private static ThreadLocal<SimpleDateFormat> secEndFormatPool = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
    };

    public static SimpleDateFormat getDayEndFormat() {
        return dayEndFormatPool.get();
    }

    public static SimpleDateFormat getMinEndFormat() {
        return minEndFormatPool.get();
    }

    public static SimpleDateFormat getSecEndFormat() {
        return secEndFormatPool.get();
    }

    public static Date parseTime(String time) {
        if (TextUtils.isEmpty(time)) return null;
        try {
            return getSecEndFormat().parse(time);
        } catch (Exception e) {
            try {
                return getMinEndFormat().parse(time);
            } catch (Exception e1) {
                try {
                    if (time.length() == 13 || time.length() == 14) {//14位的时间戳，，，也许这份代码可以活到这个时间 - -
                        return new Date(Long.parseLong(time));
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    public static String getNowLocalTimeFormat(int endTimeType) {
        return getTimeFormat(new Date(), endTimeType);
    }

    public static String getNowNetTimeFormat(int endTimeType) {
        return getTimeFormat(getNetDate(), endTimeType);
    }

    public synchronized static String getTimeFormat(Date date, int endTimeType) {
        try {
            if (endTimeType == TimeType_Value_Day) return getDayEndFormat().format(date);
            if (endTimeType == TimeType_Value_Min) return getMinEndFormat().format(date);
            else return getSecEndFormat().format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String[] WEEKS = {"日", "一", "二", "三", "四", "五", "六"};

    public static String getWeekTimeFromDateString(String dateStr) {
        if (!TextUtils.isEmpty(dateStr)) {
            SimpleDateFormat format = new SimpleDateFormat();
            Date date = parseTime(dateStr);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                format.applyPattern("yyyy-MM-dd 00:00:00");
                Date dateNow = parseTime(format.format(calendar.getTimeInMillis()));
                if (dateNow != null) {
                    long gapTimeMills = date.getTime() - dateNow.getTime();
                    long days = gapTimeMills / (1000l * 24 * 60 * 60);
                    if (gapTimeMills >= 0 && days >= 0) {
                        if (days == 0) {
                            return "今天";
                        }
                        calendar.setTimeInMillis(dateNow.getTime());
                        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                        int value = (int) days + week;
                        if (value < 2 * WEEKS.length) {
                            if (value < WEEKS.length) {
                                return "本周" + WEEKS[value];
                            } else if (week == 0 && value == WEEKS.length) {
                                return "下周日";
                            } else if (value == WEEKS.length) {
                                return "本周日";
                            } else {
                                return "下周" + WEEKS[(value % WEEKS.length)];
                            }
                        }
                    }
                }
                format.applyPattern("yyyy-MM-dd");
                return format.format(date);
            }
        }
        return dateStr;
    }

    /**
     * 获取函数调用栈名称
     */
    private static String getFunctionStackName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod() || st.getClassName().equals(Thread.class.getName())
                    || st.getClassName().equals(TimeUtils.class.getName())) {
                continue;
            }
            return st.getFileName() + ":" + st.getLineNumber();
        }
        return null;
    }

    private static final HashMap<String, Long> lastCheckTimeDivMap = new HashMap<String, Long>();

    /**
     * 限制与上次的执行必须间隔一定时间(通过方法调用栈区别唯一)
     *
     * @param timeInMillis 限制的时间
     * @return 是否可以执行
     */
    public synchronized static boolean checkTimeDivCall(long timeInMillis) {
        String functionName = getFunctionStackName();
        Long lastCall = lastCheckTimeDivMap.get(functionName);
        if (lastCall != null && System.currentTimeMillis() - lastCall < timeInMillis) {
            return false;
        }
        lastCheckTimeDivMap.put(functionName, System.currentTimeMillis());
        return true;
    }
    public static String formatDateTime(long dateL, String formater) {
        SimpleDateFormat sdf = new SimpleDateFormat(formater);
        return sdf.format(new Date(dateL));
    }
}
