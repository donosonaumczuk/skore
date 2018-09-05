<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <link href="<c:url value="css/main.css" />" rel="stylesheet">
</head>
<body>
    <h2>Hello <c:out value="${user.name}" escapeXml="true"/>!</h2>
    <h2>Id = <c:out value="${user.id}" escapeXml="true"/></h2>
    <h2>Password = <c:out value="${user.password}" escapeXml="true"/> </h2>
</body>
</html>
