package com.ocko.aventador.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.service.AuthenticationService;

@Controller
public class PageController {

private final static Logger logger = LoggerFactory.getLogger(PageController.class);
	
	@Autowired private AuthenticationService authenticationService;
	
	@ModelAttribute(name = "memberEmail")
	public String modelMember() {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		return memberInfo.getMemberEmail();
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getRoot() {
		logger.info("redirect from:/ --> to:/public/#!/stock");
		return "redirect:/public/#!/stock";
	}
	
	@RequestMapping(value = "/{pathName:(?:public|private)}", method = RequestMethod.GET)
	public String getPathIncorrectly (@PathVariable String pathName) {
		if(pathName.equals("public")) {
			logger.info("redirect from:/{} --> to:/{}/#!/stock", pathName, pathName);
			return "redirect:/public/#!/stock";
		} else if(pathName.equals("private")) {
			logger.info("redirect from:/{} --> to:/{}/#!/infinity/dashboard", pathName, pathName);
			return "redirect:/private/#!/infinite/dashboard";
		}
		return "redirect:/" + pathName + "/";
	}
	
	@RequestMapping(value = "/public/**", method = RequestMethod.GET)
	public ModelAndView getPublic(HttpServletRequest servletRequest) {
		String subpath = ((String)servletRequest.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE )).substring("/public".length());
		if(subpath.equals("")
				|| subpath.endsWith("/")) {
			subpath += "index";
		} else {
			subpath = "/public" + subpath;
		}
		ModelAndView modelAndView = new ModelAndView(subpath);
		return modelAndView;
	}
	
	@RequestMapping(value = "/private/**", method = RequestMethod.GET)
	public ModelAndView getPrivate(HttpServletRequest servletRequest) {
		String subpath = ((String)servletRequest.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE )).substring("/private".length());
		if(subpath.equals("")
				|| subpath.endsWith("/")) {
			subpath += "index";
		} else {
			subpath = "/private" + subpath;
		}
		ModelAndView modelAndView = new ModelAndView(subpath);
		return modelAndView;
	}
	
	@RequestMapping(value = "/tpl/**", method = RequestMethod.GET)
	public ModelAndView getTpl(HttpServletRequest servletRequest) {
		String subpath = ((String)servletRequest.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE )).substring("/tpl".length());
		ModelAndView modelAndView = new ModelAndView("/tpl" + subpath);
		return modelAndView;
	}
}
