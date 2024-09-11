package com.song.netty.utill;

public class Response {
    private long id;
    private Object content;
    private String code;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getContext() {
        return content;
    }

    public void setContent(Object context) {
        this.content = content;
    }
}
