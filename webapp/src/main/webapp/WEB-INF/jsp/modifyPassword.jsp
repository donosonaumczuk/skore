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

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container -->
            <div class="row text-center">
                <div class="col">
                    <a class="sign-in-brand" href="<c:url value="/"/>">sk<i class="fas fa-bullseye"></i>re</a>
                </div>
            </div>
            <c:url value="/changePassword" var="modifyPasswordUrl" />
            <form:form modelAttribute="modifyPasswordForm" action="${modifyPasswordUrl}" method="post" enctype="multipart/form-data">
                    <form:hidden  path="username" value="${loggedUser.userName}"/>
                <div class="form-group">
                    <form:label path="oldPassword"><spring:message code="oldPasswordLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="oldPassword" type="password"/>
                    <form:errors path="oldPassword" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="newPassword"><spring:message code="newPasswordLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="newPassword" type="password"/>
                    <form:errors path="newPassword" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <form:label path="repeatNewPassword"><spring:message code="repeatNewPasswordLabel"/><span class="text-muted">*</span></form:label>
                    <form:input class="form-control" path="repeatNewPassword" type="password"/>
                    <form:errors path="repeatNewPassword" element="div" cssClass="invalid-feedback d-block"/>
                </div>



               <div class="text-center">
                    <button type="submit" class="btn btn-green mb-2"><spring:message code="modifyLabel"/></button>
               </div>
            </form:form>
        </div> <!-- END Form container -->
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
</body>
</html>
