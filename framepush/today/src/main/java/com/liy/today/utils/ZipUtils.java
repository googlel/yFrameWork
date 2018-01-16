package com.liy.today.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The creator is Leone && E-mail: butleone@163.com
 *
 * @author Leone
 * @date 3/3/16
 * @description Edit it! Change it! Beat it! Whatever, just do it!
 */
public class ZipUtils {

    /**
     * 解压zip到指定目录
     *
     * @param zipFile         zipFile
     * @param outputDirectory outputDirectory
     * @throws Exception
     */
    public static void unzip(File zipFile, String outputDirectory) throws Exception {
        FileInputStream fis = new FileInputStream(zipFile);
        unzip(fis, new File(outputDirectory));
        fis.close();
    }

    /**
     * 循环读取流下的zip文件解压到指定目录
     *
     * @param inputStream     inputStream
     * @param outputDirectory outputDirectory
     *                        获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
     *                        当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
     *                        输入流读取完成；
     */
    public static void unzip(InputStream inputStream, File outputDirectory) throws Exception {
        ZipInputStream in = new ZipInputStream(inputStream);
        ZipEntry entry = in.getNextEntry();
        outputDirectory.mkdirs();
        while (entry != null) {
            if (entry.isDirectory()) {
                new File(outputDirectory + File.separator + entry.getName()).mkdirs();
            } else {
                File outFile = new File(outputDirectory + File.separator + entry.getName());
                outFile.getParentFile().mkdirs();

                FileOutputStream out = new FileOutputStream(outFile);
                BufferedOutputStream bos = new BufferedOutputStream(out);
                byte[] buffer = new byte[10 * 1024];
                int length;
                while ((length = in.read(buffer)) != -1) {
                    bos.write(buffer, 0, length);
                }
                bos.close();
                out.close();
            }
            // 读取下一个ZipEntry
            entry = in.getNextEntry();
        }
        in.close();
    }
}
