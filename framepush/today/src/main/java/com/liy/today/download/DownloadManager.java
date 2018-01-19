package com.liy.today.download;

import com.liy.today.utils.MD5Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
public class DownloadManager {

    private static DownloadManager mInstance;
    private OkHttpClient mHttpClient;

    /**
     * DownloadManager
     */
    private DownloadManager() {
        mHttpClient = new OkHttpClient();
    }

    /**
     * get DownloadManger
     *
     * @return instance
     */
    public static DownloadManager getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadManager();
        }
        return mInstance;
    }

    /**
     * 下载回调
     */
    public interface DownloadListener {
        /**
         * 下载成功回调
         */
        public void onDownloadSuccess(String filaPath);

        /**
         * 下载失败回调
         */
        public void onDownloadFail(int status);
    }

    /**
     * 开启下载任务
     *
     * @param downloadUnit downloadUnit
     * @param listener     listener
     * @return 是否能够正确下载
     */
    public boolean executeDownload(DownloadUnit downloadUnit, final DownloadListener listener) {
        if (downloadUnit == null || !downloadUnit.checkArgsIllegal() || listener == null) {
            return false;
        }
        Observable.create(new CallOnSubscriber(downloadUnit))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unit -> listener.onDownloadSuccess(unit.getFilePath())
                        , throwable -> {
                    if (throwable instanceof DownloadException) {
                        listener.onDownloadFail(((DownloadException) throwable).getErrorStatus());
                    } else {
                        listener.onDownloadFail(DownloadStatus.ERROR_UNKNOWN);
                    }
                });
        return true;
    }

    /**
     * 下载执行主体
     */
    public class CallOnSubscriber implements Observable.OnSubscribe<DownloadUnit> {

        private DownloadUnit mDownloadUnit;

        /**
         * 多线程下载
         *
         * @param unit unit
         */
        public CallOnSubscriber(DownloadUnit unit) {
            this.mDownloadUnit = unit;
        }

        @Override
        public void call(Subscriber<? super DownloadUnit> subscriber) {

            File targetFile = new File(mDownloadUnit.getFilePath());
            targetFile.getParentFile().mkdirs();

            Request.Builder builder = new Request.Builder()
                    .get()
                    .url(mDownloadUnit.getReqUrl());
            builder.header("Cache-Control", "no-cache");

            Call targetCall = mHttpClient.newCall(builder.build());
            try {
                Response response = targetCall.execute();

                if (response != null && response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    targetFile.createNewFile();
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    BufferedOutputStream bos =
                            new BufferedOutputStream(new FileOutputStream(targetFile));
                    int total = (int) response.body().contentLength();
                    int remaining = total;
                    int readLen = 0;
                    byte[] buf = new byte[total];
                    while (remaining > 0
                            && ((readLen = bis.read(buf, total - remaining, remaining)) != -1)) {
                        remaining -= readLen;
                    }

                    bos.write(buf);
                    bos.flush();
                    bos.close();
                    bis.close();

                    String targetFileMD5 = MD5Utils.getFileMD5(targetFile);
                    if (mDownloadUnit.getMd5Check().equalsIgnoreCase(targetFileMD5)) {
                        subscriber.onNext(mDownloadUnit);
                    } else {
                        DownloadException exception = new DownloadException();
                        exception.setErrorStatus(DownloadStatus.ERROR_FILE_MD5_CHECK_FAIL);
                        subscriber.onError(exception);
                        if (targetFile.exists()) {
                            targetFile.delete();
                        }
                    }
                } else if (response == null || !response.isSuccessful()) {
                    DownloadException exception = new DownloadException();
                    exception.setErrorStatus(DownloadStatus.ERROR_HTTP_DATA_ERROR);
                    subscriber.onError(exception);
                } else {
                    DownloadException exception = new DownloadException();
                    exception.setErrorStatus(DownloadStatus.ERROR_UNHANDLED_HTTP_CODE);
                    subscriber.onError(exception);
                }
            } catch (Exception ex) {
                if (!subscriber.isUnsubscribed()) {
                    DownloadException exception = new DownloadException();
                    exception.setErrorReason(ex.getMessage());
                    exception.setErrorStatus(DownloadStatus.ERROR_UNKNOWN);
                    subscriber.onError(exception);
                }
            }

            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        }
    }

}
