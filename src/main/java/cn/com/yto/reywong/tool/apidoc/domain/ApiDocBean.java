package cn.com.yto.reywong.tool.apidoc.domain;

import java.io.File;

/**
 * Created by wangrui on 2017/8/10.
 */
public class ApiDocBean {
    private String name;
    private String version;
    private String description;
    private String title;
    private String url;
    private String sampleUrl;
    private File apiDocPath;
    private String apiDocWeb;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSampleUrl() {
        return sampleUrl;
    }

    public void setSampleUrl(String sampleUrl) {
        this.sampleUrl = sampleUrl;
    }

    public File getApiDocPath() {
        return apiDocPath;
    }

    public void setApiDocPath(File apiDocPath) {
        this.apiDocPath = apiDocPath;
    }

    public String getApiDocWeb() {
        return apiDocWeb;
    }

    public void setApiDocWeb(String apiDocWeb) {
        this.apiDocWeb = apiDocWeb;
    }
}
