package com.codecharlan.samapp;

import com.amazonaws.services.apigateway.model.GatewayResponse;

public class GatewayResponse {

    private String body;
    private int statusCode;

    public GatewayResponse(String body, int statusCode) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public static GatewayResponse createGatewayResponse(List<Record> records){
        String body = new ObjectMapper().writeValueAsString(records);
        return new GatewayResponse(200, body)
    }
}
