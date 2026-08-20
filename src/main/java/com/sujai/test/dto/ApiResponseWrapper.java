package com.sujai.test.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseWrapper<T> {
    private boolean success;
    private T data;
    private String error;
    private String message;

    public ApiResponseWrapper() {
    }

    public ApiResponseWrapper(boolean success, T data, String error, String message) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.message = message;
    }

    public static <T> ApiResponseWrapper<T> success(T data) {
        return new ApiResponseWrapper<>(true, data, null, null);
    }

    public static <T> ApiResponseWrapper<T> success(T data, String message) {
        return new ApiResponseWrapper<>(true, data, null, message);
    }

    public static <T> ApiResponseWrapper<T> error(String error) {
        return new ApiResponseWrapper<>(false, null, error, null);
    }

    public static <T> ApiResponseWrapper<T> error(String error, String message) {
        return new ApiResponseWrapper<>(false, null, error, message);
    }
}
