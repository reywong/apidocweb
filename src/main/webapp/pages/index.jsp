<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <title>APIDOC-TOOL</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8"/>
    <link href='../resource/css/bootstrap.min.css' rel="stylesheet"/>
    <script type="text/javascript" src="../resource/js/jquery.min.js"></script>
    <script type="text/javascript">
        function addApiDoc() {
            var name = $("#name").val();
            var apidoc = $("#apidoc").val();
            if (name == null || trim(name) == "") {
                alert("name必填");
            } else if (!checkName(name)) {
                alert("该项目已经存在，请重新命名");
            } else if (apidoc == null || trim(apidoc) == "") {
                alert("请上传apidoc描述文件");
            } else {
                $("#myForm").submit();
            }
        }

        function checkName(name) {
            var result = false;
            $.ajax({
                url: '/apidocController/getApiDoc',
                type: 'POST', //GET
                async: false,    //或false,是否异步
                data: {
                    name: name
                },
                timeout: 5000,    //超时时间
                dataType: 'json',
                success: function (data) {
                    if (data != null) {
                        result = true
                    }
                }
            });
            return result;

        }

        function updateApiDoc(name) {
            $.ajax({
                url: '/apidocController/getApiDoc',
                type: 'POST', //GET
                async: true,    //或false,是否异步
                data: {
                    name: name
                },
                timeout: 5000,    //超时时间
                dataType: 'json',
                success: function (data) {
                    if (data != null) {
                        $("#name").val(data.name);
                        $("#version").val(data.version);
                        $("#description").val(data.description);
                        $("#title").val(data.title);
                        $("#url").val(data.url);
                        $("#sampleUrl").val(data.sampleUrl);
                        $("#myForm").attr("action", "/apidocController/updateApiDoc");
                    }
                }

            })
        }
        function deleteApiDoc(name) {
            $("#delete_name").val(name);
            $("#delete_form").submit();
        }

        function trim(str) {
            return str.replace(/(^\s*)|(\s*$)/g, "");
        }

    </script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">文档生成工具</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="/apidocController/index">Home</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</nav>

<div class="container" style="margin-top: 100px">
    <form class="form-horizontal" id="myForm" action="/apidocController/addApiDoc" method="post"
          enctype="multipart/form-data">
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">项目名称（name）<span style="color: red">*</span></label>

            <div class="col-sm-10">
                <input type="text" class="form-control" name="name" id="name" placeholder="项目名称"
                       onkeyup="value=value.replace(/[/W]/g,'') "
                       onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^/d]/g,''))">
            </div>
        </div>
        <div class="form-group">
            <label for="version" class="col-sm-2 control-label">版本号（version）</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" name="version" id="version" value="1.1.0" placeholder="文档版本号">
            </div>
        </div>
        <div class="form-group">
            <label for="description" class="col-sm-2 control-label">描述（description）</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" name="description" id="description" placeholder="接口文档描述">
            </div>
        </div>
        <div class="form-group">
            <label for="title" class="col-sm-2 control-label">标题（title）</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" name="title" id="title" placeholder="接口文档标题">
            </div>
        </div>
        <div class="form-group">
            <label for="url" class="col-sm-2 control-label">接口显示url（url）</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" name="url" id="url" placeholder="接口地址">
            </div>
        </div>
        <div class="form-group">
            <label for="sampleUrl" class="col-sm-2 control-label">接口测试URL（sampleUrl）</label>

            <div class="col-sm-10">
                <input type="text" class="form-control" name="sampleUrl" id="sampleUrl" placeholder="接口线上测试地址">
            </div>
        </div>
        <div class="form-group">
            <label for="apidoc" class="col-sm-2 control-label">上传接口定义（apidoc.java）</label>

            <div class="col-sm-10">
                <input type="file" name="apidoc" id="apidoc">

                <p class="help-block">请上传apidoc的接口描述文件.<a target="_blank" href="http://apidocjs.com/">apidoc帮助文档</a></p>
            </div>
        </div>

    </form>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="button" onclick="addApiDoc()" class="btn btn-default">确定</button>
        </div>
    </div>

</div>

<!--delete form-->
<div style="display: none">
    <form id="delete_form" action="/apidocController/deleteApiDoc">
        <input name="name" id="delete_name">
    </form>
</div>
<c:if test="${not empty apidoc}">
    <div class="container">
        <table class="table table-striped">
            <caption>接口文档列表</caption>
            <tr>
                <th>项目名称</th>
                <th>版本号</th>
                <th>描述</th>
                <th>标题</th>
                <th>接口显示url</th>
                <th>接口测试URL</th>
                <th>接口访问地址</th>
                <th>下载</th>
                <th>操作</th>
            </tr>
            <c:forEach var="ad" items="${apidoc}">
                <tr>
                    <td>
                        <c:out value="${ad.name}"/>
                    </td>
                    <td>
                        <c:out value="${ad.version}"/>
                    </td>
                    <td>
                        <c:out value="${ad.description}"/>
                    </td>
                    <td>
                        <c:out value="${ad.title}"/>
                    </td>
                    <td>
                        <c:out value="${ad.url}"/>
                    </td>
                    <td>
                        <c:out value="${ad.sampleUrl}"/>
                    </td>
                    <td>
                        <a href="/apidoc/<c:out value="${ad.name}"/>/index.html">/apidoc/<c:out value="${ad.name}"/>/index.html</a>
                    </td>
                    <td>
                        <a href="<c:out value="${ad.apiDocJson}"/>">apidoc.json</a>

                        <p/>
                        <a href="<c:out value="${ad.apiDocJava}"/>">apidoc.java</a>

                        <p/>
                        <a href="<c:out value="${ad.apiDocZip}"/>">apidoc.zip</a>
                    </td>
                    <td>
                        <button type="button" class="btn btn-warning"
                                onclick="updateApiDoc('<c:out value="${ad.name}"/>')">修改
                        </button>
                        <button type="button" class="btn btn-danger"
                                onclick="deleteApiDoc('<c:out value="${ad.name}"/>')">删除
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</c:if>


<c:if test="${not empty appResult}">
    <script type="text/javascript">
        alert('<c:out value="${appResult.resultMessage}"/>');
    </script>
</c:if>
</body>
</html>