package com.imyuu.travel.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileUtils {
    public static final String NEW_FILEPATH = (new StringBuilder()).append(Environment.getExternalStorageDirectory()).append("/imyuu/").toString();
    private static final String TAG = "FileUtils";
    private static int BUFFER = 8096; //这里缓冲区我们使用8KB，

    public FileUtils() {
    }

    public static boolean existSDCard()
    {
        return true;
    }
    private static void delete(File file) {
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File afile[] = file.listFiles();
            if (afile == null || afile.length == 0) {
                file.delete();
                return;
            }
            int i = 0;
            do {
                if (i >= afile.length) {
                    file.delete();
                    return;
                }
                delete(afile[i]);
                i++;
            } while (true);
        }
    }

    public static void deletefile(Context context) {
        if (SDcardUtil.hasSDCard() && SDcardUtil.isExternalStorageWritable()) {
            File file = new File(NEW_FILEPATH);
            if (file.exists()) {
                PreferencesUtils.deletePreferencesUtilsValue(context);
                delete(file);
                context.deleteDatabase("downloads.db");
            }
            return;
        } else {
            Log.w("HasSDcard", "Colud't Find SDcard!");
            return;
        }
    }

    public static boolean isExist(String s) {

        boolean flag = (new File(s)).exists();

        return flag;

    }

    public static boolean createDirectory(String s) {

        boolean flag = (new File(s)).mkdir();

        return flag;

    }

    public static void createNoMedia()
    {
        if(!isExist(Config.UU_FILEPATH+".nomedia")) {
            try {
                new File(Config.UU_FILEPATH + ".nomedia").createNewFile();
            }catch(Exception e)
            {

            }
        }
    }
    public static void openFile(File file, Context context) {
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(0x10000000);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static String formatFileSize(long l) {
        DecimalFormat decimalformat = new DecimalFormat("#.00");
        if (l < 1024L) {
            return (new StringBuilder(String.valueOf(decimalformat.format(l)))).append("B").toString();
        }
        if (l < 0x100000L) {
            return (new StringBuilder(String.valueOf(decimalformat.format((double) l / 1024D)))).append("KB").toString();
        }
        if (l < 0x40000000L) {
            return (new StringBuilder(String.valueOf(decimalformat.format((double) l / 1048576D)))).append("MB").toString();
        } else {
            return (new StringBuilder(String.valueOf(decimalformat.format((double) l / 1073741824D)))).append("G").toString();
        }
    }

    public static long getDirSize(File file) {
        long l = 0L;

        if (file != null && file.isDirectory()) {

            File afile[] = file.listFiles();
            int i = afile.length;
            int j = 0;
            while (j < i) {
                File file1 = afile[j];
                if (file1.isFile()) {
                    l += file1.length();
                } else if (file1.isDirectory()) {
                    l = l + file1.length() + getDirSize(file1);
                }
                j++;
            }
        }
        return l;
    }

    public static String sumcache(Context context) {
        File file = context.getFilesDir();
        File file1 = context.getCacheDir();
        File file2 = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).
                append("/imyuu/Cache").toString());
        long l = 0L + getDirSize(file) + getDirSize(file1) + getDirSize(file2);
        if (l > 0L) {
            return formatFileSize(l);
        } else {
            return "";
        }
    }

    // 图片存入到SD卡
    public static boolean storeImageToSDCard(Bitmap colorImage,
                                             String ImageName, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        File imagefile = new File(file, ImageName);
        try {
            imagefile.createNewFile();
            FileOutputStream fos = new FileOutputStream(imagefile);
            colorImage.compress(Bitmap.CompressFormat.PNG, 50, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath, String fileName) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            // 目标目录
            File newfile = new File(newPath);
            // 创建目录
            if (!newfile.exists()) {
                newfile.mkdirs();
            }
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath + fileName);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除目录下所有文件
     *
     * @param dirPath
     * @return
     */
    public static boolean deleteDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(dir.getAbsolutePath() + "/"
                        + aChildren);

            }
        }
        // The directory is now empty so now it can be smoked
        return dir.delete();
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.delete();
    }

    // 读取文本文件中的内容
    public static String readFile(String strFilePath) {
        String content = ""; // 文件内容字符串
        // 打开文件
        File file = new File(strFilePath);
        // 如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            return null;
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(
                            instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    // 分行读取
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.e(TAG, "文件读取异常", e);
                return null;
            } catch (IOException e) {
                Log.e(TAG, "文件读取异常", e);
                return null;
            }
        }
        return content;
    }

    public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            String str = folderPath + entry.getName();
            if (entry.isDirectory()) {
                File desFile = new File(str);
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                continue;
            }
            InputStream in = zf.getInputStream(entry);
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                try {
                    desFile.createNewFile();
                } catch (Exception e) {
                    Log.e("unzip", "创建解压文件失败：" + str, e);
                }
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFFER];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
    }

    public static void unzip(File zipFile, String outpath) {
        try {

            File outputDir = new File(outpath);
            if (!outputDir.exists())
                outputDir.mkdir();
            ZipInputStream in = new ZipInputStream(new FileInputStream(zipFile));
            String outputDirectory = outputDir.getAbsolutePath();
            ZipEntry z;
            File retFile = null, f = null;
            while ((z = in.getNextEntry()) != null) {
                String name = z.getName();
                if (z.isDirectory()) {
                    name = name.substring(0, name.length() - 1);
                    f = new File(outputDirectory + File.separator + name);
                    f.mkdirs();
                } else {
                    f = new File(outputDirectory + File.separator + name);
                    f.createNewFile();
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
                    byte buffer[] = new byte[BUFFER];
                    int realLength;
                    while ((realLength = in.read(buffer)) > 0) {
                        bos.write(buffer, 0, realLength);
                    }
                    bos.flush();
                    bos.close();
                }
                if (retFile == null)
                    retFile = f;
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void renamefile(Context context) {
        if (SDcardUtil.hasSDCard() && SDcardUtil.isExternalStorageWritable()) {
            Log.w("HasSDcard", "Find SDcard Success!");
            File file = new File((new StringBuilder()).append(Environment.getExternalStorageDirectory()).
                    append("/智能导游/").toString());
            File file1 = new File(NEW_FILEPATH);
            if (file.exists()) {
                file.renameTo(file1);
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_MOUNTED",
                        Uri.parse((new StringBuilder("file://")).append(Environment.getExternalStorageDirectory()).toString())));
            }
            return;
        } else {
            Log.w("HasSDcard", "Colud't Find SDcard!");
            return;
        }
    }
}

