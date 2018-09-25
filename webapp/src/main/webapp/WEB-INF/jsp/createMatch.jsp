<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="img/bullseye-solid.ico"/>
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid create-match-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container --><div class="container">
            <form>
                <div class="form-group">
                    <label for="match-name-input"><spring:message code="matchNameLabel"/></label>
                    <input type="text" class="form-control" id="match-name-input" placeholder="<spring:message code="matchNameLabel"/>">
                </div>
                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="competitiveness">Match competitiveness</label>
                        <div class="input-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons" id="competitiveness">
                                <label class="btn btn-green active" id="friendly">
                                    <input type="radio" name="options" autocomplete="off"><i class="fas fa-check"></i> Friendly
                                </label>
                                <label class="btn btn-green" id="competitive">
                                    <input type="radio" name="options" autocomplete="off"><i class="d-none fas fa-check"></i> Competitive
                                </label>
                            </div>
                        </div>
                    </div>
                    <div class="form-group col-6">
                        <label for="inscription">Inscription mode</label>
                        <div class="input-group">
                            <div class="btn-group btn-group-toggle" data-toggle="buttons" id="inscription">
                                <label class="btn btn-green active" data-target="#collapseTeam" aria-expanded="false" aria-controls="collapseTeam" id="individual">
                                    <input type="radio" name="options" autocomplete="off"><i class="fas fa-check"></i> Individual
                                </label>
                                <label class="btn btn-green"  data-toggle="collapse" data-target="#collapseTeam" aria-expanded="false" aria-controls="collapseTeam" id="team">
                                    <input type="radio" name="options" autocomplete="off"><i class="d-none fas fa-check"></i> Team
                                </label>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="inputSport">Sport</label>
                    <select id="inputSport" class="form-control">
                        <option selected>Choose sport...</option>
                        <option>Football 5</option>
                        <option>Football 7</option>
                        <option>Football 11</option>
                        <option>Paddle Singles</option>
                        <option>Paddle Doubles</option>
                    </select>
                </div>
                <div class="form-group collapse" id="collapseTeam">
                    <label for="inputTeam">Team</label>
                    <select id="inputTeam" class="form-control">
                        <option selected>Choose team...</option>
                        <option>Team1</option>
                        <option>Team2</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="datepicker">Date</label>
                    <div class="input-group date" id="datepicker" data-target-input="nearest">
                        <input type="text" class="form-control datetimepicker-input" data-target="#datepicker"/>
                        <div class="input-group-append" data-target="#datepicker" data-toggle="datetimepicker">
                            <div class="input-group-text"><i class="fas fa-calendar-alt"></i></div>
                        </div>
                    </div>
                    <small id="dateFormatHelp" class="form-text text-muted">Date format MM/DD/YYYY</small>
                </div>
                <div class="form-row">
                    <div class="form-group col-6">
                        <label for="timepicker-from">From</label>
                        <div class="input-group date" id="timepicker-from" data-target-input="nearest">
                            <input type="text" class="form-control datetimepicker-input" data-target="#timepicker-from"/>
                            <div class="input-group-append" data-target="#timepicker-from" data-toggle="datetimepicker">
                                <div class="input-group-text"><i class="fas fa-clock"></i></div>
                            </div>
                        </div>
                    </div>
                    <div class="form-group col-6">
                        <label for="timepicker-to">To</label>
                        <div class="input-group date" id="timepicker-to" data-target-input="nearest">
                            <input type="text" class="form-control datetimepicker-input" data-target="#timepicker-to"/>
                            <div class="input-group-append" data-target="#timepicker-to" data-toggle="datetimepicker">
                                <div class="input-group-text"><i class="fas fa-clock"></i></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea class="form-control" id="description" rows="3" maxlength="140" placeholder="Write optional description..."></textarea>
                </div>
                <div class="form-group" id="locationField">
                    <label for="autocomplete">Address</label>
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
                        <label for="locality">City / Locality</label>
                        <input type="text" class="form-control" id="locality" readonly>
                    </div>
                    <div class="form-group col-6">
                        <label for="administrative_area_level_1">State / Province</label>
                        <input type="text" class="form-control" id="administrative_area_level_1" readonly>
                    </div>
                </div>
                <button type="submit" class="offset-4 btn btn-green mb-2"><spring:message code="createMatchFormSubmit"/></button>
            </form>

        </div> <!-- END Form container -->

    </div>
</div>


<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="<c:url value="js/maps-autocomplete.js"/>" type="text/javascript"></script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBqSX1WHUw4OlgMDzYM40uSVPGkV06DR1I&libraries=places&callback=initAutocomplete" async defer></script>
<script src="https://cdn.jsdelivr.net/npm/moment@2.22.2/moment.min.js" type="text/javascript"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js" type="text/javascript"></script>
<script  src="<c:url value="js/create-match.js"/>" type="text/javascript"></script>
</body>
</html>