<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="/img/bullseye-solid.ico"/>
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
            <c:url value="/login" var="loginUrl" />
            <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded" >
                <c:if test="${not empty error}">
                    <div  class="invalid-feedback d-block"><spring:message code="invalidUserOrPasswordMessage"/></div>
                </c:if>
                <div class="form-group">
                    <label for="username"><spring:message code="username"/></label>
                    <spring:message code="username" var="usernameMessage" />
                    <input type="text" class="form-control" id="username" name="user_username" placeholder='${usernameMessage}'>
                </div>
                <div class="form-group">
                    <label for="password"><spring:message code="password"/></label>
                    <spring:message code="password" var="passwordMessage" />

                    <input type="password" class="form-control" id="password" name="user_password" placeholder='${passwordMessage}'/>

                </div>
                <div class="form-check">
                    <input type="checkbox" name="user_rememberme" class="form-check-input" id="rememberme">
                    <label class="form-check-label" for="rememberme"><spring:message code="rememberMeMessage"/></label>
                </div>
                <div class="text-center">
                    <button type="submit" class="btn btn-green mb-2"><spring:message code="signInLabel"/></button>
                </div>
            </form>
            <div class="row mt-4 text-center">
                <div class="col">
                    <span class="mr-1"><spring:message code="areYouNewLabel"/></span>
                    <a class="link" href="/create"><spring:message code="signUpMessage"/></a>
                </div>
            </div>
        </div> <!-- END Form container -->
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>

</body>
</html>