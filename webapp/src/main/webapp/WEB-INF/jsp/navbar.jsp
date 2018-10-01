<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar fixed-top primary-nav">
    <button class="navbar-toggler d-md-none" type="button"><span class="fas fa-bars"></span></button>
    <a class="d-none d-sm-block navbar-brand nav-brand-font" href="<c:url value="/"/>">sk<i class="fas fa-bullseye"></i>re</a>
    <a class="d-sm-none navbar-brand nav-brand-font" href="<c:url value="/"/>"><i class="fas fa-bullseye"></i>sk</a>
    <a class="d-sm-none login-link" href="/login"><i class="fas fa-user"></i></a>
    <c:choose>
        <c:when test="${loggedUser != null}">
            <form class="d-none d-sm-block form-inline">
                <a class="mr-1 login-link" href="/login"><c:out value="${loggedUser.getUserName()}"/></a>
            </form>
        </c:when>
        <c:otherwise>
            <form class="d-none d-sm-block form-inline">
                <a class="mr-1 login-link" href="/login"><spring:message code="signInMessage"/></a>
                <span class="white-text mr-1"><spring:message code="orLabel"/></span>
                <a class="login-link" href="/create"><spring:message code="signUpMessage"/></a>
            </form>
        </c:otherwise>
    </c:choose>

</nav>

<%--<nav class="navbar d-none d-sm-block fixed-top second-nav">--%>
    <%--<button class="btn offset-4 btn-second-nav" type="submit"><spring:message code="rankingsMessage"/></button>--%>
    <%--<button class="btn offset-3 btn-second-nav" type="submit"><spring:message code="tournamentsMessage"/></button>--%>
<%--</nav>--%>