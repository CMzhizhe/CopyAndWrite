package com.gxx.copyandwrite.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private static FileUtils fileUtils = null;

    public static FileUtils getInstance(){
        if(fileUtils == null){
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }

    /**
     * 将assets下的文件放到sd指定目录下
     * @param context      上下文
     * @param assetsPath   assets下的路径
     */
    public void putAssetsToSDCard(Context context, String assetsPath){
        AssetManager assetManager = context.getAssets();
        String sdCardPath = context.getCacheDir().getAbsolutePath();
        try {
            String files[] = assetManager.list(assetsPath);
            if (files.length == 0) {
                // 说明assetsPath为空,或者assetsPath是一个文件
                InputStream is = assetManager.open(assetsPath);
                byte[] mByte = new byte[1024];
                // 判断文件夹是否存在
                String targetDirsPath = sdCardPath + File.separator + assetsPath.substring(0,assetsPath.lastIndexOf("/"));
                File targetFileDir = new File(targetDirsPath);
                if (!targetFileDir.exists()){
                    targetFileDir.mkdirs();
                }

                int bt = 0;
                File file = new File(targetFileDir + File.separator + assetsPath.substring(assetsPath.lastIndexOf('/')));
                if (!file.exists()) {
                    // 创建文件
                    file.createNewFile();
                } else {
                    //已经存在直接退出
                    return;
                }

                // 写入流
                FileOutputStream fos = new FileOutputStream(file);
                // assets为文件,从文件中读取流
                while ((bt = is.read(mByte)) != -1) {
                    // 写入流到文件中
                    fos.write(mByte, 0, bt);
                }

                // 刷新缓冲区
                fos.flush();
                // 关闭读取流
                is.close();
                // 关闭写入流
                fos.close();
            } else {
                // 当mString长度大于0,说明其为文件夹
                sdCardPath = sdCardPath + File.separator + assetsPath;
                File file = new File(sdCardPath);
                if (!file.exists()) {
                    // 在sd下创建目录
                    file.mkdirs();
                }

                // 进行递归
                for (String stringFile : files) {
                    putAssetsToSDCard(context, assetsPath + File.separator + stringFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 删除文件，可以是文件或文件夹
     * @param delFile 要删除的文件夹或文件名
     * @return 删除成功返回true，否则返回false
     */
    public boolean delete(String delFile) {
        File file = new File(delFile);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile())
                return deleteSingleFile(delFile);
            else
                return deleteDirectory(delFile);
        }
    }

    /** 删除单个文件
     * @param filePath$Name 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /** 删除目录及目录下的文件
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String filePath) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator))
            filePath = filePath + File.separator;
        File dirFile = new File(filePath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (File file : files) {
            // 删除子文件
            if (file.isFile()) {
                flag = deleteSingleFile(file.getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (file.isDirectory()) {
                flag = deleteDirectory(file
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
}
