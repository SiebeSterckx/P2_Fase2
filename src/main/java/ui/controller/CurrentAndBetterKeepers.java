package ui.controller;

import domain.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CurrentAndBetterKeepers extends RequestHandler{
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {

        String selectedOption = request.getParameter("selectedOption");

        List<Player> players = null;
        String averageSavesPer90S = null;
        String averageDmConcededGoalsS = null;
        String averageMaxSpeedS = null;

        if (selectedOption != null) {
            if (selectedOption.equals("SavesPer90")) {
                players = service.getBetterKeepersSavesPer90();
                Float averageSavesPer90 = service.getAverageSavesPer90();
                averageSavesPer90S = String.format("%.3f", averageSavesPer90);

            }
            else if (selectedOption.equals("ConcededGoals")) {
                players = service.getBetterKeepersActualDevidedByExpectedGoals();
                Float averageDmConcededGoals = service.averageDmConcededGoals();
                averageDmConcededGoalsS = String.format("%.3f", averageDmConcededGoals);

            }
            else if (selectedOption.equals("MaxSpeed")) {
                players = service.getFasterMaxSpeedKeepers();
                Double averageMaxSpeed = service.averageMaxSpeed();
                averageMaxSpeedS = String.format("%.3f", averageMaxSpeed);
            }


        }
        Player current = service.getCurrentKeeper();

        request.setAttribute("currentplayer", current);
        request.setAttribute("players", players);

        request.setAttribute("averageSavesPer90", averageSavesPer90S);
        request.setAttribute("averageConcededGoals", averageDmConcededGoalsS);
        request.setAttribute("averageMaxSpeed", averageMaxSpeedS);

        request.setAttribute("selectedPosition", "Goalkeeper");
        request.setAttribute("selectedOption", selectedOption);


        return "currentAndBetterKeepers.jsp";
    }
}
