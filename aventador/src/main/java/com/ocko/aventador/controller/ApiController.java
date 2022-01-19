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
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.FcmTokenService;

@Controller
public class ApiController {
	
	@Autowired private AuthenticationService authenticationService;
	@Autowired private FcmTokenService fcmTokenService;

	/**
	 * fcm 토큰 저장 
	 * @return
	 */
	@RequestMapping(value = "/api/member/fcm-token", method = RequestMethod.POST)
	public ResponseEntity<Boolean> updateFcmTocken(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		fcmTokenService.updateFcmToken(memberInfo.getMemberId(), params);
		return new ResponseEntity<Boolean>(HttpStatus.OK);
	}
}
