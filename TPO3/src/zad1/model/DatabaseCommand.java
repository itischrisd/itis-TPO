package model;

import exception.DatabaseAccessException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCommand extends CommandImpl {

    private DataSource dataSource;

    public void init() {
        try {
            Context initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            String dbName = getParameter("databaseName");
            dataSource = (DataSource) context.lookup(dbName);
            setParameter("clause", null);
        } catch (NamingException e) {
            setStatusCode(1);
        }
    }

    public void execute() {
        clearResult();
        setStatusCode(0);
        Connection connection = null;
        List<String[]> resultList = new ArrayList<>();
        try {
            synchronized (this) {
                connection = dataSource.getConnection();
            }

            Statement statement = connection.createStatement();
            String command = "SELECT title, author, year, isbn FROM Book";
            String clause = getParameter("clause");

            if (clause != null) {
                command += " WHERE title LIKE '%" + clause + "%'";
            }

            ResultSet resultSet = statement.executeQuery(command);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                resultList.add(row);
            }

            resultSet.close();
            setResults(resultList.toArray(new String[0][0]));

            if (resultList.isEmpty()) {
                setStatusCode(3);
            }

        } catch (SQLException e) {
            setStatusCode(2);
            throw new DatabaseAccessException("Błąd w dostępie do bazy lub w SQL", e);
        } finally {
            try {
                connection.close();
            } catch (NullPointerException | SQLException ignored) {
            }
        }
    }

}
