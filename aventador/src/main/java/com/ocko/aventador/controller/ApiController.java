package com.ocko.aventador.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.model.api.ResponseDto;
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.FcmTokenService;

@Controller
public class ApiController {
	
	@Autowired private AuthenticationService authenticationService;
	@Autowired private FcmTokenService fcmTokenService;

	/**
	 * 개인 식별 토큰 발급
	 * @return
	 */
	@RequestMapping(value = "/api/member/check-token", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getMemberTocken() {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		
		Map<String ,String > map = new HashMap<String, String>();
		map.put("token", fcmTokenService.getCheckToken(memberInfo.getMemberId()));
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
	
	/**
	 * 웹뷰에서 요청한 fcm 토큰 저장 
	 * @return
	 */
	@RequestMapping(value = "/api/public/member/fcm-token", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> updateFcmTocken(@RequestBody Map<String, String> params) {
		return new ResponseEntity<ResponseDto>(fcmTokenService.updateFcmToken(params), HttpStatus.OK);
	}
}
