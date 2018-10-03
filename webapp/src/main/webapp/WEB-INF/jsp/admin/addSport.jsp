<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="./../css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="<c:url value="/img/bullseye-solid.ico"/>"/>
    <title>skore</title>
</head>
<body>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container -->
            <div class="row text-center">
                <div class="col">
                    <a class="sign-in-brand" href="<c:url value="/"/>">sk<i class="fas fa-bullseye"></i>re</a>
                </div>
            </div>
            <c:url value="/admin/createSport" var="createSportUrl" />
            <form:form modelAttribute="createSportForm" action="${createSportUrl}" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <form:label path="sportName"><spring:message code="sportNameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="sportName" type="text"/>
                    <form:errors path="sportName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="displayName"><spring:message code="displayNameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="displayName" type="text"/>
                    <form:errors path="displayName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="playerQuantity"><spring:message code="playerQuantityLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="playerQuantity" type="text"/>
                    <form:errors path="playerQuantity" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="input-group">
                    <div class="custom-file">
                        <input type="file"  name="image" class="custom-file-input" id="avatarInput">
                        <label class="custom-file-label" for="avatarInput" aria-describedby="inputGroupFileAddon02"><spring:message code="chooseFileLabel"/></label>
                    </div>
                    <form:errors path="image" element="div" cssClass="invalid-feedback d-block"/>
                </div>

                <small id="requiredHelp" class="form-text text-muted mb-2"><spring:message code="requiredHelpLabel"/></small>
                <div class="text-center">
                    <button type="submit" class="btn btn-green mb-2"><spring:message code="createSportLabel"/></button>
                </div>
            </form:form>

        </div> <!-- END Form container -->
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="./../js.jsp"></jsp:include>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqSX1WHUw4OlgMDzYM40uSVPGkV06DR1I&libraries=places&callback=initAutocomplete" async defer></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="/js/sign-up.js"/>" type="text/javascript"></script>
</body>
</html>
