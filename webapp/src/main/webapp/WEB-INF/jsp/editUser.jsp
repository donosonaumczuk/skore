<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
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
            <c:url value="/edit/${username}" var="editUrl" />
            <form:form modelAttribute="editUserForm" action="${editUrl}" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <form:label path="firstName"><spring:message code="firstNameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="firstName" type="text" value="${user.firstName}"/>
                    <form:errors path="firstName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="lastName"><spring:message code="lastNameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="lastName" type="text" value="${user.lastName}"/>
                    <form:errors path="lastName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="image"><spring:message code="imageLabel"/></form:label>
                    <small id="imgFormatHelp" class="form-text text-muted"><spring:message code="imageFormat"/></small>
                    <div class="input-group custom-file">
                        <input type="file"  name="image" class="custom-file-input" id="image" multiple onchange="showname()">
                        <label class="custom-file-label" for="image" id="fileLabel"aria-describedby="inputGroupFileAddon02" ></label>
                    </div>
                    <form:errors path="image" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="cellphone"><spring:message code="cellphoneLabel"/></form:label>
                    <form:input class="form-control" path="cellphone" type="text" value="${user.cellphone}"/>
                    <form:errors path="cellphone" element="div" cssClass="invalid-feedback d-block"/>
                </div>

                <small id="requiredHelp" class="form-text text-muted mb-2"><spring:message code="requiredHelpLabel"/></small>
                <div class="text-center">
                    <button type="submit" class="btn btn-green mb-2"><spring:message code="modifyLabel"/></button>
                </div>
            </form:form>
            <div class="row mt-4 text-center">
                <div class="col">
                    <span class="mr-1"><spring:message code="haveAccountLabel"/></span>
                    <a class="link" href="<c:url value="/login"/>"><spring:message code="signInMessage"/></a>
                </div>
            </div>
        </div> <!-- END Form container -->
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqSX1WHUw4OlgMDzYM40uSVPGkV06DR1I&libraries=places&callback=initAutocomplete" async defer></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="/js/sign-up.js"/>" type="text/javascript"></script>
</body>
</html>
