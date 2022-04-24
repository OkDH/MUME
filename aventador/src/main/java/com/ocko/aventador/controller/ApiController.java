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
import com.ocko.aventador.service.ApiService;
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.FcmTokenService;

@Controller
public class ApiController {
	
	@Autowired private AuthenticationService authenticationService;
	@Autowired private ApiService apiService;

	/**
	 * 지니 프로그램 연동 api key 발급
	 * @return
	 */
	@RequestMapping(value = "/api/member/api-key", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getApiKey() {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		
		Map<String ,String > map = new HashMap<String, String>();
		map.put("apiKey", apiService.getApiKey(memberInfo.getMemberId()));
		return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
	}
	
	/**
	 * 지니 프로그램 연동 데이터 연동
	 * @return
	 */
	@RequestMapping(value = "/api/public/kskyj", method = RequestMethod.POST)
	public ResponseEntity<ResponseDto> updateKskyjData(@RequestBody Map<String, Object> params) {
		return new ResponseEntity<ResponseDto>(apiService.updateKskyjData(params), HttpStatus.OK);
	}
}
