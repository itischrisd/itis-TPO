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

public class ParametersServlet extends HttpServlet {

    private ServletContext context;
    private String resourceEndpoint;

    public void init() {
        context = getServletContext();
        resourceEndpoint = context.getInitParameter("resourceEndpoint");
    }

    public void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = context.getRequestDispatcher(resourceEndpoint);
        requestDispatcher.include(request, response);
        String charset = BundleInfo.getCharset();
        String[] headers = BundleInfo.getHeaders();
        String[] parameterNames = BundleInfo.getCommandParameterNames();
        String[] parameterDescriptions = BundleInfo.getCommandParameterDescriptions();
        String submitMessage = BundleInfo.getSubmitMessage();
        String[] footers = BundleInfo.getFooters();

        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        out.println("<style>");
        out.println("body{font-family:Arial,sans-serif;margin:20px;background-color:#fffdfd}");
        out.println("h2{color:#333;text-align:center}");
        out.println("form{background-color:#fff;padding:20px;border-radius:8px;box-shadow:0 0 15px rgba(0,0,0,.1);margin:20px auto;width:80%;max-width:600px}");
        out.println("label{font-weight:700;display:block;margin-bottom:10px;color:#555}");
        out.println("input[type=text]{width:calc(100% - 22px);padding: 10px 15px;border:1px solid #ccc;border-radius:5px;display:block}");
        out.println("input[type=submit]{padding:10px 15px;background-color:#007bff;color:#fff;border:none;border-radius:5px;cursor:pointer;margin-top:10px}");
        out.println("input[type=submit]:hover{background-color:#0056b3}");
        out.println("hr{border:none;height:1px;background-color:#ccc;margin-top:20px}");
        out.println("</style>");

        out.println("<center><h2>");

        for (String header : headers) {
            out.println(header);
        }

        out.println("</center></h2><hr>");
        out.println("<form method=\"post\">");

        for (int i = 0; i < parameterNames.length; i++) {
            out.println("<label for=\"" + parameterNames[i] + "\">" + parameterDescriptions[i] + "</label>");
            out.print("<input type=\"text\" size=\"30\" name=\"" + parameterNames[i] + "\"");
            String parameterValue = (String) session.getAttribute("param_" + parameterNames[i]);

            if (parameterValue != null) {
                out.print(" value=\"" + parameterValue + "\"");
            }

            out.println(">");
        }

        out.println("<input type=\"submit\" value=\"" + submitMessage + "\">");
        out.println("</form>");

        for (String parameterName : parameterNames) {
            String parameterValue = request.getParameter(parameterName);

            if (parameterValue == null) {
                return;
            }

            session.setAttribute("param_" + parameterName, parameterValue);
        }

        for (String footer : footers) {
            out.println(footer);
        }
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
