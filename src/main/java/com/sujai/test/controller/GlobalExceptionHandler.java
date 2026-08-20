package com.sujai.test.controller;

import com.sujai.test.dto.ApiResponseWrapper;
import com.sujai.test.service.DeviceService;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeviceService.DeviceNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleDeviceNotFound(DeviceService.DeviceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(DeviceService.DeviceUpdateException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleDeviceUpdate(DeviceService.DeviceUpdateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(DeviceService.DeviceDeleteException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleDeviceDelete(DeviceService.DeviceDeleteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponseWrapper.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.error(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseWrapper.error(ex.getMessage()));
    }
}
