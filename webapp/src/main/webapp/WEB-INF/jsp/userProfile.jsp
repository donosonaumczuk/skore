<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="icon" href="<c:url value="/img/bullseye-solid.ico"/>"/>
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4">

            <div class="row text-center">
                <div class="col">
                    <img class="profile-pic" src="<c:url value="/profile/image/${user.getUserName()}"/>"/>
                </div>
            </div>

            <div class="container-fluid profile-container bg-white rounded-border">

                <div class="row text-center">
                    <div class="col">
                        <p class="profile-name"><c:out value="${user.getFullName()}"/></p>
                    </div>
                </div>

                <div class="row text-center">
                    <div class="col">
                        <p class="profile-username">@<c:out value="${user.getUserName()}"/></p>
                    </div>
                </div>

                <div class="row text-center">
                    <div class="col">
                        <p class="profile-data">
                            <i class="fas mr-1 fa-percentage"></i>
                            <c:choose>
                                <c:when test="${user.getWinRate() >= 50}">
                                    <span class="winnerWinRate">
                                        <c:out value="${user.getWinRate()}"/>
                                    </span>
                                </c:when>
                                <c:when test="${user.getWinRate() >= 0}">
                                    <span class="loserWinRate">
                                        <c:out value="${user.getWinRate()}"/>
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    --
                                </c:otherwise>
                            </c:choose>
                            <i class="fas ml-4 mr-1 fa-birthday-cake"></i>
                            <c:out value="${user.getAge()}"/>
                        </p>
                    </div>
                </div>

                <div class="row text-center">
                    <div class="col">
                        <p class="profile-data">
                            <i class="fas fa-map-marker-alt"></i>
                            <c:out value="${user.getHome().getPublicLocation()}"/>
                        </p>
                    </div>
                </div>

                <div class="row text-center">
                    <div class="col">
                        <c:if test="${isLogged && loggedUser.userName.equals(user.userName)}">
                            <a class="btn btn-outline-secondary mx-1" href="<c:url value="/editInfo"/>" role="button"><i class="mr-1 fas fa-edit"></i><spring:message code="editInfoMessage"/></a><a class="btn btn-outline-secondary mx-1" href="<c:url value="/changePassword"/>" role="button"><i class="mr-1 fas fa-key"></i><spring:message code="changePasswordMessage"/></a>
                        </c:if>
                    </div>
                </div>
            </div>


            <div class="container-fluid mt-4 rounded-border">

                <c:forEach items="${homeGames}" var="game">

                    <div class="row p-2 mt-2 match-card rounded-border" onclick="clickMatch('<c:out value="${game.getPrimaryKey().toURLKey()}"/>')" id="<c:out value="${game.getPrimaryKey().toURLKey()}"/>">
                        <div class="col">
                            <div class="row mb-4">
                                <div class="col-2 col-sm-1 pl-0">
                                    <img src="<c:url value="/profile/image/${game.getTeam1().getLeader().getUserName()}"/>" onclick="clickAvatar('<c:out value="${game.getTeam1().getLeader().getUserName()}"/>'); event.stopPropagation();" class="user-avatar" alt="user-pic">
                                </div>
                                <div class="col-3 col-sm-4">
                                    <div class="row">
                                        <p class="name-label"><c:out value="${game.getTitle()}"/></p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="<c:url value="/profile/${game.getTeam1().getLeader().getUserName()}"/>">@<c:out value="${game.getTeam1().getLeader().getUserName()}"/></a>
                                    </div>
                                </div>
                                <div class="col-2 col-sm-3">
                                    <div class="container-fluid pt-2">
                                        <div class="row">
                                            <div class="col col-xl-4 mr-0 mt-1">
                                                <img src="<c:url value="/sport/image/${game.getTeam1().getSport().getName()}"/>" class="sport-img" alt="sport-pic">
                                            </div>
                                            <div class="col-6 col-xl d-none d-sm-block pl-0">
                                                <p class="sport-label"><c:out value="${game.getTeam1().getSport().getDisplayName()}"/></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="offset-1 col-4 col-sm-3">
                                    <div class="row text-center">
                                        <div class="col">
                                            <p class="result-label"><c:out value="${game.getResult()}"/></p>
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <c:if test="${(game.firstScoreFromResult() > game.secondScoreFromResult())}">
                                                <i class="name-label fas fa-check-circle mr-2"></i><spring:message code="winLabel"/>
                                            </c:if>
                                            <c:if test="${(game.firstScoreFromResult() == game.secondScoreFromResult())}">
                                                <i class="name-label fas fa-minus-circle mr-2"></i><spring:message code="tieLabel"/>
                                            </c:if>
                                            <c:if test="${(game.firstScoreFromResult() < game.secondScoreFromResult())}">
                                                <i class="name-label fas fa-times-circle mr-2"></i><spring:message code="loseLabel"/>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <c:choose>
                                        <c:when test="${game.getCompetitiveness().equals(\"Friendly\")}">
                                            <p><span class="friendly-icon mr-2 fas fa-handshake"></span><spring:message code="friendlyLabel"/><span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="friendlyTooltip"/>"/></p>
                                        </c:when>
                                        <c:otherwise>
                                            <p><span class="competitive-icon mr-2 fas fa-medal"></span><spring:message code="competitiveLabel"/><span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="competitiveTooltip"/>"/></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span><c:out value="${game.getStartTimeString()}"/></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span><c:out value="${game.getFinishTimeString()}"/></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="location-icon mr-2 fas fa-map-marker-alt"></span><c:out value="${game.getPlace().toString()}"/></p>
                                </div>
                            </div>
                        </div>
                    </div>

                </c:forEach>

                <c:forEach items="${awayGames}" var="game">

                    <div class="row p-2 mt-2 match-card rounded-border" onclick="clickMatch('<c:out value="${game.getPrimaryKey().toURLKey()}"/>')" id="<c:out value="${game.getPrimaryKey().toURLKey()}"/>">
                        <div class="col">
                            <div class="row mb-4">
                                <div class="col-2 col-sm-1 pl-0">
                                    <img src="<c:url value="/profile/image/${game.getTeam1().getLeader().getUserName()}"/>" onclick="clickAvatar('<c:out value="${game.getTeam1().getLeader().getUserName()}"/>'); event.stopPropagation();" class="user-avatar" alt="user-pic">
                                </div>
                                <div class="col-3 col-sm-4">
                                    <div class="row">
                                        <p class="name-label"><c:out value="${game.getTitle()}"/></p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="<c:url value="/profile/${game.getTeam1().getLeader().getUserName()}"/>">@<c:out value="${game.getTeam1().getLeader().getUserName()}"/></a>
                                    </div>
                                </div>
                                <div class="col-2 col-sm-3">
                                    <div class="container-fluid pt-2">
                                        <div class="row">
                                            <div class="col col-xl-4 mr-0 mt-1">
                                                <img src="<c:url value="/sport/image/${game.getTeam1().getSport().getName()}"/>" class="sport-img" alt="sport-pic">
                                            </div>
                                            <div class="col-6 col-xl d-none d-sm-block pl-0">
                                                <p class="sport-label"><c:out value="${game.getTeam1().getSport().getDisplayName()}"/></p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="offset-1 col-4 col-sm-3">
                                    <div class="row text-center">
                                        <div class="col">
                                            <p class="result-label"><c:out value="${game.getResult()}"/></p>
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <c:choose>
                                                <c:when test="${Integer.parseInt(game.getResult().split(\"-\")[0]) < Integer.parseInt(game.getResult().split(\"-\")[1])}">
                                                    <i class="name-label fas fa-check-circle mr-2"></i><spring:message code="winLabel"/>
                                                </c:when>
                                                <c:when test="${Integer.parseInt(game.getResult().split(\"-\")[0]) == Integer.parseInt(game.getResult().split(\"-\")[1])}">
                                                    <i class="name-label fas fa-minus-circle mr-2"></i><spring:message code="tieLabel"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="name-label fas fa-times-circle mr-2"></i><spring:message code="loseLabel"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <c:choose>
                                        <c:when test="${game.getCompetitiveness().equals(\"Friendly\")}">
                                            <p><span class="friendly-icon mr-2 fas fa-handshake"></span><spring:message code="friendlyLabel"/><span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="friendlyTooltip"/>"/></p>
                                        </c:when>
                                        <c:otherwise>
                                            <p><span class="competitive-icon mr-2 fas fa-medal"></span><spring:message code="competitiveLabel"/><span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="competitiveTooltip"/>"/></p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span><c:out value="${game.getStartTimeString()}"/></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span><c:out value="${game.getFinishTimeString()}"/></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <p><span class="location-icon mr-2 fas fa-map-marker-alt"></span><c:out value="${game.getPlace().toString()}"/></p>
                                </div>
                            </div>
                        </div>
                    </div>

                </c:forEach>

            </div>

        </div>
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="<c:url value="/js/clickToGo.js"/>" charset="utf-8" type="text/javascript"></script>
<script src="<c:url value="/js/profile.js"/>" charset="utf-8" type="text/javascript"></script>
</body>
</html>