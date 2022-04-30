package com.ocko.aventador.exception;

public class RefreshTokenExpiredException extends RuntimeException {

	@Override
    public String getMessage() {
        return "REFRESH_TOKEN 유효기간이 만료되었습니다.";
    }
}
