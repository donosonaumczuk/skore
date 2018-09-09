<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>

    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">

        <div class="container-fluid create-match-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container -->

            <form>
                <div class="form-group">
                    <label for="match-name-input"><spring:message code="createMatchFormMatchName"/></label>
                    <input type="text" class="form-control" id="match-name-input" placeholder="<spring:message code="createMatchFormMatchName"/>">
                </div>
                <div class="form-group">
                    <label for="inputState">Sport</label>
                    <select id="inputState" class="form-control">
                        <option selected>Choose sport...</option>
                        <option>Futbol</option>
                        <option>Padel</option>
                    </select>
                    <small id="emailHelp" class="form-text text-muted">If there isn't the sport you want click here to request to add it</small>
                </div>
                <button type="submit" class="offset-4 btn btn-green"><spring:message code="createMatchFormSubmit"/></button>
            </form>

        </div> <!-- END Form container -->

    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>

</body>
</html>