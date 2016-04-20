<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="/tld/ala.tld" prefix="ala" %>
<%@include file="../common/top.jsp" %>
<header id="page-header">
    <div class="inner">
        <nav id="breadcrumb">
            <ol>
                <li><a href="http://www.ala.org.au">Home</a></li>
                <li><a href="/">Mapping &#038; analysis</a></li>
                <li class="last">Spatial Layers</li>
            </ol>
        </nav>
        <section id="content-search">
            <h1>Spatial layers</h1>

            <p>Following are a list of ALA Spatial web services.</p>
        </section>
    </div>
    <!--inner-->

</header>
<div class="row">

    <style>
        .wrapword {
            max-width:80px;
            white-space: -moz-pre-wrap !important;
            white-space: -pre-wrap;
            white-space: -o-pre-wrap;
            white-space: pre-wrap;
            word-wrap: break-word;
        }
        td {
            font-size: 14px;
        }
        th {
            font-size: 14px;
        }
    </style>

    <div class="" style="width:100%">
        <section id="results-options">
            <span class="alignleft">

                <span id="resultsReturned"><strong>${fn:length(layers)}</strong> layers found.</span>
            </span>
            <span class="alignright">
                <!--<a class="button caution" href="javascript:reset()" title="Remove all filters and sorting options">Reset list</a>-->
                <a href="#" id="downloadCSVLink" class="button" onclick="downloadLayers('csv');" title="Download metadata for datasets as a CSV file">Download
                    as CSV</a>
                <a href="#" id="downloadJSONLink" class="button" onclick="downloadLayers('json');" title="Download metadata for datasets as a JSON file">Download
                    as JSON</a>

            </span>
        </section>

        <section class="results">
            <c:choose>
                <c:when test="${fn:length(layers) > 0}">
                    <table id="layerstable" class="table-borders" style="width:100%">
                        <thead>
                        <tr>
                            <th>Classification 1</th>
                            <th>Classification 2</th>
                            <th>Display name</th>
                            <th>Short name</th>
                            <th>Description</th>
                            <th>Type</th>
                            <th>Metadata contact organization</th>
                            <th>Keywords</th>
                            <th>Preview</th>
                            <!-- <th>Reference date</th> -->
                            <!--<th>Actions</th>-->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${layers}" var="layer" varStatus="status">
                            <tr style="width:100%">
                                <td>${layer.classification1}</td>
                                <td>${layer.classification2}</td>
                                <td><a href="/layers/more/${layer.name}">${layer.displayname}</a></td>
                                <td style="max-width:80px" class="wrapword">${layer.name}</td>
                                <td class="wrapword">${layer.description}</td>
                                <c:choose>
                                    <c:when test="${layer.type eq 'Environmental'}">
                                        <td class="wrapword">Environmental (gridded) ${layer.scale}</td>
                                    </c:when>
                                    <c:when test="${layer.type eq 'Contextual'}">
                                        <td class="wrapword">Contextual (polygonal) ${layer.scale}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="wrapword">${layer.type} ${layer.scale}</td>
                                    </c:otherwise>
                                </c:choose>
                                <td class="wrapword">${layer.source}</td>
                                <td class="wrapword">${layer.keywords}</td>
                                <td class="wrapword">
                                    <img src="/output/layerthumbs/ALA:${layer.name}.jpg" width="200px"/>
                                    <br/>
                                    <a href="http://spatial.ala.org.au/?layers=${layer.name}">Click to view this
                                        layer</a>
                                </td>
                                <!-- <td>${layer.citation_date}</td> -->
                                <!--
                                    <td>
                                        <a href="layers/edit/${layer.id}">edit</a>
                                    </td>
                                    -->
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <ul>
                        <li>No layers available</li>
                    </ul>
                </c:otherwise>
            </c:choose>
        </section>

        <!--
        <nav class="pagination">
            <ol>
                <li id="prevPage">&laquo Previous</li>

                <li class="currentPage">1</li>
                <li><a href="javascript:gotoPage(2);">2</a></li>
                <li><a href="javascript:gotoPage(3);">3</a></li>
                <li><a href="javascript:gotoPage(4);">4</a></li>
                <li><a href="javascript:gotoPage(5);">5</a></li>
                <li><a href="javascript:gotoPage(6);">6</a></li>

                <li><a href="javascript:gotoPage(7);">7</a></li>
                <li><a href="javascript:gotoPage(8);">8</a></li>
                <li><a href="javascript:gotoPage(9);">9</a></li>
                <li id="nextPage"><a href="javascript:nextPage();">Next &raquo</a></li>
            </ol>
        </nav>
        -->
    </div>
    <!--col-wide-->

</div>
<!--inner-->


<script type="text/javascript" src="../includes/scripts/jquery.dataTables.min.js"></script>
<script type="text/javascript">

    jQuery(document).ready(function () {
        // setup the table
        jQuery('#layerstable').dataTable({
            "aaSorting": [
                [ 2, "asc" ]
            ],
            "aLengthMenu": [
                [10, 25, 50, 100, -1],
                [10, 25, 50, 100, "All"]
            ],
            "sPaginationType": "full_numbers",
            "sDom": '<"sort-options"fl<"clear">>rt<"pagination"ip<"clear">>',
            "oLanguage": {
                "sSearch": ""
            }
        });

        jQuery("div.dataTables_filter input").attr("placeholder", "Filter within results");


    });

    function downloadLayers(type) {
        var downloadurl = "/layers-service/layers";
        var query = jQuery("div.dataTables_filter input").val();
        if (type == "json") {
            downloadurl += ".json";
        } else {
            downloadurl += ".csv";
        }
        if (query != "") {
            downloadurl += "?q=" + query;
        }
        location.href = downloadurl;
    }
</script>


<%@include file="../common/bottom.jsp" %>