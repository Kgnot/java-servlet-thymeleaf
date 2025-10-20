package org.server.config.http;

import com.google.gson.Gson;
import org.server.config.http.status.HttpStatus;

import java.util.Map;

public class ResponseCommon<T> {

    private final HttpStatus status;
    private String description;
    private final T data;

    private ResponseCommon(HttpStatus status, String description, T data) {
        this.status = status;
        this.description = description;
        this.data = data;
    }

    private ResponseCommon(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseCommon(T data) {
        this.status = HttpStatus.OK;
        this.data = data;
    }

    // Ok section
    public static <T> ResponseCommon<T> ok(HttpStatus status, String description, T data) {
        return new ResponseCommon<T>(status, description, data);
    }


    public static <T> ResponseCommon<T> ok(HttpStatus status, T data) {
        return new ResponseCommon<T>(status, data);
    }

    public static <T> ResponseCommon<T> ok(T data) {
        return new ResponseCommon<T>(data);
    }

    // Client-Error | warning section
    public static <T> ResponseCommon<T> warning(HttpStatus status, String description, T data) {
        return new ResponseCommon<T>(status, description, data);
    }


    public static <T> ResponseCommon<T> warning(HttpStatus status, T data) {
        return new ResponseCommon<T>(status, data);
    }

    public static <T> ResponseCommon<T> warning(T data) {
        return new ResponseCommon<T>(HttpStatus.BAD_REQUEST, data);
    }


    public String toJson() {
        Map<String, Object> map = new java.util.HashMap<>(Map.of(
                "code", status.getCode(),
                "reason", status.getReason(),
                "data", data
        ));
        if (description != null) {
            map.put("description", description);
        }


        return new Gson().toJson(map);
    }

}
