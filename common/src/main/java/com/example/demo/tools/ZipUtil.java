package com.example.demo.tools;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author zjh
 * @Description ZIP解压缩工具类
 * @date 2020/10/15 11:11
 */
public class ZipUtil {


    /**
     * 缓冲大小
     */
    private static final int BUFFER_SIZE = 1024 * 4;

    /**
     * 压缩
     *
     * @param paths    待压缩文件路径
     * @param fileName 压缩包名称
     */
    public static void zip(String[] paths, String fileName) {

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(fileName))) {
            for (String filePath : paths) {
                //递归压缩文件
                File file = new File(filePath);
                String relativePath = file.getName();
                if (file.isDirectory()) {
                    relativePath += File.separator;
                }
                zipFile(file, relativePath, zos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFile(File file, String relativePath, ZipOutputStream zos) {
        try (InputStream is = new FileInputStream(file)) {
            if (!file.isDirectory()) {
                ZipEntry zp = new ZipEntry(relativePath);
                zos.putNextEntry(zp);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length = 0;
                while ((length = is.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
                zos.flush();
                zos.closeEntry();
            } else {
                String tempPath = null;
                for (File f : file.listFiles()) {
                    tempPath = relativePath + f.getName();
                    if (f.isDirectory()) {
                        tempPath += File.separator;
                    }
                    zipFile(f, tempPath, zos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压缩
     *
     * @param fileName
     * @param path
     */
    public static void unzip(String fileName, String path) {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            ZipFile zf = new ZipFile(new File(fileName));
            Enumeration en = zf.entries();
            while (en.hasMoreElements()) {
                ZipEntry zn = (ZipEntry) en.nextElement();
                if (!zn.isDirectory()) {
                    is = zf.getInputStream(zn);
                    File f = new File(path + zn.getName());
                    File file = f.getParentFile();
                    file.mkdirs();
                    fos = new FileOutputStream(path + zn.getName());
                    int len = 0;
                    byte bufer[] = new byte[BUFFER_SIZE];
                    while (-1 != (len = is.read(bufer))) {
                        fos.write(bufer, 0, len);
                    }
                    fos.close();
                } else {
                    String name = zn.getName();
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        zip(new String[]{"D:/A/feng/feng/src/com/feng/util/ZipUtil.java", "D:/A/feng/feng/src/com/feng/test"}, "E:/test/test.zip");
        unzip("E:/test/test.zip", "E:/test/");
    }
}
