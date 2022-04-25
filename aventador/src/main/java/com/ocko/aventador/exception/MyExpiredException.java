package com.ocko.aventador.exception;

public class MyExpiredException extends RuntimeException {

	@Override
    public String getMessage() {
        return "토큰 유효기간이 만료되었습니다.";
    }
}
