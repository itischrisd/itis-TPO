package view;

import international.BundleInfo;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;

public class PresentationServlet extends HttpServlet {

    public void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String parametersEndpoint = context.getInitParameter("parametersEndpoint");
        RequestDispatcher requestDispatcher = context.getRequestDispatcher(parametersEndpoint);
        requestDispatcher.include(request, response);

        HttpSession session = request.getSession();
        Lock lock = (Lock) session.getAttribute("Lock");
        lock.unlock();

        String[][] results = (String[][]) session.getAttribute("Results");
        Integer statusCode = (Integer) session.getAttribute("StatusCode");

        String statusMessage = BundleInfo.getStatusMessage()[statusCode];
        String[] resultDescriptions = BundleInfo.getResultDescriptions();

        PrintWriter out = response.getWriter();

        out.println("<style>");
        out.println("table { width: 100%; border-collapse: collapse; }");
        out.println("th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("tr:hover { background-color: #f5f5f5; }");
        out.println("</style>");

        out.println("<hr>");
        out.println("<h2>" + statusMessage + "</h2>");

        out.println("<table border=\"1\">");
        out.println("<tr>");

        for (String resultDescription : resultDescriptions) {
            out.println("<th>" + resultDescription + "</th>");
        }

        out.println("</tr>");

        for (String[] result : results) {
            out.println("<tr>");
            for (String value : result) {
                out.println("<td>" + value + "</td>");
            }
            out.println("</tr>");
        }

        out.println("</table>");
        out.close();
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
