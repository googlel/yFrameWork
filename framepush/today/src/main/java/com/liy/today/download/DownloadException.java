package com.liy.today.download;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public final class DownloadException extends Throwable {

    private int mErrorStatus;

    private String mErrorReason;

    public int getErrorStatus() {
        return mErrorStatus;
    }

    public void setErrorStatus(int errorStatus) {
        mErrorStatus = errorStatus;
    }

    public String getErrorReason() {
        return mErrorReason;
    }

    public void setErrorReason(String errorReason) {
        mErrorReason = errorReason;
    }
}
