<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<c:url value="/login" var="loginUrl" />
<form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded" >
    <div>
        <label for="username">Username</label>
        <input id="username" name="user_username" type="text"/>
    </div>
    <div>
        <label for="password">Password</label>
        <input id="password" name="user_password" type="password"/>
    </div>
    <div>
        <label><input name="user_rememberme" type="checkbox" /><spring:message code="rememberMeMessage"/></label>
    </div>
    <div>
        <input type="submit" value="Login!"/>
    </div>
</form>
</body>
</html>
