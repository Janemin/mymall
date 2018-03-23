<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<meta charset="utf-8"/>
<body>
<h2>Hello World!</h2>

<p>文件上传</p>
<form name="form1" action="/manage/product/upload.do" method="post" ENCTYPE="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="上传" />
</form>

<br>
<br>
<br>
<p>富文本文件上传</p>
<form name="form1" action="/manage/product/richtext_img_upload.do" method="post" ENCTYPE="multipart/form-data">
    <input type="file" name="uploadFile">
    <input type="submit" value="上传" />
</form>

</body>
</html>
