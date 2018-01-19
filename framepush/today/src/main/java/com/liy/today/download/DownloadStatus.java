package com.liy.today.download;

/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class DownloadStatus {
    /**
     * 下载过程报错，无法完成下载
     */
    public final static int ERROR_UNKNOWN = 1000;

    /**
     * 下载文件处理报错
     */
    public final static int ERROR_FILE_ERROR = 1001;

    /**
     * 下载HTTP请求状态出错
     */
    public final static int ERROR_UNHANDLED_HTTP_CODE = 1002;

    /**
     * 下载文件数据出错
     */
    public final static int ERROR_HTTP_DATA_ERROR = 1004;

    /**
     * 下载空间不足
     */
    public final static int ERROR_INSUFFICIENT_SPACE = 1005;

    /**
     * 下载文件已存在
     */
    public final static int ERROR_FILE_ALREADY_EXISTS = 1006;

    /**
     * 文件MD5验证失败
     */
    public final static int ERROR_FILE_MD5_CHECK_FAIL = 1007;
}
