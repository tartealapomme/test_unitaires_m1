package com.example.dto;

public class ApiError {

    private final int status;
    private final String message;

    public ApiError(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ApiError of(int status, String message) {
        return new ApiError(status, message);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
