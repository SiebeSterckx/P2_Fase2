package ui.controller;

import domain.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PlayerDetailsGoalKeepers extends RequestHandler{
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String selectedOption = request.getParameter("selectedOption");

        if (id != null) {
            request.setAttribute("player", service.getKeeper(id));
        }
        request.setAttribute("current", service.getCurrentKeeper());
        request.setAttribute("currentMaxSpeed", service.getMaxSpeed(service.getCurrentKeeper().getId()));
        request.setAttribute("currentExpectedConcededGoals", service.getExpConcededGoals(service.getCurrentKeeper().getId()));
        request.setAttribute("selectedOption", selectedOption);

        return "playerDetailsGoalKeepers.jsp";
    }
}
