package com.ocko.aventador.exception;

public class MyArgumentException extends RuntimeException {
	
	@Override
    public String getMessage() {
        return "유효하지 않은 파라미터입니다.";
    }
}
