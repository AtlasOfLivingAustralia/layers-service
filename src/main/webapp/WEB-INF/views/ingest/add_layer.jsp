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
file name: ${filename}

<br/>
<table>
    <tr>
        <td>
            Uploaded file review
            <iframe src="${test_url}" width="800" height="600"></iframe>
        </td>
    </tr>

</table>

<br/>Layer created: <b>${has_layer}</b>&nbsp;
<c:if test="${layer_creation != null}"><br/><b>********* LAYER CREATING IN PROGRESS *******</b><br/></c:if>

<form method="POST"
      action="../add_layer/${raw_id}">

    <label for="name">Name:</label> <br/>
    <input type="text" id="name" name="name" value="${name}" maxlength="150"/>
    <br/>

    <label for="displayname">Display name:</label> <br/>
    <input type="text" id="displayname" name="displayname" value="${displayname}" maxlength="150"/>
    <br/>

    <label for="description">Description:</label> <br/>
    <textarea id="description" name="description" cols="150" rows="10">${description}</textarea>
    <br/>

    <label for="type">Type:</label> <br/>
    <select id="type" name="type">
        <option value="Contextual"
                <c:if test="${type == 'Contextual'}">selected</c:if> >Contextual
        </option>
        <option value="Environmental"
                <c:if test="${type == 'Environmental'}">selected</c:if> >Environmental
        </option>
    </select>
    <br/>

    <label for="domain">Domain:</label> <br/>
    <select id="domain" name="domain">
        <option value="Terrestrial"
                <c:if test="${domain == 'Terrestrial'}">selected</c:if> >Terrestrial
        </option>
        <option value="Marine"
                <c:if test="${domain == 'Marine'}">selected</c:if> >Marine
        </option>
        <option value="Terrestrial,Marine"
                <c:if test="${domain == 'Terrestrial,Marine'}">selected</c:if> >Terrestrial,Marine
        </option>
    </select>
    <br/>

    <label for="source">Source (e.g. organisation name):</label> <br/>
    <input type="text" id="source" name="source" value="${source}" maxlength="150"/>
    <br/>

    <label for="scale">Scale (e.g. "0.01 degree (~1km)", "> 1:150,000") :</label> <br/>
    <input type="text" id="scale" name="scale" value="${scale}" maxlength="20"/>
    <br/>

    <label for="environmentalvaluemin">Min Environmental value (Environmental Only)</label> <br/>
    <input type="text" id="environmentalvaluemin" name="environmentalvaluemin" value="${environmentalvaluemin}"
           readonly/>
    <br/>

    <label for="environmentalvaluemax">Max Environmental value (Environmental Only)</label> <br/>
    <input type="text" id="environmentalvaluemax" name="environmentalvaluemax" value="${environmentalvaluemax}"
           readonly/>
    <br/>

    <label for="environmentalvalueunits">Environmental value units (Environmental Only)</label> <br/>
    <input type="text" id="environmentalvalueunits" name="environmentalvalueunits" value="${environmentalvalueunits}"
           maxlength="150"/>
    <br/>

    <label for="metadatapath">Metadata path (e.g. URL to original metadata, if available)</label> <br/>
    <input type="text" id="metadatapath" name="metadatapath" value="${metadatapath}" maxlength="300"/>
    <br/>

    Existing classifications:
    <select>
        <c:forEach items="${classification}" var="classifications">
            <option>{$classification}</option>
        </c:forEach>
    </select>
    <br/>

    <label for="classification1">Classification 1 (e.g. choose from existing or enter a new classification1, e.g.
        Climate)</label> <br/>
    <input type="text" id="classification1" name="classification1" value="${classification1}" maxlength="150"/>
    <br/>

    <label for="classification2">Classification 2 (e.g. choose from existing or enter a new classification1, e.g.
        Humidity)</label> <br/>
    <input type="text" id="classification2" name="classification2" value="${classification2}" maxlength="150"/>
    <br/>

    <label for="mddatest">Metadata date (e.g. "2011-08-23") </label> <br/>
    <input type="text" id="mddatest" name="mddatest" value="${mddatest}" maxlength="30"/>
    <br/>

    <label for="citation_date">Citation date (e.g. "2011-08-23")</label> <br/>
    <input type="text" id="citation_date" name="citation_date" value="${citation_date}" maxlength="30"/>
    <br/>

    <label for="datalang">Data language (e.g. "eng")</label> <br/>
    <input type="text" id="datalang" name="datalang" value="${datalang}" maxlength="5"/>
    <br/>

    <label for="respparty_role">Responsible party role (e.g. "Custodian")</label> <br/>
    <input type="text" id="respparty_role" name="respparty_role" value="${respparty_role}" maxlength="30"/>
    <br/>

    <label for="licence_level">Licence level</label> <br/>
    <input type="text" id="licence_level" name="licence_level" value="${licence_level}"/>
    <br/>

    <label for="licence_link">Licence link</label> <br/>
    <select id="licence_link" name="licence_link">
        <option value="1"
                <c:if test="${licence_link == 1}">selected</c:if> > permission to distribute, see Licence notes
        </option>
        <option value="2"
                <c:if test="${licence_link == 2}">selected</c:if> >Varies, see Licence notes
        </option>
        <option value="3"
                <c:if test="${licence_link == 3}">selected</c:if> >Permitted to distribute, see Licence notes
        </option>
    </select>
    <br/>

    <label for="licence_notes">Licence notes (details of the licence, e.g. "CC BY")</label> <br/>
    <textarea id="licence_notes" name="licence_notes" cols="150" rows="10">${licence_notes}</textarea>
    <br/>

    <label for="source_link">Source link (URL to source data or information, if available)</label> <br/>
    <input type="text" id="source_link" name="source_link" value="${source_link}" maxlength="300"/>
    <br/>

    <label for="keywords">Keywords (Used in layer searches, e.g. "solar, sun"): </label> <br/>
    <input type="text" id="keywords" name="keywords" value="${keywords}" maxlength="256"/>
    <br/>

    <label for="notes">Notes (information about the layer): </label> <br/>
    <textarea id="notes" name="notes" cols="150" rows="10">${notes}</textarea>
    <br/>

    <label for="enabled">Enabled (makes the layer available for use, disable to remove layers from use)</label>
    <input type="checkbox" id="enabled" name="enabled" checked="${enabled}"/>
    <br/>

    <input type="submit" class="button" value=${has_layer ? "Update Layer" : "Add Layer"}/>

    <input type="hidden" name="raw_id" value="${raw_id}"/>

    <input type="hidden" name="id" value="${id}"/>

    <label for="minlongitude">Minimum Longitude</label> <br/>
    <input type="text" id="minlongitude" name="minlongitude" value="${minlongitude}" maxlength="256" readonly/>
    <br/>

    <label for="maxlongitude">Maxium Longitude</label> <br/>
    <input type="text" id="maxlongitude" name="maxlongitude" value="${maxlongitude}" maxlength="256" readonly/>
    <br/>

    <label for="minlatitude">Minimum Latitude</label> <br/>
    <input type="text" id="minlatitude" name="minlatitude" value="${minlatitude}" maxlength="256" readonly/>
    <br/>

    <label for="maxlatitude">Maximum Latitude</label> <br/>
    <input type="text" id="maxlatitude" name="maxlatitude" value="${maxlatitude}" maxlength="256" readonly/>
    <br/>
</form>


<c:if test="${has_layer}">
    Existing fields
    <table>
        <tr>
            <td>Field Id</td>
            <td>Field name</td>
            <td>Field description</td>
        </tr>
        <c:forEach items="${fields}" var="item">
            <tr>
                <td>{$item.id}</td>
                <td>{$item.name}</td>
                <td>{$item.desc}</td>
                <td><a href="">edit</a></td>
                <td><a href="">delete</a></td>
            </tr>
        </c:forEach>
    </table>
    <a href="../add_field/${raw_id}">Add new Field</a>
</c:if>
</body>
</html>
