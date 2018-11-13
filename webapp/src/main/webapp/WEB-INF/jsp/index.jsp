<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="icon" href="<c:url value="/img/bullseye-solid.ico"/>" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/3.5.2/animate.min.css" integrity="sha384-OHBBOqpYHNsIqQy8hL1U+8OXf9hH6QRxi0+EODezv82DfnZoV7qoHAZDwMwEJvSw" crossorigin="anonymous">
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">

        <div class="sidepanel col-md-4 col-lg-4 offset-xl-1 col-xl-3 navbar-collapse" id="navbarSupportedContent">
            <div class="container-fluid"> <!-- Leftside panel container -->
                <div class="row">
                    <div class="col text-center create-match p-4">
                        <p><spring:message code="cantFindMatchMessage"/></p>
                        <a id="create-match-btn" class="btn btn-white-succ" href="<c:url value="/createMatch"/>" role="button">
                            <spring:message code="createAMatchMessage"/>
                        </a>
                    </div>
                </div>
                <div class="row filters p-4 mt-2">
                    <div class="container-fluid">
                        <div class="row">
                            <p class="left-panel-title"></i><spring:message code="filtersAndCategoriesMessage"/></p>
                        </div>
                        <c:if test="${isLogged}">
                            <div class="row mb-4">
                                <label for="matches"><spring:message code="matchesLabel"/></label>
                                <div class="btn-group input-group btn-group-toggle" data-toggle="buttons" id="matches">
                                    <label class="btn btn-secondary <c:if test="${section.equals(\"default\")}">active</c:if>" id="to-join">
                                        <input type="radio" name="options" id="option1" autocomplete="off" checked> <spring:message code="toJoinLabel"/>
                                    </label>
                                    <label class="btn btn-secondary <c:if test="${section.equals(\"joined\")}">active</c:if>" id="joined">
                                        <input type="radio" name="options" id="option2" autocomplete="off"> <spring:message code="joinedLabel"/>
                                    </label>
                                    <label class="btn btn-secondary <c:if test="${section.equals(\"created\")}">active</c:if>" id="created">
                                        <input type="radio" name="options" autocomplete="off"> <spring:message code="createdLabel"/>
                                    </label>
                                </div>
                            </div>
                        </c:if>
                        <div class="row mb-4" id="country">
                            <label for="country-filter"><spring:message code="countryLabel"/></label>
                            <input class="form-control filter-input mb-2" type="text" id="country-filter"/>
                        </div>
                        <div class="row mb-4" id="state">
                            <label for="state-filter"><spring:message code="stateLabel"/></label>
                            <input class="form-control filter-input mb-2" type="text" id="state-filter"/>
                        </div>
                        <div class="row mb-4" id="city">
                            <label for="state-filter"><spring:message code="cityLabel"/></label>
                            <input class="form-control filter-input mb-2" type="text" id="city-filter"/>
                        </div>
                    </div>
                </div>
            </div> <!-- END Leftside panel container -->
        </div>

        <div class="col-md-8 col-lg-8 col-xl-6">
            <div class="match-container container-fluid"> <!-- Match cards container -->

                <div class="row p-2 mt-2" id="loader">
                    <div class="offset-5 col-2">
                        <img class="img-fluid" src="<c:url value="/img/loader.gif"/>">
                    </div>
                </div>

            </div> <!-- END Match cards container -->
        </div>
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<c:choose>
    <c:when test="${isLogged}">
        <script>
            var filters = {
                toJoin: {
                    country: {<c:if test="${section.equals(\"default\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="country" items="${countries}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${country}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    },
                    state: {<c:if test="${section.equals(\"default\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="state" items="${states}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${state}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    },
                    city: {
                        <c:if test="${section.equals(\"default\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="city" items="${cities}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${city}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    }
                },
                joined: {
                    country: {<c:if test="${section.equals(\"joined\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="country" items="${countries}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${country}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    },
                    state: {<c:if test="${section.equals(\"joined\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="state" items="${states}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${state}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    },
                    city: {
                        <c:if test="${section.equals(\"joined\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="city" items="${cities}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${city}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    }
                },
                created: {
                    country: {<c:if test="${section.equals(\"created\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="country" items="${countries}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${country}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    },
                    state: {<c:if test="${section.equals(\"created\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="state" items="${states}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${state}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    },
                    city: {
                        <c:if test="${section.equals(\"created\")}"><c:set var="isFirst" value="${true}"/><c:forEach var="city" items="${cities}"><c:if test="${!isFirst}">${","}</c:if>
                        <c:out value="${city}: true"/><c:set var="isFirst" value="${false}"/></c:forEach></c:if>
                    }
                }
            };

            <c:choose>
                <c:when test = "${section.equals(\"created\")}">
                    var currentFilters = filters.created;
                    var section = '<c:out value="${section}"/>';
                </c:when>
                <c:when test = "${section.equals(\"joined\")}">
                    var currentFilters = filters.joined;
                    var section = '<c:out value="${section}"/>';
                </c:when>
                <c:otherwise>
                    var currentFilters = filters.toJoin;
                    var section = '';
                </c:otherwise>
            </c:choose>

        </script>

        <script src="<c:url value="/js/authenticated-index.js"/>"></script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript">
            var filters = {
                minStartTime: null,
                maxStartTime: null,
                minFinishTime: null,
                maxFinishTime: null,
                types: [],
                sportNames: [],
                minQuantity: 0,
                maxQuantity: 0,
                country: {<c:set var="isFirst" value="${true}"/><c:forEach var="country" items="${countries}"><c:if test="${!isFirst}">${","}</c:if>
                    <c:out value="${country}: true"/><c:set var="isFirst" value="${false}"/></c:forEach>
                },
                state: {<c:set var="isFirst" value="${true}"/><c:forEach var="state" items="${states}"><c:if test="${!isFirst}">${","}</c:if>
                    <c:out value="${state}: true"/><c:set var="isFirst" value="${false}"/></c:forEach>
                },
                city: {<c:set var="isFirst" value="${true}"/><c:forEach var="city" items="${cities}"><c:if test="${!isFirst}">${","}</c:if>
                    <c:out value="${city}: true"/><c:set var="isFirst" value="${false}"/></c:forEach>
                },
                minFreePlaces: 0,
                maxFreePlaces: 0
            };

            var currentFilters = filters;
            var endPointURL = contextPath + '/filterMatch';
        </script>
    </c:otherwise>
</c:choose>
<script src="<c:url value="/js/index.js"/>"></script>

</body>
</html>