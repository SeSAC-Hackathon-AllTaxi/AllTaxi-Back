package com.sesac.alltaxi.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final T requestId;
    private final int status;
    private final String message;

    private ApiResponse(T requestId, int status, String message) {
        this.status = status;
        this.message = message;
        this.requestId = requestId;
    }

    public static ApiResponse<Void> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> ok(T requestId) {
        return new ApiResponse<>(requestId, 200, "OK");
    }

    public static <T> ApiResponse<T> created(T requestId) {
        return new ApiResponse<>(requestId, 201, "Created");
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(null, status, message);
    }
}
