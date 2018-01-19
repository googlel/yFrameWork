package com.liy.today.download;

import android.text.TextUtils;

import com.liy.today.base.BaseApplication;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class DownloadUnit {

    private String filePath;

    private String md5Check;

    private String reqUrl;

    private int progress;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getFilePath() {
        return BaseApplication.getBaseApplication ().getExternalCacheDir() + filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMd5Check() {
        return md5Check;
    }

    public void setMd5Check(String md5Check) {
        this.md5Check = md5Check;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    /**
     * 检查是否所有的字段不为空
     * @return boolean
     */
    public boolean checkArgsIllegal() {
        return !(TextUtils.isEmpty(filePath) || TextUtils.isEmpty(md5Check)
                || TextUtils.isEmpty(reqUrl));
    }
}
