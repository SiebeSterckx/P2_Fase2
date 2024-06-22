<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <link rel="icon" href="img/OHL_Logo.png" type="imgage/icon type">
    <title>playerOverview</title>
</head>
<body>
    <jsp:include page="header.jsp"></jsp:include>

    <main class="container">

        <button onclick="topFunction()" id="myBtn" title="Go to top"><i class='far fa-arrow-alt-circle-up'></i></button>

        <div>
<%--            Filter options--%>
        </div>

        <div class="p-5">
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
                            <a href="Controller?command=PlayerDetails&id=${player.id}" class="btn btn-success">View Player Details</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

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