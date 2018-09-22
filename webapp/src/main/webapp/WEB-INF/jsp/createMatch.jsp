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
                    <label for="match-name-input"><spring:message code="matchNameLabel"/></label>
                    <input type="text" class="form-control" id="match-name-input" placeholder="<spring:message code="matchNameLabel"/>">
                </div>
                <div class="form-group">
                    <label for="inputSport">Sport</label>
                    <select id="inputSport" class="form-control">
                        <option selected>Choose sport...</option>
                        <option>Futbol</option>
                        <option>Padel</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="email-input"><spring:message code="emailLabel"/></label>
                    <input type="text" class="form-control" id="email-input" placeholder="<spring:message code="emailLabel"/>">
                </div>
                <div class="form-row">
                    <div class="col-2 form-group">
                        <label for="inputDay">Day</label>
                        <select id="inputDay" class="form-control">
                            <option selected>Choose day...</option>
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>etc...</option>
                        </select>
                    </div>
                    <div class="offset-1 col-5 form-group">
                        <label for="inputMonth">Month</label>
                        <select id="inputMonth" class="form-control">
                            <option selected>Choose month...</option>
                            <option>January</option>
                            <option>February</option>
                            <option>March</option>
                            <option>etc...</option>
                        </select>
                    </div>
                    <div class="offset-1 col-3 form-group">
                        <label for="inputYear">Year</label>
                        <select id="inputYear" class="form-control">
                            <option selected>Choose year...</option>
                            <option>2018</option>
                            <option>2019</option>
                            <option>2020</option>
                            <option>etc...</option>
                        </select>
                    </div>
                </div>
                <div class="form-group" id="locationField">
                    <label for="email-input">Address</label>
                    <input type="text" class="form-control" id="autocomplete" placeholder="Enter your address" onFocus="geolocate()" >
                </div>
                <div class="form-group">
                    <label for="country">Country</label>
                    <input type="text" class="form-control" id="country" readonly>
                </div>
                <div class="form-row">
                    <div class="form-group col-9">
                        <label for="route">Street</label>
                        <input type="text" class="form-control" id="route" readonly>
                    </div>
                    <div class="form-group col-3">
                        <label for="street_number">Number</label>
                        <input type="text" class="form-control" id="street_number" readonly>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="locality">City</label>
                        <input type="text" class="form-control" id="locality" readonly>
                    </div>
                    <div class="form-group col-6">
                        <label for="administrative_area_level_1">State</label>
                        <input type="text" class="form-control" id="administrative_area_level_1" readonly>
                    </div>
                </div>
                <button type="submit" class="offset-4 btn btn-green"><spring:message code="createMatchFormSubmit"/></button>
            </form>

        </div> <!-- END Form container -->

    </div>
</div>


<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="<c:url value="js/maps-autocomplete.js"/>" type="text/javascript"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqSX1WHUw4OlgMDzYM40uSVPGkV06DR1I&libraries=places&callback=initAutocomplete" async defer></script>

</body>
</html>