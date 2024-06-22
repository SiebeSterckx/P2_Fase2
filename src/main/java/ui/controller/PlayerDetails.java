package ui.controller;

import domain.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PlayerDetails extends RequestHandler{
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        List<Player> players = service.getAll();
        Player player = null;

        for (Player p : players) {
            if (p.getId().equals(id)) {
                player = p;
            }
        }

        request.setAttribute("player", player);

        return "playerDetails.jsp";
    }
}
