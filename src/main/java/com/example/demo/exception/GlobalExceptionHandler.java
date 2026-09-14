package com.example.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// bat loi khong tim thay Student
    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFound(
            StudentNotFoundException ex) {

	        ErrorResponse er = new ErrorResponse(
	                HttpStatus.NOT_FOUND.value(),
	                ex.getMessage(),
	                LocalDateTime.now()
	        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(er);
    }
    
    // bat loi validation request khong hop le
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation (MethodArgumentNotValidException ex){
    	
    	//tao message
    	String message = ex.getBindingResult()
    					.getFieldErrors()
    					.stream()
    					.map(error -> {
    						String field = error.getField();
    						String errorMessage = error.getDefaultMessage();
    								
    						return field + ":" + errorMessage;
    						
    					})
    					.toList()
    					.toString();
    	
    	
    	ErrorResponse er = new ErrorResponse (
    			HttpStatus.BAD_REQUEST.value(),
    			message,
    			LocalDateTime.now()
    			);
    	return ResponseEntity
    			.status(HttpStatus.BAD_REQUEST)
    			.body(er);
    	
    }
    
}