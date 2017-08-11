/**
 *
 * File Desc:    
 *
 * Product AB:   热风投资有限公司
 *
 * Module Name:  
 *
 * Author:       wangrui
 *
 * Create:       13-3-28-下午3:09
 *
 * History:      13-3-28-下午3:09 modify  by  wangrui
 */
package cn.com.yto.reywong.tool.apidoc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileParserTool {
    private static final Logger logger = LoggerFactory.getLogger(FileParserTool.class);

    /**
     * 获得文件地址</br>
     *
     * @param fileName 文件名
     * @return 返回文件的路径
     */
    public static String getUrl(String fileName) {
        String path = "";
        try {
            path = FileParserTool.class.getClassLoader().getResource(fileName).toString().substring(6);
            path = path.replace("%20", " ");// 如果你的文件路径中包含空格，是必定会报错的
        } catch (Exception e) {
            path = System.getProperty("user.dir") + File.separator + fileName;
        }

        //linux下系统
        if (path != null) {
            if (!path.contains(":") && !path.startsWith(File.separator)) {
                path = File.separator + path;
            }
        }
        return path;
    }


    /**
     * 读取文件数据
     *
     * @param file 文件名称
     * @return 返回文件内容
     */
    public static String getFileText(File file) {
        String result = null;
        InputStreamReader fr = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        try {
            fr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (FileNotFoundException e) {
            logger.error("文件不存在", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("文件编码有误", e);
        } catch (IOException e) {
            logger.error("I/O读取异常", e);
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                logger.error("解析URL：" + file.getPath());
                logger.error("数据流关闭异常", e);

            }
        }
        return result;
    }

    /**
     * 文件数据
     *
     * @param fileName 文件名称
     * @param data     要写入的文件内容，清除文件原有内容
     * @return 返回文件内容
     */
    public static void writeFileText(String data, String fileName) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8"));
            writer.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    /**
     * 向文件后添加内容 </br>
     *
     * @param data 要添加的内容
     * @param url  文件名
     */
    public static void appendFileText(String data, String url) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(url), true), "UTF-8"));
            writer.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    /**
     * 清除文件的内容</br>
     *
     * @param fileName 文件名
     */
    public static void clear(String fileName) {
        BufferedWriter writer = null;
        String url = getUrl(fileName);
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(url))));
            writer.write("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            System.out.println("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            return false;
        }
    }

}
