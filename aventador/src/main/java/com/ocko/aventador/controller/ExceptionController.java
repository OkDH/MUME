package com.ocko.aventador.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.exception.MyExpiredException;
import com.ocko.aventador.model.ResponseError;

@ControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler(MyAccessDeniedException.class)
	public @ResponseBody ResponseError error(MyAccessDeniedException e){
		ResponseError error = new ResponseError();
		error.setStatus(415);
		error.setMessage(e.getMessage());
	    return error;
	}
	
	@ExceptionHandler(MyExpiredException.class)
	public @ResponseBody ResponseError error(MyExpiredException e){
		ResponseError error = new ResponseError();
		error.setStatus(416);
		error.setMessage(e.getMessage());
		return error;
	}
	
	@ExceptionHandler(MyArgumentException.class)
	public @ResponseBody ResponseError error(MyArgumentException e){
		ResponseError error = new ResponseError();
		error.setStatus(215);
		error.setMessage(e.getMessage());
		return error;
	}

}
