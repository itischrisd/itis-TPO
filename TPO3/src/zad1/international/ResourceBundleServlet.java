package international;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleServlet extends HttpServlet {

    private String resourceBundleClassName;

    public void init() {
        resourceBundleClassName = getServletContext().getInitParameter("resourceBundleClassName");
    }

    public void serviceRequest(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ResourceBundle resourceBundle = (ResourceBundle) session.getAttribute("resourceBundle");

        if (resourceBundle == null) {
            Locale locale = request.getLocale();
            resourceBundle = ResourceBundle.getBundle(resourceBundleClassName, locale);
            session.setAttribute("resourceBundle", resourceBundle);
            BundleInfo.generateInfo(resourceBundle);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        serviceRequest(request);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        serviceRequest(request);
    }
}
