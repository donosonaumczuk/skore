<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="./../css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="<c:url value="/img/bullseye-solid.ico"/>"/>
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="./../navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container -->

            <div class="row text-center">
                <c:if test="${sports.size() < 1}">
                    <div class="col">
                        <p><spring:message code="AdminMainPageLabel"/><a class="btn btn-green mb-2" href="<c:url value="/admin/createSport"/>" role="button"><spring:message code="createSportLabel"/></a></p>
                    </div>
                </c:if>
            </div>
            <div class="table-responsive">
                <c:if test="${sports.size() > 0}">
                    <table class="table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Image</th>
                                <th>Sport Name</th>
                                <th>Display Name</th>
                                <th>Player Quantity On Each Team</th>
                                <th>
                                    <div class="col">
                                        <a class="btn btn-green mb-2" href="<c:url value="/admin/createSport"/>" role="button"><spring:message code="createSportLabel"/></a>
                                    </div>
                                </th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var = "i" begin = "0" end = "${sports.size() - 1}">
                                <tr>
                                    <td><img src="<c:url value="/sport/image/${sports.get(i).name}"/>" class="sport-img" alt="sport-pic"></td>
                                    <td><c:out value = "${sports.get(i).name}"/></td>
                                    <td><c:out value = "${sports.get(i).displayName}"/></td>
                                    <td><c:out value = "${sports.get(i).quantity}"/></td>
                                    <td>
                                        <div class="col">
                                            <a class="btn btn-outline-secondary" href="<c:url value="/admin/createSport"/>" role="button"><i class="fas fa-edit"></i><spring:message code="EditSportLabel"/></a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

            </div>
        </div>
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="./../js.jsp"></jsp:include>

</body>
</html>