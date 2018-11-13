<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar fixed-top primary-nav">
    <button class="navbar-toggler d-md-none" onclick="navbarTogglerClick()" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="fas fa-bars"></span>
    </button>
    <a class="d-none d-sm-block navbar-brand nav-brand-font" href="<c:url value="/"/>">sk<i class="fas fa-bullseye"></i>re</a>
    <a class="d-sm-none navbar-brand nav-brand-font" href="<c:url value="/"/>"><i class="fas fa-bullseye"></i>sk</a>
    <c:if test="${isAdmin}">
        <form class="d-none d-sm-block form-inline">
            <a class="login-link" href="<c:url value="/admin/"/>"><spring:message code="adminLabel"/></a>
        </form>
    </c:if>
    <c:choose>
        <c:when test="${isLogged}">
            <a class="d-sm-none login-link" href="<c:url value="/logout"/>"><i class="fas fa-sign-out-alt"></i></a>
        </c:when>
        <c:otherwise>
            <a class="d-sm-none login-link" href="<c:url value="/login"/>"><i class="fas fa-user"></i></a>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${isLogged}">
            <form class="d-none d-sm-block form-inline">
                <c:set value="${loggedUser.getUserName()}" var="username"/>
                <a class="mr-1 login-link" href="<c:url value="/profile/${username}"/>"><c:out value="${loggedUser.getUserName()}"/></a>
                <span class="white-text mr-1"> | </span>
                <a class="login-link" href="<c:url value="/logout"/>"><spring:message code="logoutLabel"/></a>
            </form>
        </c:when>
        <c:otherwise>
            <form class="d-none d-sm-block form-inline">
                <a class="mr-1 login-link" href="<c:url value="/login"/>"><spring:message code="signInMessage"/></a>
                <span class="white-text mr-1"><spring:message code="orLabel"/></span>
                <a class="login-link" href="<c:url value="/create"/>"><spring:message code="signUpMessage"/></a>
            </form>
        </c:otherwise>
    </c:choose>
</nav>

<%--<nav class="navbar d-none d-sm-block fixed-top second-nav">--%>
    <%--<button class="btn offset-4 btn-second-nav" type="submit"><spring:message code="rankingsMessage"/></button>--%>
    <%--<button class="btn offset-3 btn-second-nav" type="submit"><spring:message code="tournamentsMessage"/></button>--%>
<%--</nav>--%>