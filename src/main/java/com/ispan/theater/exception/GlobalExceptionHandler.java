package com.ispan.theater.exception;

import java.util.Date;

import ecpay.payment.integration.exception.EcpayException;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ispan.theater.dto.ErrorDto;
import com.ispan.theater.util.DatetimeConverter;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
        System.out.println(e.getCause());
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()), HttpStatus.SC_INTERNAL_SERVER_ERROR, "INTERNAL", e.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorDto> handleJwtException(ExpiredJwtException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
        System.out.println(e.getCause());
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()), HttpStatus.SC_FORBIDDEN, "forbidden", e.getMessage()));
    }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorDto> handleOrderException(OrderException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
        System.out.println(e.getCause());
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(e.getErrorCode()).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()), e.getErrorCode(), "order_ticket_error", e.getErrorMessage()));
    }

    @ExceptionHandler(EcpayException.class)
    public ResponseEntity<ErrorDto> handleEcpayException(EcpayException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getStackTrace());
        System.out.println(e.getCause());
        System.out.println(e.getClass().getName());
        return ResponseEntity.status(HttpStatus.SC_METHOD_FAILURE).body(new ErrorDto(DatetimeConverter.createSqlDatetime(new Date()), HttpStatus.SC_METHOD_FAILURE, "method_failure", e.getNewExceptionMessage()));
    }

}
