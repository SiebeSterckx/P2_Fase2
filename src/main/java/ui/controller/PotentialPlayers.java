package ui.controller;

import domain.model.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PotentialPlayers extends RequestHandler{
    @Override
    public String handleRequest(HttpServletRequest request, HttpServletResponse response) {

        return "potentialPlayers.jsp";
    }
}
