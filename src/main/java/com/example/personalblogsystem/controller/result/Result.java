package com.example.personalblogsystem.controller.result;

import lombok.Data;

@Data
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setCode(200);
        result.setData(data);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode(500);
        return result;
    }

    public static Result fail(String failMsg) {
        Result result = new Result();
        result.setCode(500);
        result.setMsg(failMsg);
        return result;
    }

}
