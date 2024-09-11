package com.song.client.param;

public class ResponseUtill {
    public static Response createSuccessResponse() {
        return new Response();
    }
    public static Response createFailResponse(String code, String msg) {
        Response response = new Response();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
    public static Response createSuccessResponse(Object content) {
        Response response = new Response();
        response.setContent(content);
        return response;
    }
}
