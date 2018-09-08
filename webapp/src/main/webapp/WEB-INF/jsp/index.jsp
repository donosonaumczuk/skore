<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
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

            <div class="d-none d-md-block fixed-top sidepanel col-md-4 col-lg-4 offset-xl-1 col-xl-3">
                <div class="container-fluid sidepanel-container"> <!-- Leftside panel container -->
                    <div class="row">
                        <div class="col text-center create-match p-4">
                            <p class="">¿No encontras el tipo de partido que buscas?</p>
                            <button class="btn btn-white-succ" type="submit">ORGANIZAR UN PARTIDO</button>
                        </div>
                    </div>
                    <div class="row filters p-4 mt-2">
                        <div class="col">
                            <div class="row">
                                <p class="">Filtros y categorias</p>
                            </div>
                            <div class="row">
                                <p class="">Ordenar por</p>
                            </div>
                            <div class="row">
                                <p class="">Mas recientes</p>
                            </div>
                            <div class="row">
                                <p class="">Jugadores faltantes</p>
                            </div>
                            <div class="row">
                                <p class="">Ubicacion</p>
                            </div>
                            <div class="row">
                                <p class="">CABA</p>
                            </div>
                            <div class="row">
                                <p class="">GBA</p>
                            </div>
                        </div>
                    </div>
                </div> <!-- END Leftside panel container -->
            </div>

            <div class="offset-md-4 col-md-8 offset-lg-4 col-lg-8 offset-xl-4 col-xl-5">
                <div class="match-container container-fluid"> <!-- Match cards container -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row">
                                <div class="col-3 col-xl-1">
                                    <img src="img/user-default.svg" class="img-fluid" alt="user-pic">
                                </div>
                                <div class="col-3 col-xl-3">
                                    <div class="row">
                                        <p class="name-label">Alan</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="">@donosonaumczuk</a>
                                    </div>
                                </div>
                                <div class="col-2 col-xl-3 pt-2">
                                    <div class="row">
                                        <div class="col col-xl-3">
                                            <img src="img/football.svg" class="img-fluid" alt="sport-pic">
                                        </div>
                                        <div class="col-6 col-xl-9 d-none d-sm-block">
                                            <p class="sport-label">Futbol</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 col-xl-5">
                                    <div class="row text-center">
                                        <div class="col-3 p-0 offset-xl-3 col-xl-2">
                                            <img src="img/people.svg" class="img-fluid" alt="people-pic">
                                        </div>
                                        <div class="col-8 col-xl-4">
                                            7 / 10
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green">+ UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/calendar.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">Domingo 11 de Agosto de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/clock.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">19:00 - 20:00</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/location.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class=""> Stadium Futbol 5, Ituzaingo Sur, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row">
                                <div class="col-3 col-xl-1">
                                    <img src="img/user-default.svg" class="img-fluid" alt="user-pic">
                                </div>
                                <div class="col-3 col-xl-3">
                                    <div class="row">
                                        <p class="name-label">Agustin Dammiano</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="">@dammiano98</a>
                                    </div>
                                </div>
                                <div class="col-2 col-xl-3 pt-2">
                                    <div class="row">
                                        <div class="col col-xl-3">
                                            <img src="img/football.svg" class="img-fluid" alt="sport-pic">
                                        </div>
                                        <div class="col-6 col-xl-9 d-none d-sm-block">
                                            <p class="sport-label">Futbol</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 col-xl-5">
                                    <div class="row text-center">
                                        <div class="col-3 p-0 offset-xl-3 col-xl-2">
                                            <img src="img/people.svg" class="img-fluid" alt="people-pic">
                                        </div>
                                        <div class="col-8 col-xl-4">
                                            18 / 22
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green">+ UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/calendar.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">Lunes 24 de Septiembre de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/clock.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">16:00 - 17:30</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/location.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class=""> Predio Deportivo Peron, Nordelta, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row">
                                <div class="col-3 col-xl-1">
                                    <img src="img/user-default.svg" class="img-fluid" alt="user-pic">
                                </div>
                                <div class="col-3 col-xl-3">
                                    <div class="row">
                                        <p class="name-label">Agustin</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="">@agustinizaguirre</a>
                                    </div>
                                </div>
                                <div class="col-2 col-xl-3 pt-2">
                                    <div class="row">
                                        <div class="col col-xl-3">
                                            <img src="img/football.svg" class="img-fluid" alt="sport-pic">
                                        </div>
                                        <div class="col-6 col-xl-9 d-none d-sm-block">
                                            <p class="sport-label">Futbol</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 col-xl-5">
                                    <div class="row text-center">
                                        <div class="col-3 p-0 offset-xl-3 col-xl-2">
                                            <img src="img/people.svg" class="img-fluid" alt="people-pic">
                                        </div>
                                        <div class="col-8 col-xl-4">
                                            2 / 10
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green">+ UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/calendar.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">Jueves 13 de Septiembre de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/clock.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">09:00 - 10:00</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/location.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class=""> Muni Futbol, Recoleta, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row">
                                <div class="col-3 col-xl-1">
                                    <img src="img/user-default.svg" class="img-fluid" alt="user-pic">
                                </div>
                                <div class="col-3 col-xl-3">
                                    <div class="row">
                                        <p class="name-label">Alan</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="">@donosonaumczuk</a>
                                    </div>
                                </div>
                                <div class="col-2 col-xl-3 pt-2">
                                    <div class="row">
                                        <div class="col col-xl-3">
                                            <img src="img/football.svg" class="img-fluid" alt="sport-pic">
                                        </div>
                                        <div class="col-6 col-xl-9 d-none d-sm-block">
                                            <p class="sport-label">Futbol</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 col-xl-5">
                                    <div class="row text-center">
                                        <div class="col-3 p-0 offset-xl-3 col-xl-2">
                                            <img src="img/people.svg" class="img-fluid" alt="people-pic">
                                        </div>
                                        <div class="col-8 col-xl-4">
                                            7 / 10
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green">+ UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/calendar.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">Domingo 11 de Agosto de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/clock.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">19:00 - 20:00</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/location.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class=""> Stadium Futbol 5, Ituzaingo Sur, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row">
                                <div class="col-3 col-xl-1">
                                    <img src="img/user-default.svg" class="img-fluid" alt="user-pic">
                                </div>
                                <div class="col-3 col-xl-3">
                                    <div class="row">
                                        <p class="name-label">Agustin Dammiano</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="">@dammiano98</a>
                                    </div>
                                </div>
                                <div class="col-2 col-xl-3 pt-2">
                                    <div class="row">
                                        <div class="col col-xl-3">
                                            <img src="img/football.svg" class="img-fluid" alt="sport-pic">
                                        </div>
                                        <div class="col-6 col-xl-9 d-none d-sm-block">
                                            <p class="sport-label">Futbol</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 col-xl-5">
                                    <div class="row text-center">
                                        <div class="col-3 p-0 offset-xl-3 col-xl-2">
                                            <img src="img/people.svg" class="img-fluid" alt="people-pic">
                                        </div>
                                        <div class="col-8 col-xl-4">
                                            18 / 22
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green">+ UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/calendar.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">Lunes 24 de Septiembre de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/clock.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">16:00 - 17:30</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/location.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class=""> Predio Deportivo Peron, Nordelta, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                    <div class="row p-2 mt-2 match-card rounded-border"> <!-- match card -->
                        <div class="col">
                            <div class="row">
                                <div class="col-3 col-xl-1">
                                    <img src="img/user-default.svg" class="img-fluid" alt="user-pic">
                                </div>
                                <div class="col-3 col-xl-3">
                                    <div class="row">
                                        <p class="name-label">Agustin</p>
                                    </div>
                                    <div class="row">
                                        <a class="username-label" href="">@agustinizaguirre</a>
                                    </div>
                                </div>
                                <div class="col-2 col-xl-3 pt-2">
                                    <div class="row">
                                        <div class="col col-xl-3">
                                            <img src="img/football.svg" class="img-fluid" alt="sport-pic">
                                        </div>
                                        <div class="col-6 col-xl-9 d-none d-sm-block">
                                            <p class="sport-label">Futbol</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-4 col-xl-5">
                                    <div class="row text-center">
                                        <div class="col-3 p-0 offset-xl-3 col-xl-2">
                                            <img src="img/people.svg" class="img-fluid" alt="people-pic">
                                        </div>
                                        <div class="col-8 col-xl-4">
                                            2 / 10
                                        </div>
                                    </div>
                                    <div class="row text-center">
                                        <div class="col mt-xl-2 ml-xl-4">
                                            <button class="btn btn-green">+ UNIRSE</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/calendar.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">Jueves 13 de Septiembre de 2018</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/clock.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class="">09:00 - 10:00</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-1">
                                    <img src="img/location.svg" class="img-fluid" alt="date-pic">
                                </div>
                                <div class="col">
                                    <p class=""> Muni Futbol, Recoleta, Buenos Aires </p>
                                </div>
                            </div>
                        </div>
                    </div> <!-- END match card -->

                </div> <!-- END Match cards container -->
            </div>

            <!-- <div class="col-3">
                <div class="container-fluid bg-white">

                </div>
            </div> -->
        </div>
    </div>

    <%-- Include JS Scripts --%>
    <jsp:include page="js.jsp"></jsp:include>

</body>
</html>