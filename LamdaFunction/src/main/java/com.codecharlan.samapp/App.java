package com.codecharlan.samapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;


import java.util.ArrayList;
import java.util.List;

public class App implements RequestHandler<Request, GatewayResponse> {
    Connection conn;

    private static final String CREATE = "CREATE";
    private static final String READ = "READ";
    private static final String UPDATE = "UPDATE";
    private static final String DELETE = "DELETE";
    private Record record;

    @Override
    public GatewayResponse handleRequest(final Request request, final Context context) {
        String operation = request.getOperation();
        connectToDB();
        switch (operation) {
            case CREATE:
                try {
                    PreparedStatement createStatement = conn.prepareStatement("INSERT INTO my_table (name, age) VALUES (?, ?)");
                    createStatement.setString(1, request.getName());
                    createStatement.setInt(2, request.getAge());
                    createStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
                break;

            case READ:
                try {
                    PreparedStatement readStatement = conn.prepareStatement("SELECT * FROM my_table");
                    ResultSet rs = readStatement.executeQuery();
                    List<Record> records = new ArrayList<>();
                    while (rs.next()) {
                        record = new Record(rs.getInt("id"), rs.getString("name"));
                        records.add(record);
                    }
                    return GatewayResponse.createGatewayResponse(records);
                } catch (SQLException | JsonProcessingException ignored) {
                }
                break;

            case UPDATE:
                try {
                    PreparedStatement updateStatement = conn.prepareStatement("UPDATE my_table SET name = ? WHERE id = ?");
                    updateStatement.setString(1, record.getName());
                    updateStatement.setInt(2, record.getId());
                    updateStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
                break;

            case DELETE:
                try {
                    PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM my_table WHERE id = ?");
                    deleteStatement.setInt(1, record.getId());
                    deleteStatement.executeUpdate();
                } catch (SQLException ignored) {
                }
                break;

            default:
                throw new RuntimeException("Invalid operation: " + operation);
        }

        try {
            conn.close();
        } catch (SQLException ignored) {
        }

        return new GatewayResponse("Success", 200);
    }

    private void connectToDB() {
        String rdsHost = System.getenv("RDS_HOST");
        int rdsPort = Integer.parseInt(System.getenv("RDS_PORT"));
        String rdsUsername = System.getenv("RDS_USERNAME");
        String rdsPassword = System.getenv("RDS_PASSWORD");

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + rdsHost + ":" + rdsPort + "/my-rds-db", rdsUsername, rdsPassword);
        } catch (SQLException ignored) {
        }
    }
}
