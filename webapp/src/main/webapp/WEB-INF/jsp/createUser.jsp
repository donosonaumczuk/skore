<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
<c:url value="/create" var="signUpUrl" />
<form:form modelAttribute="registerForm" action="${loginUrl}" method="post">
    <div>
        <form:label path="username"><spring:message code="usernameLabel"/>: </form:label>
        <form:input id="input2" path="username" type="text"/>
        <form:errors path="username" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="password"><spring:message code="passwordLabel"/>: </form:label>
        <form:input id="input3" path="password" type="password"/>
        <form:errors path="password" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="repeatPassword"><spring:message code="repeatPasswordLabel"/>: </form:label>
        <form:input id="input4" path="repeatPassword" type="password"/>
        <form:errors path="repeatPassword" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="firstName"><spring:message code="firstNameLabel"/>: </form:label>
        <form:input id="input5" path="firstName" type="text"/>
        <form:errors path="firstName" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="lastName"><spring:message code="lastNameLabel"/>: </form:label>
        <form:input id="input6" path="lastName" type="text"/>
        <form:errors path="lastName" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="email"><spring:message code="emailLabel"/>: </form:label>
        <form:input id="Input1" path="email" type="text"/>
        <form:errors path="email" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="cellphone"><spring:message code="cellphoneLabel"/>: </form:label>
        <form:input id="input7" path="cellphone" type="text"/>
        <form:errors path="cellphone" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="birthday"><spring:message code="birthdayLabel"/>: </form:label>
            <div class="input-group dater" id="timepicker-from" data-target-input="nearest">
                <form:input type="text" path="birthday" class="form-control datetimepicker-input" data-target="#timepicker-from"/>
                <div class="input-group-append" data-target="#timepicker-from" data-toggle="datetimepicker">
                    <div class="input-group-text"><i class="fas fa-clock"></i></div>
                </div>
            </div>
        <form:errors path="birthday" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="country"><spring:message code="countryLabel"/>: </form:label>
        <form:input id="input8" path="country" type="text"/>
        <form:errors path="country" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="state"><spring:message code="stateLabel"/>: </form:label>
        <form:input id="input9" path="state" type="text"/>
        <form:errors path="state" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="city"><spring:message code="cityLabel"/>: </form:label>
        <form:input id="input10" path="city" type="text"/>
        <form:errors path="city" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <form:label path="street"><spring:message code="streetLabel"/>: </form:label>
        <form:input id="input11" path="street" type="text"/>
        <form:errors path="street" element="div" cssClass="invalid-feedback d-block"/>
    </div>
    <div>
        <input type="submit" value="<spring:message code="registerLabel"/>"/>
    </div>
</form:form>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="js/create-match.js"/>" type="text/javascript"></script>
</body>
</html>
