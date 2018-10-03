<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="/img/bullseye-solid.ico"/>
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid create-match-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container -->
            <c:url value="/createMatch" var="createMatchUrl" />
            <form:form modelAttribute="createMatchForm" action="${createMatchUrl}" method="post">
                <div class="form-group">
                    <label for="match-name-input"><spring:message code="matchNameLabel"/><span class="text-muted">*</span></label>
                    <spring:message code="matchNameLabel" var="matchLabelHolder" />
                    <form:input type="text" class="form-control" id="match-name-input" path="matchName" placeholder='${matchLabelHolder}'/>
                    <form:errors path="matchName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <label for="competitiveness"><spring:message code="matchCompetitivenessLabel"/><span class="text-muted">*</span></label>
                    <div class="input-group">
                        <div class="btn-group btn-group-toggle" data-toggle="buttons" id="competitiveness">
                            <label class="btn btn-green" id="friendly">
                                <input:radiobutton path="competitivity" name="options"  value="Friendly" autocomplete="false"/><i class="d-none fas fa-check"></i> <spring:message code="friendlyLabel"/>
                            </label>
                            <label class="btn btn-green" id="competitive">
                                <input:radiobutton path="competitivity" name="options" value="Competitive" autocomplete="false"/><i class="d-none fas fa-check"></i> <spring:message code="competitiveLabel"/>
                            </label>
                        </div>
                        <form:errors path="competitivity" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inscription"><spring:message code="inscriptionModeLabel"/><span class="text-muted">*</span></label>
                    <div class="input-group">
                        <div class="btn-group btn-group-toggle" data-toggle="buttons" id="inscription">
                            <label class="btn btn-green" id="individual">
                                <form:radiobutton path="mode" name="options" value="Individual" autocomplete="false"/><i class="d-none fas fa-check"></i> <spring:message code="individualLabel"/>
                            </label>
                            <label class="btn btn-green" id="team">
                                <form:radiobutton path="mode" name="options" value="Team" autocomplete="false"/><i class="d-none fas fa-check"></i> <spring:message code="teamLabel"/>
                            </label>
                        </div>
                        <form:errors path="mode" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputSport"><spring:message code="sportLabel"/><span class="text-muted">*</span></label>
                    <form:select id="inputSport" path="sportName" class="form-control">
                        <option value=""><spring:message code="chooseSportLabel"/></option>
                        <c:forEach var="sport" items="${sports}">
                            <option value="<c:out value="${sport.getName()}"/>"><c:out value="${sport.getDisplayName()}"/></option>
                        </c:forEach>
                    </form:select>
                    <form:errors path="sportName" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group collapse" id="collapseTeam">
                    <label for="inputTeam"><spring:message code="teamLabel"/><span class="text-muted">*</span></label>
                    <form:select  path="teamId" id="inputTeam" class="form-control">
                        <option value="" selected><spring:message code="chooseTeamLabel"/></option>
                        <option value="Team1">Team1</option>
                        <option value="Team2">Team2</option>
                    </form:select>
                    <form:errors path="teamId" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group">
                    <label for="datepicker"><spring:message code="dateLabel"/><span class="text-muted">*</span></label>
                    <small id="dateFormatHelp" class="form-text text-muted"><spring:message code="dateFormatLabel"/></small>
                    <div class="input-group date" id="datepicker" data-target-input="nearest">
                        <form:input type="text" path="date" class="form-control datetimepicker-input" data-target="#datepicker"/>
                        <div class="input-group-append" data-target="#datepicker" data-toggle="datetimepicker">
                            <div class="input-group-text"><i class="fas fa-calendar-alt"></i></div>
                        </div>
                        <form:errors path="date" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="timepicker-from"><spring:message code="fromLabel"/><span class="text-muted">*</span></label>
                        <div class="input-group date" id="timepicker-from" data-target-input="nearest">
                            <form:input type="text" path="startTime" class="form-control datetimepicker-input" data-target="#timepicker-from"/>
                            <div class="input-group-append" data-target="#timepicker-from" data-toggle="datetimepicker">
                                <div class="input-group-text"><i class="fas fa-clock"></i></div>
                            </div>
                            <form:errors path="startTime" element="div" cssClass="invalid-feedback d-block"/>
                        </div>
                    </div>
                    <div class="form-group col-6">
                        <label for="timepicker-to"><spring:message code="durationLevel"/><span class="text-muted">*</span></label>
                        <div class="input-group date" id="timepicker-to" data-target-input="nearest">
                            <form:input type="text" path="duration" class="form-control datetimepicker-input" data-target="#timepicker-to"/>
                            <div class="input-group-append" data-target="#timepicker-to" data-toggle="datetimepicker">
                                <div class="input-group-text"><i class="fas fa-stopwatch"></i></div>
                            </div>
                            <form:errors path="duration" element="div" cssClass="invalid-feedback d-block"/>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="description"><spring:message code="descriptionLabel"/></label>
                    <form:textarea class="form-control" id="description" path="description" rows="3" maxlength="140"/>
                    <form:errors path="description" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-group" id="locationField">
                    <label for="autocomplete"><spring:message code="addressLabel"/><span class="text-muted">*</span></label>
                    <input type="text" class="form-control" id="autocomplete" placeholder="<spring:message code="enterAddressLabel"/>" onFocus="geolocate()" >
                </div>
                <div class="form-group">
                    <label for="country"><spring:message code="countryLabel"/><span class="text-muted">*</span></label>
                    <form:input type="text" path="country" class="form-control" id="country" readonly="true"/>
                    <form:errors path="country" element="div" cssClass="invalid-feedback d-block"/>
                </div>
                <div class="form-row">
                    <div class="form-group col-9">
                        <label for="route"><spring:message code="streetLabel"/></label>
                        <form:input type="text" path="street" class="form-control" id="route" readonly="true"/>
                        <form:errors path="street" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                    <div class="form-group col-3">
                        <label for="street_number"><spring:message code="numberLabel"/></label>
                        <form:input type="text" path="streetNumber" class="form-control" id="street_number" readonly="true"/>
                        <form:errors path="streetNumber" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="locality"><spring:message code="cityLabel"/></label>
                        <form:input type="text" path="city" class="form-control" id="locality" readonly="true"/>
                        <form:errors path="city" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                    <div class="form-group col-6">
                        <label for="administrative_area_level_1"><spring:message code="stateLabel"/><span class="text-muted">*</span></label>
                        <form:input type="text" path="state" class="form-control" id="administrative_area_level_1" readonly="true"/>
                        <form:errors path="state" element="div" cssClass="invalid-feedback d-block"/>
                    </div>
                </div>
                <small id="requiredHelp" class="form-text text-muted mb-2"><spring:message code="requiredHelpLabel"/></small>
                <button type="submit" class="offset-4 btn btn-green mb-2"><spring:message code="createMatchFormSubmit"/></button>
            </form:form>

        </div> <!-- END Form container -->

    </div>
</div>


<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="<c:url value="/js/maps-autocomplete.js"/>" type="text/javascript"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqSX1WHUw4OlgMDzYM40uSVPGkV06DR1I&libraries=places&callback=initAutocomplete" async defer></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="/js/create-match.js"/>" type="text/javascript"></script>
</body>
</html>