package com.ocko.aventador.exception;

public class MyAccessDeniedException extends RuntimeException {

	@Override
    public String getMessage() {
        return "접근 권한이 없습니다.";
    }
}
