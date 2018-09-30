<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="icon" href="img/bullseye-solid.ico" />
    <title>skore</title>
</head>
<body>

    <%-- Include Navigation Bars --%>
    <jsp:include page="navbar.jsp"></jsp:include>

    <div class="container-fluid">
        <div class="row">

            <div class="d-none d-md-block sidepanel col-md-4 col-lg-4 offset-xl-1 col-xl-3">
                <div class="container-fluid"> <!-- Leftside panel container -->
                    <div class="row">
                        <div class="col text-center create-match p-4">
                            <p><spring:message code="cantFindMatchMessage"/></p>
                            <button id="create-match-btn" class="btn btn-white-succ" type="submit"><spring:message code="createAMatchMessage"/></button>
                        </div>
                    </div>
                    <div class="row filters p-4 mt-2">
                        <div class="container-fluid">
                            <div class="row">
                                <p class="left-panel-title"><spring:message code="filtersAndCategoriesMessage"/></p>
                            </div>
                            <div class="row mb-4">
                                <label for="country-filter"><spring:message code="countryLabel"/></label>
                                <input class="form-control filter-input mb-2" type="text" id="country-filter"/>
                            </div>
                            <div class="row mb-4">
                                <label for="state-filter"><spring:message code="stateLabel"/></label>
                                <input class="form-control filter-input mb-2" type="text" id="state-filter"/>
                            </div>
                            <div class="row mb-4">
                                <label for="state-filter"><spring:message code="cityLabel"/></label>
                                <input class="form-control filter-input mb-2" type="text" id="city-filter"/>
                            </div>
                        </div>
                    </div>
                </div> <!-- END Leftside panel container -->
            </div>

            <div class="col-md-8 col-lg-8 col-xl-6">
                <div class="match-container container-fluid"> <!-- Match cards container -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row mb-4">
                                <div class="col-2 col-sm-1 pl-0">
                                    <img src="img/user-default.svg" class="user-avatar" alt="user-pic">
                                </div>
                                <div class="col-3 col-sm-4">
                                    <div class="row">
                                        <p class="name-label">Alan Donoso Naumczuk</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="/profile/donosonaumczuk">@donosonaumczuk</a>
                                    </div>
                                </div>
                                <div class="col-2 col-sm-3">
                                    <div class="container-fluid pt-2">
                                        <div class="row">
                                            <div class="col col-xl-4 mr-0 mt-1">
                                                <img src="img/football.svg" class="sport-img" alt="sport-pic">
                                            </div>
                                            <div class="col-6 col-xl d-none d-sm-block pl-0">
                                                <p class="sport-label">Futbol</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="offset-1 col-4 col-sm-3">
                                    <div class="row text-center">
                                        <div class="col">
                                            <i class="name-label fas fa-users mr-2"></i>7 / 10
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green"><i class="fas fa-plus"></i> UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span>Domingo 11 de Agosto de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="name-label mr-2 fas fa-clock"></span>19:00 - 20:00</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="location-icon mr-2 fas fa-map-marker-alt"></span>Stadium Futbol 5, Ituzaingo Sur, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                    <div class="row p-2 mt-2">
                        <div class="offset-5 col-2">
                            <img class="img-fluid" src="img/loader.gif">
                        </div>
                    </div>

                </div> <!-- END Match cards container -->
            </div>
        </div>
    </div>

    <%-- Include JS Scripts --%>
    <jsp:include page="js.jsp"></jsp:include>
    <script src="<c:url value="js/index.js"/>"></script>
    <script src="<c:url value="js/filters.js"/>" type="text/javascript"></script>
    <script src="<c:url value="js/load-matches.js"/>" type="text/javascript"></script>

</body>
</html>