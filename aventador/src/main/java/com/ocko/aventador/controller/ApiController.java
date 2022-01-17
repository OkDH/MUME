package com.ocko.aventador.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApiController {

	@RequestMapping(value = "/api/member/token", method = RequestMethod.GET)
	public @ResponseBody String getMemberTocken() {
		return null;
	}
	
}
