package com.liy.today.statistic;

import java.io.Serializable;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class UBTActionEevnt implements Serializable {

    private String action;
    private String name;
    private String timemills;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimemills() {
        return timemills;
    }

    public void setTimemills(String timemills) {
        this.timemills = timemills;
    }
}
