package view;

import international.BundleInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;

public class ErrorHandler extends HttpServlet {

    public void serviceRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String charset = BundleInfo.getCharset();
        response.setContentType("text/html; charset=" + charset);
        Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");

        PrintWriter out = response.getWriter();

        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; background-color: #fff; color: #333; }");
        out.println("h2 { color: #D8000C; }");
        out.println("hr { border: 0; height: 1px; background-color: #ccc; }");
        out.println("p { margin: 20px 0; }");
        out.println(".error-details { background-color: #FEEFB3; padding: 10px 20px; border: 1px solid #D8000C; border-radius: 5px; }");
        out.println("</style>");

        if (exc != null) {
            out.println("<h2>Error: " + exc.getMessage() + "</h2><hr>");
            Throwable cause = exc.getCause();

            if (cause instanceof SQLException) {
                SQLException e = (SQLException) cause;
                out.println("<div class='error-details'>");
                out.println("Detailed Message: " + e.getMessage() + "<br><br>");
                out.println("Error code: " + e.getErrorCode() + "<br>");
                out.println("SQL state: " + e.getSQLState() + "<br>");
                out.println("</div>");
            } else {
                Exception e = (Exception) cause;
                out.println("<div class='error-details'>");
                out.println("Detailed Message: " + e.getMessage() + "<br><br>");
                out.println("Stack Trace: " + Arrays.toString(e.getStackTrace()) + "<br>");
                out.println("</div>");
            }
        } else {
            out.println("<h2>No detailed error information available.</h2>");
        }

        out.close();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        serviceRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        serviceRequest(request, response);
    }
}
