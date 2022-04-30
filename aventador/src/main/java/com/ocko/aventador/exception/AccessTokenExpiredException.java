package com.ocko.aventador.exception;

public class AccessTokenExpiredException extends RuntimeException {

	@Override
    public String getMessage() {
        return "ACCESS_TOKEN 유효기간이 만료되었습니다.";
    }
}
