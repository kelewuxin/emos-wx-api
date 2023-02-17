package com.example.emos.wx.util;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: renke
 * @description: TODO
 * @date: 2023/2/5 14:31
 * @version: 1.0
 */

public class ResultMessage extends HashMap<String, Object> {

    public ResultMessage() {
        put("code", HttpStatus.SC_OK);
        put("msg", "success");
    }

    public static ResultMessage error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static ResultMessage error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static ResultMessage error(int code, String msg) {
        ResultMessage r = new ResultMessage();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static ResultMessage ok(String msg) {
        ResultMessage r = new ResultMessage();
        r.put("msg", msg);
        return r;
    }

    public static ResultMessage ok(Map<String, Object> map) {
        ResultMessage r = new ResultMessage();
        r.putAll(map);
        return r;
    }



    public static ResultMessage ok() {
        return new ResultMessage();
    }

    public ResultMessage put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}


