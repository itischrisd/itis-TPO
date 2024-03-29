package international;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

public class BundleInfo {

    private static String[] commandParameterNames;
    private static String[] commandParameterDescriptions;
    private static String[] statusMessage;
    private static String[] headers;
    private static String[] footers;
    private static String[] resultDescriptions;
    private static String charset;
    private static String submitMessage;

    public static void generateInfo(ResourceBundle resourceBundle) {

        synchronized (BundleInfo.class) {
            List<String> commandParameterNames = new ArrayList<>();
            List<String> commandParameterDescriptions = new ArrayList<>();
            Enumeration<String> keys = resourceBundle.getKeys();

            while (keys.hasMoreElements()) {
                String key = keys.nextElement();

                switch (key) {
                    case "header":
                        headers = resourceBundle.getStringArray(key);
                        break;
                    case "footer":
                        footers = resourceBundle.getStringArray(key);
                        break;
                    case "statusMessage":
                        statusMessage = resourceBundle.getStringArray(key);
                        break;
                    case "resultDescriptions":
                        resultDescriptions = resourceBundle.getStringArray(key);
                        break;
                    case "charset":
                        charset = resourceBundle.getString(key);
                        break;
                    case "submit":
                        submitMessage = resourceBundle.getString(key);
                        break;
                    default:
                        if (key.startsWith("param_")) {
                            commandParameterNames.add(key.substring(6));
                            commandParameterDescriptions.add(resourceBundle.getString(key));
                        }
                        break;
                }
            }

            BundleInfo.commandParameterNames = commandParameterNames.toArray(new String[0]);
            BundleInfo.commandParameterDescriptions = commandParameterDescriptions.toArray(new String[0]);
        }
    }

    public static String getCharset() {
        return charset;
    }

    public static String getSubmitMessage() {
        return submitMessage;
    }

    public static String[] getCommandParameterNames() {
        return commandParameterNames;
    }

    public static String[] getCommandParameterDescriptions() {
        return commandParameterDescriptions;
    }

    public static String[] getStatusMessage() {
        return statusMessage;
    }

    public static String[] getHeaders() {
        return headers;
    }

    public static String[] getFooters() {
        return footers;
    }

    public static String[] getResultDescriptions() {
        return resultDescriptions;
    }
}