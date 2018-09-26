<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="img/bullseye-solid.ico"/>
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<%--<jsp:include page="navbar.jsp"></jsp:include>--%>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container --><div class="container">
            <div class="row">
                <div class="col">
                    <a class="offset-3 sign-in-brand" href="<c:url value="/"/>">sk<i class="fas fa-bullseye"></i>re</a>
                </div>
            </div>
            <c:url value="/login" var="loginUrl" />
            <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded" >
                <div class="form-group">
                    <label for="username"><spring:message code="username"/></label>
                    <input type="text" class="form-control" id="username" name="user_username" placeholder="<spring:message code="username"/>">
                </div>
                <div class="form-group">
                    <label for="password"><spring:message code="password"/></label>
                    <input type="password" class="form-control" id="password" name="user_password" placeholder="<spring:message code="password"/>">
                </div>
                <div class="form-check">
                    <input type="checkbox" name="user_rememberme" class="form-check-input" id="rememberme">
                    <label class="form-check-label" for="rememberme"><spring:message code="rememberMeMessage"/></label>
                </div>
                <button type="submit" class="offset-4 btn btn-green mb-2"><spring:message code="signInLabel"/></button>
            </form>

        </div> <!-- END Form container -->
    </div>
</div>


<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="<c:url value="js/maps-autocomplete.js"/>" type="text/javascript"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqSX1WHUw4OlgMDzYM40uSVPGkV06DR1I&libraries=places&callback=initAutocomplete" async defer></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="js/create-match.js"/>" type="text/javascript"></script>

</body>
</html>