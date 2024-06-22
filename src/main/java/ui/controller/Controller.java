package ui.controller;

import domain.service.AppService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/Controller")
public class Controller extends HttpServlet {


    HandlerFactory handlerFactory = new HandlerFactory();
    AppService service = new AppService();


    public Controller() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request,response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session;
        session = request.getSession();
        String destination = "index.jsp";

        String command = request.getParameter("command");

        if (command != null) {

            RequestHandler handler = handlerFactory.getHandler(command, service);
            destination = handler.handleRequest(request, response);

        }

        RequestDispatcher view = request.getRequestDispatcher(destination);
        view.forward(request, response);
    }

}
