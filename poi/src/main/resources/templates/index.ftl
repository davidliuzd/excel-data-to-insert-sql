<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title></title>
</head>
<body style="padding-left:15%;padding-top:20px;">
<h1>上传文件</h1>
<form method="post" enctype="multipart/form-data"
  action="/upload">
  	<input type="hidden" name="insertBath" value="false"  />
  	<div>表名：<input type="text" name="tableName"  value="`user`"/> </div>
  	<div>列名：<input type="text" name="columns" value="`name`,`age`,`email`" /> </div>
    <div>文件：<input type="file" name="file" accept=".xls,.xlsx" /> </div>
    <div>上传：<input type="submit" value="上传Excel文件" /></div>
</form>
</body>
</html>