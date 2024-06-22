<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="icon" href="img/OHL_Logo.png" type="imgage/icon type">
    <title>playerDetails</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<main class="container">

    <h2 class="justify-content-center text-center my-3">Player Details</h2>
    <div class="row">
        <div class="col">
            <div class="card my-4 border border-danger" style="border-width: 3px !important;">
                <div class="card-body bg-dark">
                    <h3 class="text-center text-white border-bottom border-danger border-bottom-spacing">Current Goalkeeper</h3>
                    <h4 class="card-title text-success">${current.matchName}</h4>
                    <h5 class="card-subtitle mb-2 text-white">${current.position}</h5>
                    <p class="card-text text-white h6 border-bottom border-danger border-bottom-spacing">
                        <strong class="text-danger">First Name:</strong> ${current.firstName}<br>
                        <strong class="text-danger">Last Name:</strong> ${current.lastName}<br>
                        <strong class="text-danger">Nationality:</strong> ${current.nationality}<br>
                        <strong class="text-danger">Number:</strong> ${current.nr}<br>
                        <strong class="text-danger">Active:</strong> ${current.active}<br>
                        <strong class="text-danger">Club:</strong> ${current.contestantClubName} (${current.contestantCode})<br>
                    </p>
                    <h5 class="card-subtitle mt-3 mb-2 text-white">Stats:</h5>
                    <div class="card-text text-white h6">
                        <p>
                            <strong class="text-danger">Saves per 90:</strong> ${String.format("%.3f", Float.parseFloat(current.goalsper90))}
                        </p>
                        <p>
                            <strong class="text-danger">Conceded Goals:</strong> ${String.format("%.3f", Float.parseFloat(current.actualDevidedByExpectedGoals))} %
                        </p>
                        <p>
                            <strong class="text-danger">Max speed:</strong> ${String.format("%.3f", Float.parseFloat(current.maxSpeed))} km/h
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <c:choose>
            <c:when test="${!empty player}">
                <div class="col">
                    <div class="card my-4 border border-danger" style="border-width: 3px !important;">
                        <div class="card-body bg-dark">
                            <h3 class="text-center text-white border-bottom border-danger border-bottom-spacing">Selected Goalkeeper</h3>
                            <h4 class="card-title text-success">${player.matchName}</h4>
                            <h5 class="card-subtitle mb-2 text-white">${player.position}</h5>
                            <p class="card-text text-white h6 border-bottom border-danger border-bottom-spacing">
                            <strong class="text-danger">First Name:</strong> ${player.firstName}<br>
                            <strong class="text-danger">Last Name:</strong> ${player.lastName}<br>
                            <strong class="text-danger">Nationality:</strong> ${player.nationality}<br>
                            <strong class="text-danger">Number:</strong> ${player.nr}<br>
                            <strong class="text-danger">Active:</strong> ${player.active}<br>
                            <strong class="text-danger">Club:</strong> ${player.contestantClubName} (${player.contestantCode})<br>
                            </p>
                            <h5 class="card-subtitle mt-3 mb-2 text-white">Stats:</h5>
                            <div class="card-text text-white h6">
                                <p ${Float.parseFloat(current.goalsper90) > Float.parseFloat(player.goalsper90) ? "class='text-warning'" : "class='text-success'"}>
                                    <strong class="text-danger">Saves per 90:</strong> ${String.format("%.3f", Float.parseFloat(player.goalsper90))}
                                </p>
                                <p ${Float.parseFloat(current.actualDevidedByExpectedGoals) < Float.parseFloat(player.actualDevidedByExpectedGoals) ? "class='text-warning'" : "class='text-success'"}>
                                    <strong class="text-danger">Conceded Goals:</strong> ${String.format("%.3f", Float.parseFloat(player.actualDevidedByExpectedGoals))} %
                                </p>
                                <p ${Float.parseFloat(current.goalsper90) > Float.parseFloat(player.goalsper90) ? "class='text-warning'" : "class='text-success'"}>
                                    <strong class="text-danger">Max speed:</strong> ${String.format("%.3f", Float.parseFloat(player.maxSpeed))} km/h
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
            </c:otherwise>
        </c:choose>
    </div>


    <a href="Controller?command=CurrentAndBetterKeepers&selectedOption=${selectedOption}" class="btn btn-success d-flex justify-content-center">Back</a>
</main>



<jsp:include page="footer.jsp"></jsp:include>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script><!-- Third party plugin JS-->
<style>
    .border-bottom-spacing {
        padding-bottom: 10px;
    }

</style>
</body>
</html>