package cn.com.yto.reywong.tool.apidoc.util;

import cn.com.yto.reywong.tool.apidoc.domain.ApiDocBean;
import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangrui on 2017/8/10.
 */
public class ApiDocUtil {

    /**
     * 获取所有接口文档
     *
     * @return
     */
    public static List<ApiDocBean> getApiDocList() {
        List<ApiDocBean> result = new ArrayList<ApiDocBean>();
        List<File> apiDocFiles = getApiDocFile();
        if (apiDocFiles != null && apiDocFiles.size() > 0) {
            for (int i = 0; i < apiDocFiles.size(); i++) {
                File apiDocFile = apiDocFiles.get(i);
                String apiDocJson = FileParserTool.getFileText(apiDocFile);
                ApiDocBean apiDocBean = JSON.parseObject(apiDocJson, ApiDocBean.class);
                apiDocBean.setApiDocPath(apiDocFile.getParentFile());
                result.add(apiDocBean);
            }
        }
        return result;
    }

    /**
     * 获取所有接口地址
     *
     * @return
     */
    public static List<File> getApiDocFile() {
        List<File> result = new ArrayList<File>();
        String apidocHome = FileParserTool.getUrl("apidoc");
        if (!StringUtils.isEmpty(apidocHome)) {
            File file = new File(apidocHome);
            File[] apidocs = file.listFiles();
            if (apidocs != null && apidocs.length > 0) {
                for (int i = 0; i < apidocs.length; i++) {
                    if (apidocs[i].isDirectory()) {
                        File apidoc = apidocs[i].listFiles()[0];
                        result.add(new File(apidoc.getPath() + File.separator + "apidoc.json"));
                    }
                }
            }
        }
        return result;
    }

    /**
     * 添加接口
     *
     * @param apiDocBean
     * @param multipartFile
     * @return
     */
    public static boolean addApiDoc(ApiDocBean apiDocBean, MultipartFile multipartFile) {
        boolean result = false;
        //是否存在返回
        String name = apiDocBean.getName();
        if (!StringUtils.isEmpty(name)) {
            List<ApiDocBean> apiDocBeanList = getApiDocList();
            if (apiDocBeanList != null && apiDocBeanList.size() > 0) {
                for (int i = 0; i < apiDocBeanList.size(); i++) {
                    String temp_name = apiDocBeanList.get(i).getName();
                    if (temp_name.equals(name)) {
                        return false;
                    }
                }
            }

            //如果不存在则添加
            String datetime = String.valueOf(new Date().getTime());
            String apiDocUrl = FileParserTool.getUrl("apidoc") + datetime + File.separator + "apidoc" + File.separator;
            String apiDocJsonUrl = apiDocUrl + "apidoc.json";
            File apiDocUrlFile = new File(apiDocUrl);
            File apiDocJsonFile = new File(apiDocJsonUrl);
            //创建 apidoc.json文件
            if (!apiDocUrlFile.exists()) {
                apiDocUrlFile.mkdirs();
                if (!apiDocJsonFile.exists()) {
                    try {
                        apiDocJsonFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (apiDocJsonFile.exists()) {
                    FileParserTool.appendFileText(JSON.toJSONString(apiDocBean), apiDocJsonUrl);
                }

                //创建apidoc.java文件
                if (multipartFile != null) {
                    //取得当前上传文件的文件名称
                    String myFileName = multipartFile.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if (myFileName.trim() != "") {
                        //重命名上传后的文件名
                        String fileName = apiDocUrl + "apidoc.java";
                        //定义上传路径
                        File localFile = new File(fileName);
                        try {
                            multipartFile.transferTo(localFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //执行node.js命令
                Runtime runtime = Runtime.getRuntime();
                try {
                    String destPath = apiDocUrl.substring(0, apiDocUrl.lastIndexOf("WEB-INF")) + "apidoc/" + name;
                    runtime.exec("cmd /c apidoc -i " + apiDocUrl + " -o " + destPath);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 删除apiDoc
     *
     * @param name
     * @return
     */
    public static boolean deleteApiDoc(String name) {
        boolean result = false;
        if (!StringUtils.isEmpty(name)) {
            List<ApiDocBean> apiDocBeanList = getApiDocList();
            if (apiDocBeanList != null && apiDocBeanList.size() > 0) {
                for (int i = 0; i < apiDocBeanList.size(); i++) {
                    String temp_name = apiDocBeanList.get(i).getName();
                    if (name.equals(temp_name)) {
                        result = FileParserTool.delete(apiDocBeanList.get(i).getApiDocPath().getParentFile().getPath());
                        if (result) {
                            String apidocHome = FileParserTool.getUrl("apidoc");
                            File apiDocWeb = new File(apidocHome.substring(0, apidocHome.lastIndexOf("WEB-INF")) + "apidoc" + File.separator + name);
                            if (apiDocWeb.exists()) {
                                result = FileParserTool.delete(apiDocWeb.getPath());
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 更新apiDoc
     *
     * @param apiDocBean
     * @param multipartFile
     * @return
     */
    public static boolean updateApiDoc(ApiDocBean apiDocBean, MultipartFile multipartFile) {
        boolean result = false;
        String name = apiDocBean.getName();
        if (!StringUtils.isEmpty(name)) {
            result = deleteApiDoc(name);
            if (result) {
                addApiDoc(apiDocBean, multipartFile);
            }
        }
        return result;
    }

    /**
     * 获取apidoc
     *
     * @param name
     * @return
     */
    public static ApiDocBean getApiDoc(String name) {
        ApiDocBean result = new ApiDocBean();
        if (!StringUtils.isEmpty(name)) {
            List<ApiDocBean> apiDocBeanList = getApiDocList();
            if (apiDocBeanList != null && apiDocBeanList.size() > 0) {
                for (int i = 0; i < apiDocBeanList.size(); i++) {
                    ApiDocBean temp_apiDocBean = apiDocBeanList.get(i);
                    if (name.equals(temp_apiDocBean.getName())) {
                        result = temp_apiDocBean;
                    }

                }
            }
        }
        return result;
    }
}
