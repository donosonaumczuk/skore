<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar fixed-top primary-nav">
    <a class="navbar-brand nav-brand-font" href="<c:url value="/"/>">sk<i class="fas fa-bullseye"></i>re</a>
    <a class="d-none d-sm-block login-link" href=""><spring:message code="logInMessage"/></a>
</nav>


<%--<nav class="navbar d-none d-sm-block fixed-top second-nav">--%>
    <%--<button class="btn offset-4 btn-second-nav" type="submit"><spring:message code="rankingsMessage"/></button>--%>
    <%--<button class="btn offset-3 btn-second-nav" type="submit"><spring:message code="tournamentsMessage"/></button>--%>
<%--</nav>--%>