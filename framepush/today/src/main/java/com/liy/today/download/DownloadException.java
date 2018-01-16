package com.liy.today.download;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 3/2/16
 * @description Edit it! Change it! Beat it! Whatever, just do it!
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
