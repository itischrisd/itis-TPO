package model;

import java.util.*;

public class CommandImpl implements Command {

    private final Map<String, String> parameters = new HashMap<>();
    private String[][] results = new String[0][0];
    private int statusCode;

    @Override
    public void init() {
    }

    @Override
    public void setParameter(String name, String value) {
        parameters.put(name, value);
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public void execute() {
    }

    @Override
    public String[][] getResults() {
        return Arrays.copyOf(results, results.length);
    }

    @Override
    public void setResults(String[][] results) {
        this.results = results;
    }

    @Override
    public void setStatusCode(int code) {
        statusCode = code;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public void clearResult() {
        results = new String[0][0];
    }
}
