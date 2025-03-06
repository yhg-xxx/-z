
package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class Result extends HashMap<String, Object> {
    // 添加链式调用支持
    public Result put(String key, Object value) {
        super.put(key, value);
        return this; // 关键修改：返回当前对象
    }
    private static final long serialVersionUID = 1L;

    public static Result ok(Object data) {
        Result result = new Result();
        result.put("success", true);
        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.put("success", false);
        result.put("code", 500);
        result.put("message", message);
        return result;
    }
}