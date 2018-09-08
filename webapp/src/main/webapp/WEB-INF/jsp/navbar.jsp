<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar fixed-top primary-nav">
    <a class="navbar-brand nav-brand-font" href="<c:url value="/test"/>">skore</a>
    <form class="form-inline">
        <input class="form-control" type="search" placeholder="Buscar..." aria-label="Search">
    </form>
    <a class="d-none d-sm-block login-link" href="">¿Ya tenes cuenta? Inicia sesion</a>
</nav>


<nav class="navbar d-none d-sm-block fixed-top second-nav">
    <button class="btn offset-4 btn-second-nav" type="submit">RANKINGS</button>
    <button class="btn offset-3 btn-second-nav" type="submit">TORNEOS</button>
</nav>