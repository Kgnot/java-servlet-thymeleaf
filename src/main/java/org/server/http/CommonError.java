package org.server.http;

import com.google.gson.Gson;
import org.server.http.status.HttpStatus;

import java.util.Map;

public record CommonError(
        HttpStatus status,
        String description
) {
    public CommonError {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }

    public static CommonError of(HttpStatus status, String description) {
        return new CommonError(status, description);
    }

    public String toJson() {
        Map<String, Object> json = Map.of(
                "code", status.getCode(),
                "error", status.getReason(),
                "description", description
        );
        return new Gson().toJson(json);
    }

}
