package com.liy.today.statistic;

import java.io.Serializable;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 15/11/26
 * @description Edit it! Change it! Beat it! Whatever, just do it!
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
