package com.sesac.alltaxi.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private final int status;

    private final String message;

    private ApiResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ApiResponse<Void> ok() {
        return ok(null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(200, "OK");
    }

}