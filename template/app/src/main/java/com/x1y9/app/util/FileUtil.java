package com.x1y9.app.util;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.x1y9.app.Consts;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class FileUtil {

    public static boolean writeFileQ(File file, String content, boolean setReadPemission) {
        try {
            writeFile(file, content);
            if (setReadPemission)
                file.setReadable(true, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void writeFile(File file, String content) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(content.getBytes());
        } finally {
            stream.close();
        }
    }

    public static String readAssetsQ(Context context, String inFile) {
        try (InputStream stream = context.getAssets().open(inFile)) {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            return new String(buffer,"utf-8");
        } catch (Exception e) {
            return null;
        }
    }

    public static List<File> listFileQ(File file) {
        try {
            return Arrays.asList(file.listFiles());
        }catch (Exception e) {
            return null;
        }
    }

    public static List<String> listQ(String path) {
        return listQ(new File(path));
    }

    public static List<String> listQ(File file) {
        try {
            return Arrays.asList(file.list());
        }catch (Exception e) {
            return null;
        }
    }

    public static String readFileQ(String path) {
        return readFileQ(new File(path));
    }

    public static String readFileQ(File file) {
        try {
            return readFile(file);
        } catch (Exception e) {
            return null;
        }
    }

    public static String readFile(File file) throws IOException {
        return readFile(file, Charset.defaultCharset().name());
    }

    //不能用file.length来初始化数组，因为有些file的length是不准确的，比如/proc/下的部分文件
    public static String readFile(File file, String encode) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(file, encode)) {
            while(scanner.hasNextLine()) {
                sb.append(scanner.nextLine() + System.lineSeparator());
            }
            return sb.toString();
        }
    }

    public static boolean copyFileQ(File source, File destination) {
        try {
            copyFile(source, destination);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void copyFile(File source, File destination) throws IOException {
        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(destination).getChannel();){
            in.transferTo(0, in.size(), out);
        }
    }


    public static InputStream openUriQ(Context context, Uri uri) {
        try {
            return context.getContentResolver().openInputStream(uri);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean unzipQ(File zipFile, File targetDirectory) {
        try {
            unzip(zipFile, targetDirectory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean unzipQ(InputStream zipStream, File targetDirectory) {
        try {
            unzip(zipStream, targetDirectory);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        unzip(new FileInputStream(zipFile), targetDirectory);
    }

    public static void unzip(InputStream zipStream, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(zipStream));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException(dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }


    public static boolean deleteDirQ(File dir) {
        try {
            deleteDir(dir);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static File[] listFiles(File dir, final String pattern) {
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().matches(pattern);
            }
        });
        return files;
    }

    public static String extension(String name) {
        if (name == null) {
            return null;
        }
        else {
            int dot = name.lastIndexOf(".");
            return dot == -1 ? "" : name.substring(dot + 1);
        }
    }

    public static String basename(String name) {
        if (name == null) {
            return null;
        }
        else {
            int dot = name.lastIndexOf(".");
            return dot == -1 ? name : name.substring(0, dot);
        }
    }

    public static boolean deleteFileQ(File file) {
        try {
            if (file.isFile()) {
                file.delete();
                return true;
            }
        }catch (Exception e){
        }
        return false;
    }
}
