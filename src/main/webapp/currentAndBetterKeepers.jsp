<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %><html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="icon" href="img/OHL_Logo.png" type="imgage/icon type">
    <title>Current and better keepers</title>
</head>
<body>
    <jsp:include page="header.jsp"></jsp:include>



    <main class="container">

        <button onclick="topFunction()" id="myBtn" title="Go to top"><i class='far fa-arrow-alt-circle-up'></i></button>

        <jsp:include page="positionSelector.jsp"></jsp:include>


        <div class="p-5">
            <h2 class="text-center justify-content-center">Current Goalkeeper</h2>
            <table class="justify-content-center table table-danger table-dark table-hover border border-danger" style="border-width: 3px !important;">
                <thead>
                    <tr class="text-danger font-weight-bold">
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Position</th>
                        <th>Type</th>
                        <th>Nationality</th>
                        <th>Details</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><c:out value="${currentplayer.firstName}" /></td>
                        <td><c:out value="${currentplayer.lastName}" /></td>
                        <td><c:out value="${currentplayer.position}" /></td>
                        <td><c:out value="${currentplayer.type}" /></td>
                        <td><c:out value="${currentplayer.nationality}" /></td>
                        <td>
                            <a href="Controller?command=PlayerDetailsGoalKeepers&selectedOption=${selectedOption}" class="btn btn-success">View Player Details</a>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="justify-content-center text-center">
                <p class="font-weight-bold mt-4 h4">
                    Select Filter Option:
                </p>
                <div>
                    <a href="/Controller?command=CurrentAndBetterKeepers&selectedOption=SavesPer90" ${selectedOption == "SavesPer90" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>
                        Saves per 90
                    </a>
                    <a href="/Controller?command=CurrentAndBetterKeepers&selectedOption=ConcededGoals" ${selectedOption == "ConcededGoals" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>
                        Conceded Goals
                    </a>
                    <a href="/Controller?command=CurrentAndBetterKeepers&selectedOption=MaxSpeed" ${selectedOption == "MaxSpeed" ? 'class="btn btn-success"' : 'class="btn btn-danger"'}>
                        Max Speed
                    </a>
                    <a href="/Controller?command=CurrentAndBetterKeepers" class="btn btn-dark">Reset</a>
                </div>
            </div>

            <c:choose>
                <c:when test="${!empty players}">

                    <c:choose>
                        <c:when test="${!empty averageSavesPer90}">
                            <p class="h4 mt-4 mb-3 justify-content-center text-center"><strong class="text-danger">Average Saves per 90 (for all Goalkeepers) :</strong> <c:out value="${averageSavesPer90}"/></p>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${!empty averageConcededGoals}">
                            <p class="h4 mt-4 mb-3 justify-content-center text-center"><strong class="text-danger">Conceded Goals (for all Goalkeepers) :</strong> <c:out value="${averageConcededGoals}"/> %</p>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${!empty averageMaxSpeed}">
                            <p class="h4 mt-4 mb-3 justify-content-center text-center"><strong class="text-danger">Max Speed (for all Goalkeepers) :</strong> <c:out value="${averageMaxSpeed}"/> km/h</p>
                        </c:when>
                        <c:otherwise></c:otherwise>
                    </c:choose>

                    <h2 class="text-center justify-content-center mt-4">Potential Goalkeepers</h2>
                    <table class="justify-content-center table table-danger table-dark table-hover border border-danger" style="border-width: 3px !important;">
                        <thead>
                        <tr class="text-danger font-weight-bold">
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Position</th>
                            <th>Type</th>
                            <th>Nationality</th>
                            <th>Details</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="player" items="${players}">
                            <tr>
                                <td><c:out value="${player.firstName}" /></td>
                                <td><c:out value="${player.lastName}" /></td>
                                <td><c:out value="${player.position}" /></td>
                                <td><c:out value="${player.type}" /></td>
                                <td><c:out value="${player.nationality}" /></td>
                                <td>
                                    <a href="Controller?command=PlayerDetailsGoalKeepers&id=${player.id}&selectedOption=${selectedOption}" class="btn btn-success">View Player Details</a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                </c:otherwise>
            </c:choose>

        </div>

    </main>

    <jsp:include page="footer.jsp"></jsp:include>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script><!-- Third party plugin JS-->
    <script src='https://kit.fontawesome.com/a076d05399.js' crossorigin='anonymous'></script>
    <script>
        // Get the button:
        let mybutton = document.getElementById("myBtn");

        // When the user scrolls down 20px from the top of the document, show the button
        window.onscroll = function() {scrollFunction()};

        function scrollFunction() {
            if (document.body.scrollTop > 400 || document.documentElement.scrollTop > 400) {
                mybutton.style.display = "block";
            } else {
                mybutton.style.display = "none";
            }
        }

        // When the user clicks on the button, scroll to the top of the document
        function topFunction() {
            document.body.scrollTop = 0; // For Safari
            document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
        }
    </script>
    <style>
        #myBtn {
            display: none; /* Hidden by default */
            position: fixed; /* Fixed/sticky position */
            bottom: 75px; /* Place the button at the bottom of the page */
            right: 30px; /* Place the button 30px from the right */
            z-index: 99; /* Make sure it does not overlap */
            border: none; /* Remove borders */
            outline: none; /* Remove outline */
            background-color: #d9534f; /* Set a background color */
            color: white; /* Text color */
            cursor: pointer; /* Add a mouse pointer on hover */
            padding: 15px; /* Some padding */
            border-radius: 10px; /* Rounded corners */
            font-size: 18px; /* Increase font size */
        }

        #myBtn:hover {
            background-color: #555; /* Add a dark-grey background on hover */
        }
    </style>
</body>
</html>
