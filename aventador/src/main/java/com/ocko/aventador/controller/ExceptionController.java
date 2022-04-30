package com.ocko.aventador.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ocko.aventador.exception.AccessTokenExpiredException;
import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.exception.RefreshTokenExpiredException;
import com.ocko.aventador.model.ResponseError;

@ControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler(MyAccessDeniedException.class)
	public ResponseEntity<ResponseError> error(MyAccessDeniedException e){
		ResponseError error = new ResponseError();
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(e.getMessage());
		return new ResponseEntity<ResponseError>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(AccessTokenExpiredException.class)
	public ResponseEntity<ResponseError> error(AccessTokenExpiredException e){
		ResponseError error = new ResponseError();
		error.setStatus(HttpStatus.UNAUTHORIZED.value());
		error.setMessage(e.getMessage());
		return new ResponseEntity<ResponseError>(error, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(RefreshTokenExpiredException.class)
	public ResponseEntity<ResponseError> error(RefreshTokenExpiredException e){
		ResponseError error = new ResponseError();
		error.setStatus(HttpStatus.FORBIDDEN.value());
		error.setMessage(e.getMessage());
		return new ResponseEntity<ResponseError>(error, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(MyArgumentException.class)
	public ResponseEntity<ResponseError> error(MyArgumentException e){
		ResponseError error = new ResponseError();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(e.getMessage());
		return new ResponseEntity<ResponseError>(error, HttpStatus.BAD_REQUEST);
	}

}
