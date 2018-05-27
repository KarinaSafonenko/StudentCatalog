package controller;

import command.Command;
import command.CommandFactory;
import util.PagePath;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/ControllerServlet")
public class Controller extends HttpServlet {
    private static final String COMMAND = "command";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter(COMMAND);
        Optional<Command> command = new CommandFactory().defineCommand(action);
        if (command.isPresent()){
            Trigger trigger = command.get().execute(request);
            if (trigger.getRoot().equals(Trigger.TriggerType.FORWARD)){
                request.getRequestDispatcher(trigger.getPagePath()).forward(request, response);
            }else{
                response.sendRedirect(trigger.getPagePath());
            }
        }
        else{
            response.sendRedirect(request.getContextPath() + PagePath.INDEX_PATH);
        }
    }
}
