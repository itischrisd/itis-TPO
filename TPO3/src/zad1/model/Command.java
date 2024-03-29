package model;

public interface Command {

    void init();
    void setParameter(String name, String value);
    String getParameter(String name);
    void execute();
    String[][] getResults();
    void setResults(String[][] results);
    void setStatusCode(int code);
    int getStatusCode();
    void clearResult();
}
