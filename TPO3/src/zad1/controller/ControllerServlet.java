package controller;

import exception.NoCommandException;
import international.BundleInfo;
import model.Command;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ControllerServlet extends HttpServlet {

    private ServletContext context;
    private Command command;
    private String presentationEndpoint;
    private String parametersEndpoint;

    @Override
    public void init() {
        context = getServletContext();
        presentationEndpoint = context.getInitParameter("presentationEndpoint");
        parametersEndpoint = context.getInitParameter("parametersEndpoint");
        String commandClassName = context.getInitParameter("commandClassName");
        String databaseName = context.getInitParameter("databaseName");

        try {
            Class<?> commandClass = Class.forName(commandClassName);
            command = (Command) commandClass.newInstance();
            command.setParameter("databaseName", databaseName);
            command.init();
        } catch (Exception e) {
            throw new NoCommandException("No command class for name: " + commandClassName, e);
        }
    }

    public void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        RequestDispatcher requestDispatcher = context.getRequestDispatcher(parametersEndpoint);
        requestDispatcher.include(request, response);

        HttpSession session = request.getSession();
        String[] commandParameterNames = BundleInfo.getCommandParameterNames();

        for (String parameterName : commandParameterNames) {
            String attribute = (String) session.getAttribute("param_" + parameterName);
            command.setParameter(parameterName, attribute);
        }

        Lock lock = new ReentrantLock();
        lock.lock();
        command.execute();
        String[][] results = command.getResults();
        session.setAttribute("StatusCode", command.getStatusCode());
        session.setAttribute("Results", results);
        session.setAttribute("Lock", lock);

        requestDispatcher = context.getRequestDispatcher(presentationEndpoint);
        requestDispatcher.forward(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serviceRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        serviceRequest(request, response);
    }

}
