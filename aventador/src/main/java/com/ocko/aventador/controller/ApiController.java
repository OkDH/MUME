package com.ocko.aventador.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.FcmTokenService;

@Controller
public class ApiController {
	
	@Autowired private AuthenticationService authenticationService;
	@Autowired private FcmTokenService fcmTokenService;

	@RequestMapping(value = "/api/member/token", method = RequestMethod.GET)
	public @ResponseBody String getMemberTocken() {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		return fcmTokenService.getMyToken(memberInfo.getMemberId());
	}
	
}
