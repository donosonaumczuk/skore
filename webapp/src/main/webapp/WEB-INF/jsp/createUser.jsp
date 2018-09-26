<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
</head>
<body>
<c:url value="/create" var="signUpUrl" />
<form:form modelAttribute="registerForm" action="${loginUrl}" method="post">
    <div>
        <form:label path="username">Username: </form:label>
        <form:input path="username" type="text"/>
        <form:errors path="username" element="p" cssClass=""/>
    </div>
    <div>
        <form:label path="password">Password: </form:label>
        <form:input path="password" type="password"/>
        <form:errors path="password" element="p"/>
    </div>
    <div>
        <form:label path="repeatPassword">Repeat Password: </form:label>
        <form:input path="repeatPassword" type="password"/>
        <form:errors path="repeatPassword" element="p"/>
    </div>
    <div>
        <input type="submit" value="Resgister!"/>
    </div>
</form:form>
</body>
</html>
