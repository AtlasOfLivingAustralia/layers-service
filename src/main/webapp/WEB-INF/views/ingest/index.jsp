<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title></title>
</head>
<body>

<br/>
Upload a new grid file (zipped bil, hdr with prj) or a new shape file (zipped shape file with prj)
<form method="POST" enctype="multipart/form-data"
      action="ingest/upload">
    File to upload: <input type="file" name="file">
    <br/>
    <input type="submit" value="Upload"> Press here to upload the file!
</form>

<br/><br/>
Uploaded files without layers or fields
<br/>
<c:if test="${fn:length(files) > 0}">
    <table>
        <tr>
            <td>Raw Id</td>
            <td>Filename</td>
            <td>Layer Id</td>
        </tr>
        <c:forEach var="item" items="${files}">
            <tr>
                <td>${item.raw_id}</td>
                <td>${item.filename}</td>
                <td>${item.layer_id}</td>
                <td>
                    <c:forEach items="${item.fields}" var="field">
                        ${field}<br/>
                    </c:forEach>
                </td>
                <td><a href="ingest/add_layer/${item.raw_id}">edit</a></td>
                <td><a href="ingest/delete_layer/${item.raw_id}">delete</a></td>
            </tr>
        </c:forEach>
    </table>
</c:if>

</body>
</html>
