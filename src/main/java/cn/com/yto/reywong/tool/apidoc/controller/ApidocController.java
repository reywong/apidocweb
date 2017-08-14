package cn.com.yto.reywong.tool.apidoc.controller;

import cn.com.yto.reywong.tool.apidoc.domain.ApiDocBean;
import cn.com.yto.reywong.tool.apidoc.domain.AppResult;
import cn.com.yto.reywong.tool.apidoc.util.ApiDocUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wangrui on 2017/8/10.
 */
@Controller
@RequestMapping(value = "/apidocController")
public class ApidocController {

    /**
     * 出售化apiDoc
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/index")
    public String index(Model model) {
        String result = "index";
        List<ApiDocBean> list = ApiDocUtil.getApiDocList();
        model.addAttribute("apidoc", list);
        return result;
    }

    /**
     * 添加新的文档接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/addApiDoc")
    public String addApiDoc(HttpServletRequest request, Model model) {
        String result = "redirect:/apidocController/index";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            //构建apidoc.json
            String name = multiRequest.getParameter("name");
            String version = multiRequest.getParameter("version");
            String description = multiRequest.getParameter("description");
            String title = multiRequest.getParameter("title");
            String url = multiRequest.getParameter("url");
            String sampleUrl = multiRequest.getParameter("sampleUrl");
            ApiDocBean apiDocBean = new ApiDocBean();
            apiDocBean.setName(name);
            apiDocBean.setVersion(version);
            apiDocBean.setDescription(description);
            apiDocBean.setTitle(title);
            apiDocBean.setUrl(url);
            apiDocBean.setSampleUrl(sampleUrl);

            AppResult appResult = new AppResult(false, "请上传apidoc接口文件", null);
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile multipartFile = multiRequest.getFile(iter.next());
                appResult = ApiDocUtil.addApiDoc(apiDocBean, multipartFile);
                model.addAttribute("appResult", appResult);
            }
            if (appResult.getResultFlag()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result = "redirect:/apidoc/" + name + "/index.html";
            }
        }
        return result;
    }

    @RequestMapping(value = "/updateApiDoc")
    public String updateApiDoc(HttpServletRequest request, Model model) {
        String result = "redirect:/apidocController/index";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

            //构建apidoc.json
            String name = multiRequest.getParameter("name");
            String version = multiRequest.getParameter("version");
            String description = multiRequest.getParameter("description");
            String title = multiRequest.getParameter("title");
            String url = multiRequest.getParameter("url");
            String sampleUrl = multiRequest.getParameter("sampleUrl");
            ApiDocBean apiDocBean = new ApiDocBean();
            apiDocBean.setName(name);
            apiDocBean.setVersion(version);
            apiDocBean.setDescription(description);
            apiDocBean.setTitle(title);
            apiDocBean.setUrl(url);
            apiDocBean.setSampleUrl(sampleUrl);

            AppResult appResult = new AppResult(false, "操作失败");
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                //取得上传文件
                MultipartFile multipartFile = multiRequest.getFile(iter.next());
                appResult = ApiDocUtil.updateApiDoc(apiDocBean, multipartFile);
                model.addAttribute("appResult", appResult);
            }
            if (appResult.getResultFlag()) {
                result = "redirect:/apidoc/" + name + "/index.html";
            }
        }
        return result;
    }

    @RequestMapping(value = "/deleteApiDoc")
    public String deleteApiDoc(String name, Model model) {
        String result = "redirect:/apidocController/index";
        model.addAttribute("appResult", ApiDocUtil.deleteApiDoc(name));
        return result;
    }

    @RequestMapping(value = "/getApiDoc", method = {RequestMethod.POST})
    @ResponseBody
    public ApiDocBean getApiDoc(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        return ApiDocUtil.getApiDoc(name);
    }

}
