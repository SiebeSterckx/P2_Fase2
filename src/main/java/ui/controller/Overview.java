package ui.controller;

import domain.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Overview extends RequestHandler{
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {

        List<Player> players = service.getAll();
        request.setAttribute("players", players);

        return "playerOverview.jsp";
    }
}
