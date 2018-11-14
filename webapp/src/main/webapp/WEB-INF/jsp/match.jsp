<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%-- Include Bootstrap v4.1.3 and Custom CSS --%>
    <jsp:include page="css.jsp"></jsp:include>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="icon" href="<c:url value="/img/bullseye-solid.ico"/>"/>
    <title>skore</title>
</head>
<body>

<%-- Include Navigation Bars --%>
<jsp:include page="navbar.jsp"></jsp:include>

<div class="container-fluid">
    <div class="row">
        <div class="container-fluid sign-in-container offset-sm-2 col-sm-8 offset-md-3 col-md-6 offset-lg-3 col-lg-6 offset-xl-4 col-xl-4"> <!-- Form container -->

            <div class="row text-center">
                <div class="col">
                    <h1><c:out value="${match.getTitle()}"/></h1>
                </div>
            </div>
            <div class="row text-center">
                <div class="col">
                    <img src="<c:url value="/sport/image/${match.getTeam1().getSport().getName()}"/>" class="sport-img mr-2" alt="sport-pic"><c:out value="${match.getTeam1().getSport().getDisplayName()}"/>
                </div>
            </div>
            <div class="row text-center">
                <div class="col">
                    <img src="<c:url value="/profile/image/${match.getTeam1().getLeader().getUserName()}"/>" onclick="clickAvatar('<c:out value="${match.getTeam1().getLeader().getUserName()}"/>'); event.stopPropagation();" class="user-avatar" alt="user-pic">
                </div>
            </div>
            <div class="row text-center">
                <div class="col">
                    <a class="username-label" href="<c:url value="/profile/${match.getTeam1().getLeader().getUserName()}"/>">@<c:out value="${match.getTeam1().getLeader().getUserName()}"/></a>
                </div>
            </div>
            <div class="row text-center">
                <div class="col">
                    <c:choose>
                        <c:when test="${match.getCompetitiveness().equals(\"Friendly\")}">
                            <p><span class="friendly-icon mr-2 fas fa-handshake"></span><spring:message code="friendlyLabel"/><span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="friendlyTooltip"/>"/></p>
                        </c:when>
                        <c:otherwise>
                            <p><span class="competitive-icon mr-2 fas fa-medal"></span><spring:message code="competitiveLabel"/><span class="tooltip-icon ml-2 far fa-question-circle"  onclick="event.stopPropagation();" data-toggle="tooltip" data-placement="right" data-html="true" title="<spring:message code="competitiveTooltip"/>"/></p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="row text-center">
                <div class="col">
                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span><spring:message code="match.startTime"/>: <c:out value="${match.getStartTimeString()}"/></p>
                </div>
            </div>
            <div class="row text-center">
                <div class="col">
                    <p><span class="calendar-icon mr-2 fas fa-calendar-alt"></span><spring:message code="match.finishTime"/>: <c:out value="${match.getFinishTimeString()}"/></p>
                </div>
            </div>
            <c:if test="${match.getResult() != null}">
                <div class="row text-center">
                    <div class="col">
                        <label><spring:message code="match.result"/>: <label><c:out value="${match.getResult()}"/></label></label>
                    </div>
                </div>
            </c:if>
            <c:if test="${match.getDescription() != null && match.getDescription().length() > 0}">
                <div class="row text-center">
                    <div class="col">
                        <i class="fas mr-2 fa-comment-alt"></i><spring:message code="match.description"/>: <c:out value="${match.getDescription()}"/>
                    </div>
                </div>
            </c:if>
            <c:if test="${canEdit}">
                <a class="btn btn-green mb-2" href="<c:url value="/submitMatchResult/${matchURLKey}"/>" role="button"><i class="fas fa-plus mr-1"></i><spring:message code="addResultLabel"/></a>
            </c:if>
            <c:if test="${match.result != null}}">
                <div>
                    ${match.result}
                </div>
            </c:if>

            <div class="row text-center">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <c:choose>
                            <c:when test="${match.getTeam1().isTemporal()}">
                                <th scope="col"><spring:message code="match.team"/> 1</th>
                            </c:when>
                            <c:otherwise>
                                <th scope="col"><c:out value="${match.getTeam1().getName()}"/></th>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${match.getTeam2()==null || match.getTeam2().isTemporal()}">
                                <th scope="col"><spring:message code="match.team"/> 2</th>
                            </c:when>
                            <c:otherwise>
                                <th scope="col"><c:out value="${match.getTeam2().getName()}"/></th>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var = "iterator1" value = "${match.getTeam1().getAccountsPlayers().entrySet().iterator()}"/>
                    <c:set var = "iterator2" value = "${(match.getTeam2()==null)?null:match.getTeam2().getAccountsPlayers().entrySet().iterator()}"/>
                    <c:choose>
                        <c:when test="${match.getTeam2()==null || match.getTeam2().getAccountsPlayers().size() < match.getTeam1().getAccountsPlayers().size()}">
                            <c:forEach items="${match.getTeam1().getAccountsPlayers().entrySet()}" var="entry">
                                <tr>
                                    <td scope="row">
                                        <c:choose>
                                            <c:when test="${entry.getValue() == null}">
                                                <c:out value="${entry.getKey().getFirstName()}"/> <c:out value="${entry.getKey().getLastName()}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value = "/profile/${entry.getValue().getUserName()}"/>">
                                                    @<c:out value="${entry.getValue().getUserName()}"/> <%--TODO--%>
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <c:choose>
                                        <c:when test="${match.getTeam2() == null || !iterator2.hasNext()}">
                                            <td></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td>
                                                <c:set var = "entry2" value = "${iterator2.next()}"/>
                                                <c:choose>
                                                    <c:when test="${entry2.getValue() == null}">
                                                        <c:out value="${entry2.getKey().getFirstName()}"/> <c:out value="${entry2.getKey().getLastName()}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="<c:url value = "/profile/${entry2.getValue().getUserName()}"/>">
                                                            @<c:out value="${entry2.getValue().getUserName()}"/> <%--TODO--%>
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <c:forEach items="${match.getTeam2().getAccountsPlayers().entrySet()}" var="entry">
                                <tr>
                                    <c:choose>
                                        <c:when test="${!iterator1.hasNext()}">
                                            <td scope="row"></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td scope="row">
                                                <c:set var = "entry2" value = "${iterator1.next()}"/>
                                                <c:choose>
                                                    <c:when test="${entry2.getValue() == null}">
                                                        <c:out value="${entry2.getKey().getFirstName()}"/> <c:out value="${entry2.getKey().getLastName()}"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="<c:url value = "/profile/${entry2.getValue().getUserName()}"/>">
                                                            @<c:out value="${entry2.getValue().getUserName()}"/> <%--TODO--%>
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </c:otherwise>
                                    </c:choose>
                                    <td>
                                        <c:choose>
                                            <c:when test="${entry.getValue() == null}">
                                                <c:out value="${entry.getKey().getFirstName()}"/> <c:out value="${entry.getKey().getLastName()}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="<c:url value = "/profile/${entry.getValue().getUserName()}"/>">
                                                    @<c:out value="${entry.getValue().getUserName()}"/> <%--TODO--%>
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div> <!-- END Form container -->
    </div>
</div>

<%-- Include JS Scripts --%>
<jsp:include page="js.jsp"></jsp:include>
<script src="<c:url value="/js/enable-tooltips.js"/>" charset="utf-8" type="text/javascript"></script>
<script src="<c:url value="/js/clickToGo.js"/>" charset="utf-8" type="text/javascript"></script>

</body>
</html>
