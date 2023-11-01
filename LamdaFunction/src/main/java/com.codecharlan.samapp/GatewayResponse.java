package com.codecharlan.samapp;

import com.amazonaws.services.apigateway.model.GatewayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

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
    public static GatewayResponse createGatewayResponse(List<Record> records) throws JsonProcessingException {
        String body = new ObjectMapper().writeValueAsString(records);
        return new GatewayResponse(body, 200);
    }
}
