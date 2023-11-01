package com.codecharlan.samapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class App implements RequestHandler<Request, Response> {
    Connection conn;
    public Object handleRequest(final Request request, final Context context) {
        String operation = request.getOperation();
        connectToDB();
        switch (operation) {
            case CREATE:
                PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO my_table (name, age) VALUES (?, ?");
                insertStatement.setString(1, request.getName());
                insertStatement.setInt(2, request.getAge());
                insertStatement.executeUpdate();
                break;

            case READ:
                PreparedStatement insertStatement = conn.prepareStatement("SELECT * FROM my_table");
                ResultSet rs = selectStatement.executeQuery();
                Response response = new Response();
                while (rs.next()) {
                    response.addRecord(rs.getInt("id"), rs.getString("name"));
                }
                return response;

            case UPDATE:
                PreparedStatement updateStatement = conn.prepareStatement("UPDATE my_table SET name = ? WHERE id = ?");
                updateStatement.setString(1, request.getName());
                updateStatement.setInt(2, request.getAge());
                updateStatement.executeUpdate();
                break;

            case DELETE:
                PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM my_table WHERE id = ?");
                deleteStatement.setInt(1, request.getId());
                deleteStatement.executeUpdate();
                break;
            default:
                throw new RuntimeException("Invalid operation" + operation);
        }
        conn.close();
        return new Response();
    }

    private void connectToDB() throws IOException {
        String rdsHost = System.getenv("RDS_HOST");
        int rdsPort = Integer.parseInt(System.getenv("RDS_PORT"));
        String rdsUsername = System.getenv("RDS_USERNAME");
        String rdsPassword = System.getenv("RDS_PASSWORD");

        conn = DriverManager.getConnection("jdbc:mysql://" + rdsHost + ":" + rdsPort + "/my-rds-db", rdsUsername, rdsPassword);
    }
}
