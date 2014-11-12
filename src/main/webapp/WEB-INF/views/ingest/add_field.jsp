<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: a
  Date: 24/09/2014
  Time: 1:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>

<c:if test="${error != null}">
    <b>${error}</b>
    <br/>
    <br/>
</c:if>

<table border="1">
    <tr>
        <th>Column name</th>
    </tr>
    <c:forEach items="${columns}" var="column">
        <tr>
            <td>${column}</td>
        </tr>
    </c:forEach>
</table>

<br/>
Uploaded file review
<table>
    <tr>
        <td>abcd</td>
    </tr>
    <tr>
        <td>
            Uploaded file review
            <iframe src="${test_url}" width="800" height="600"></iframe>
        </td>
    </tr>

</table>

Existing fields
<table>
    <tr>
        <td>Field Id</td>
        <td>Field name</td>
        <td>Field description</td>
    </tr>
    <c:forEach items="${fields}" var="item">
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.desc}</td>
            <td><a href="../add_field/${raw_id}/${item.id}">edit</a></td>
            <td><a href="">delete</a></td>
        </tr>
    </c:forEach>
</table>

<br/><br/>
New field:
<form method="POST" action="../add_field/${raw_id}">

    <label for="name">Name:</label> <br/>
    <input type="text" id="name" name="name" value="${name}" maxlength="256"/>
    <br/><br/>

    <label for="desc">Description:</label> <br/>
    <input type="text" id="desc" name="desc" maxlength="256"> ${desc} </textarea>
    <br/><br/>

    <label for="type">Type:</label> <br/>
    <select id="type" name="type">
        <option value="c"
                <c:if test="${type == 'c'}">selected</c:if> >Contextual from shapefile
        </option>
        <option value="a"
                <c:if test="${type == 'a'}">selected</c:if> >Contextual from gridfile (creates types 'a' - classes and
            'b' - individual shapes)
        </option>
        <option value="e"
                <c:if test="${type == 'e'}">selected</c:if> >Environmental
        </option>
    </select>
    <br/><br/>

    <label for="sid">Source id (contextual only; comma delimited list of shape file column names for aggregation to
        create unique objects, e.g. "id")</label> <br/>
    <!--input type="text" id="sid" name="sid" value="${sid}" maxlength="256"/-->
    <select id="sid" name="sid">
        <option value=""
                <c:if test="${sid == '' || sid == null}">selected</c:if> >(none)
        </option>
        <c:forEach items="${columns}" var="column">
            <option value="${column}"
                    <c:if test="${sid == '${column}'}">selected</c:if> >${column}</option>
        </c:forEach>
    </select>
    <br/><br/>

    <label for="sname">Source name (contextual only; column names with optional formatting for the name for each unique
        Objects, e.g. "name (state)"</label> <br/>
    <!--input type="text" id="sname" name="sname" value="${sname}" maxlength="256"/-->
    <select id="sname" name="sname">
        <option value=""
                <c:if test="${sname == '' || sname == null}">selected</c:if> >(none)
        </option>
        <c:forEach items="${columns}" var="column">
            <option value="${column}"
                    <c:if test="${sname == '${column}'}">selected</c:if> >${column}</option>
        </c:forEach>
    </select>
    <br/><br/>

    <label for="sdesc">Source description (contextual only; column names with optional formatting for the description
        for each unique Objects, e.g. "state (area_km)"</label> <br/>
    <!--input type="text" id="sdesc" name="sdesc" value="${sdesc}" maxlength="256"/-->
    <select id="sdesc" name="sdesc">
        <option value=""
                <c:if test="${sdesc == '' || sdesc == null}">selected</c:if> >(none)
        </option>
        <c:forEach items="${columns}" var="column">
            <option value="${column}"
                    <c:if test="${sdesc == '${column}'}">selected</c:if> >${column}</option>
        </c:forEach>
    </select>
    <br/><br/>

    <label for="indb">This field is intended for inclusion in biocache (SOLR index)</label> <br/>
    <input type="checkbox" id="indb" name="indb" checked="${indb}"/>
    <br/><br/>

    <label for="enabled">Enabled (makes the field available for use, disable to remove field from use)</label><br/>
    <input type="checkbox" id="enabled" name="enabled" checked="${enabled}"/>
    <br/><br/>

    <label for="namesearch">This field's objects are included in the objects search (gaz autocomplete). (Contextual
        only)</label><br/>
    <input type="checkbox" id="namesearch" name="namesearch" checked="${namesearch}"/>
    <br/><br/>

    <label for="defaultlayer">When more than ONE field is created from a source layer, use the 'defaultlayer' for
        intersection requests</label><br/>
    <input type="checkbox" id="defaultlayer" name="defaultlayer" checked="${defaultlayer}"/>
    <br/><br/>

    <label for="intersect">Include this Field in calculated Tabulations (Contextual only)</label><br/>
    <input type="checkbox" id="intersect" name="intersect" checked="${intersect}"/>
    <br/><br/>

    <label for="layerbranch">Used by Spatial Portal. When Contextual Layers are listed by their Classifications in a
        tree structure, list objects in the layer as individual leaves in the tree. (Contextual only and
        defaultlayer=true)</label><br/>
    <input type="checkbox" id="layerbranch" name="layerbranch" checked="${layerbranch}"/>
    <br/><br/>

    <label for="analysis">This field is available in the Spatial Portal Tool lists</label><br/>
    <input type="checkbox" id="analysis" name="analysis" checked="${analysis}"/>
    <br/><br/>

    <label for="addtomap">This field is available in the Spatial Portal Add To Map list</label><br/>
    <input type="checkbox" id="addtomap" name="addtomap" checked="${addtomap}"/>
    <br/><br/>

    <c:if test="${layer_creation != null}">
        <br/><b>********* LAYER CREATING IN PROGRESS, CANNOT ADD FIELD *******</b><br/
    </c:if>
    <c:if test="${layer_creation == null}">
        <input type="submit" class="button" value=${is_field ? "Update Field" : "Add Field" }/>
    </c:if>

    <input type="hidden" name="raw_id" value="${raw_id}"/>

    <input type="hidden" name="id" value="${id}"/>

</form>

</body>
</html>
