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
            <c:url value="/create" var="signUpUrl" />
            <form:form modelAttribute="registerForm" action="${signUpUrl}" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <form:label path="username"><spring:message code="usernameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="username" type="text"/>
                    <form:errors path="username" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="password"><spring:message code="passwordLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="password" type="password"/>
                    <form:errors path="password" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="repeatPassword"><spring:message code="repeatPasswordLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="repeatPassword" type="password"/>
                    <form:errors path="repeatPassword" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="firstName"><spring:message code="firstNameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="firstName" type="text"/>
                    <form:errors path="firstName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="lastName"><spring:message code="lastNameLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="lastName" type="text"/>
                    <form:errors path="lastName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="email"><spring:message code="emailLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="email" type="text"/>
                    <form:errors path="email" element="div" cssClass="invalid-feedback d-block"/>
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
                    <form:input class="form-control" path="cellphone" type="text"/>
                    <form:errors path="cellphone" element="div" cssClass="invalid-feedback d-block"/>
                </div>

                <div class="form-group">
                    <label for="datepicker"><spring:message code="birthdayLabel"/><span class="text-muted">*</span></label>
                    <small id="dateFormatHelp" class="form-text text-muted"><spring:message code="dateFormatLabel"/></small>
                    <div class="input-group date" id="datepicker" data-target-input="nearest">
                        <form:input type="text" path="birthday" class="form-control datetimepicker-input" data-target="#datepicker"/>
                        <div class="input-group-append" data-target="#datepicker" data-toggle="datetimepicker">
                            <div class="input-group-text"><i class="fas fa-calendar-alt"></i></div>
                        </div>
                        <form:errors path="birthday" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>

                <div class="form-group" id="locationField">
                    <label for="autocomplete"><spring:message code="addressLabel"/></label>
                    <input type="text" class="form-control" id="autocomplete" placeholder="<spring:message code="enterAddressLabel"/>" onFocus="geolocate()" >
                </div>
                <div class="form-group">
                    <label for="country"><spring:message code="countryLabel"/></label>
                    <form:input type="text" path="country" class="form-control" id="country" readonly="true"/>
                    <form:errors path="country" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <label for="route"><spring:message code="streetLabel"/></label>
                    <form:input type="text" path="street" class="form-control" id="route" readonly="true"/>
                    <form:errors path="street" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="locality"><spring:message code="cityLabel"/></label>
                        <form:input type="text" path="city" class="form-control" id="locality" readonly="true"/>
                        <form:errors path="city" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                    <div class="form-group col-6">
                        <label for="administrative_area_level_1"><spring:message code="stateLabel"/></label>
                        <form:input type="text" path="state" class="form-control" id="administrative_area_level_1" readonly="true"/>
                        <form:errors path="state" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>
                <small id="requiredHelp" class="form-text text-muted mb-2"><spring:message code="requiredHelpLabel"/></small>
                <div class="text-center">
                    <button type="submit" class="btn btn-green mb-2"><spring:message code="signUpLabel"/></button>
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
