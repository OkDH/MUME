package com.ocko.aventador.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.service.AuthenticationService;

/**
 * 
 * @author ocko112
 *
 */
@Controller
public class InfiniteController {
	
	@Autowired private AuthenticationService authenticationService;
	
	@RequestMapping(value = "/api/infinite/my-account", method = RequestMethod.GET)
	public @ResponseBody StockDetail getMyAccount(HttpServletRequest request) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		
		
		return null;
	}
}
