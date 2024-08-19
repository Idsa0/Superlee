package com.Superlee.HR.Backend.Service;

public class Response {
    public static final String emptyResponseString = "{\"errMsg\":null,\"value\":null}";
    public String errMsg;
    public Object value;

    public Response() {
        errMsg = null;
        value = null;
    }

    public Response(String msg) {
        errMsg = msg;
    }

    public Response(Object res) {
        value = res;
    }
}
