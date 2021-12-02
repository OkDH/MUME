package com.ocko.aventador.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.model.ResponseError;

@Controller
public class ExceptionController {
	
	@ExceptionHandler(MyAccessDeniedException.class)
	public @ResponseBody ResponseError error(MyAccessDeniedException e){
		ResponseError error = new ResponseError();
		error.setMessage(e.getMessage());
	    return error;
	}
	
	@ExceptionHandler(MyArgumentException.class)
	public @ResponseBody ResponseError error(MyArgumentException e){
		ResponseError error = new ResponseError();
		error.setMessage(e.getMessage());
		return error;
	}

}
