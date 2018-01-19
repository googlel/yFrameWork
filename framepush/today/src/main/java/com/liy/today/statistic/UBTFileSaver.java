package com.liy.today.statistic;

import android.text.TextUtils;

import com.liy.today.APPEnvironment;
import com.liy.today.base.BaseApplication;
import com.liy.today.utils.FileUtils;
import com.liy.today.utils.GsonUtils;
import com.liy.today.utils.LogUtils;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * The creator is qiujie && E-mail: mailtoqiu@163.com
 *
 * @author qiujie
 * @date 15/10/25
 *
 */
final class UBTFileSaver extends Thread {

    private static final String TARGET_FILE = "/file.zip";
    private static final int FILE_SIZE = 16 * 1024;
    private ConcurrentLinkedQueue<UBTPageEvent> mVector;
    private AtomicBoolean mCloseFlag;
    private FileWriter mFileWriter;
    private String mFolderPath;
    private File mTargetFile;
    private boolean mUploading;

    public interface FileUploadService {
        @Multipart
        @POST("/acquire/report/terminal")
        Call<HashMap<String, Object>> upload(@Part("file\"; filename=\"file.zip\"") RequestBody file);
    }

    /**
     * UBTFileSaver
     */
    public UBTFileSaver() {
        mFolderPath = BaseApplication.getBaseApplication().getExternalCacheDir() + "/log";
        mCloseFlag = new AtomicBoolean(false);
        mVector = new ConcurrentLinkedQueue<>();
        mUploading = false;
    }

    /**
     * 关闭文件存储器
     */
    public void shutDown() {
        mCloseFlag.set(true);
    }

    /**
     * enqueueEvent
     * @param event event
     */
    public synchronized void enqueueEvent(UBTPageEvent event) {
        mVector.add(event);
    }

    /**
     * 创建文件写入通道
     * @param fileName fileName
     * @return FileWriter
     */
    private FileWriter createWriterBuffer(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }

        File file = new File(fileName);
        if(file.getParentFile()!=null){
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer;
    }

    @Override
    public void run() {
        while(!mCloseFlag.get()) {
            try {
                File file = new File(mFolderPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                File zipFile = new File(mFolderPath + TARGET_FILE);
                if (zipFile.exists() && !mCloseFlag.get() && !mUploading) {
                    uploadFile(zipFile.getPath());
                }

                if (mVector.isEmpty()) {
                    continue;
                }

                UBTPageEvent event = mVector.poll();
                if (event == null) {
                    continue;
                }

                String result = GsonUtils.toJson(event);

                LogUtils.i("UBTUploader", "File.zip is uploading---json!"+result);
                if (TextUtils.isEmpty(result)) {
                    continue;
                }

                result += "\n";

                if (mFileWriter == null) {
                    int count = 1;
                    while(!mCloseFlag.get()) {
                        mTargetFile = new File(mFolderPath + "/" + count + ".json");
                        if (mTargetFile.exists() && mTargetFile.length() > FILE_SIZE) {
                            zipAndUploadFile(mTargetFile.getPath(), mFolderPath + TARGET_FILE);
                            count ++;
                        } else if (!mCloseFlag.get()){
                            mFileWriter = createWriterBuffer(mTargetFile.getPath());
                            break;
                        }
                    }
                }

                if (mFileWriter != null && !mCloseFlag.get()) {
                    mFileWriter.write(result);
                    mFileWriter.flush();
                    mFileWriter.close();
                    mFileWriter = null;
                }

                if (mTargetFile.length() > FILE_SIZE && !mCloseFlag.get()) {
                    zipAndUploadFile(mTargetFile.getPath(), mFolderPath + TARGET_FILE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mFileWriter != null) {
            try {
                mFileWriter.flush();
                mFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void zipAndUploadFile(String filePath, String zipPath) {
        File file = new File(zipPath);
        if (file.exists()) {
            return;
        }

        doZipWork(filePath, zipPath);
        FileUtils.deleteFile(filePath);
        uploadFile(zipPath);
    }

    private void doZipWork(String filePath, String zipPath) {
        ZipArchiveOutputStream zos = null;
        try {
            if (!mCloseFlag.get()) {
                zos = (ZipArchiveOutputStream) new ArchiveStreamFactory()
                        .createArchiveOutputStream("zip", new FileOutputStream(zipPath));
                zos.setEncoding("utf-8");
                ZipArchiveEntry entry = new ZipArchiveEntry("event.json");
                zos.putArchiveEntry(entry);
                FileInputStream in = new FileInputStream(filePath);
                if (!mCloseFlag.get()) {
                    IOUtils.copy(in, zos);
                }
                zos.closeArchiveEntry();
                zos.close();
                in.close();
            }
        } catch (ArchiveException | IOException e) {
            e.printStackTrace();
        }
    }



    private void uploadFile(String filePath) {

        LogUtils.i("UBTUploader", "File.zip is uploading!"+filePath);
        final File uploadFile = new File(filePath);
        if (!uploadFile.exists()) {
            return;
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(APPEnvironment.isRelease()
                        ? "http://ubt.fx.yaomaiche.com"
                        : "http://10.16.35.89:8080")
                .addConverterFactory(GsonConverterFactory.create());

        RequestBody requestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);

        Call<HashMap<String, Object>> call = builder
                .build()
                .create(FileUploadService.class)
                .upload(requestBody);
        try {
            // synchronous call
            Response resp = call.execute();
            LogUtils.i("UBTUploader", "Upload "
                    +  (resp.isSuccessful() ? "success!" : "fail!"));
        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            FileUtils.deleteFile(uploadFile.getPath());
        }
    }

}
