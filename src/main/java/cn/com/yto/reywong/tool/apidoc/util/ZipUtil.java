package cn.com.yto.reywong.tool.apidoc.util;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;


/**
 * Created by wangrui on 2017/8/14.
 */
public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 递归压缩文件夹
     *
     * @param srcPath  源文件夹
     * @param destPath 目标文件夹
     * @throws Exception
     */
    public static void zip(String srcPath, String destPath) throws Exception {
        Assert.notNull(srcPath, "源文件不能为空");
        Assert.notNull(destPath, "目标文件不能为空");
        ZipFile zipFile = new ZipFile(destPath);
        String folderToAdd = srcPath;
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        zipFile.addFolder(folderToAdd, parameters);
    }
}
