
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
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

</body>
</html>
