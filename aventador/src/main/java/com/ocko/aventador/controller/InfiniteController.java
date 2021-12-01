package com.ocko.aventador.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.model.StockDetail;

/**
 * 
 * @author ocko112
 *
 */
@Controller
public class InfiniteController {

	@RequestMapping(value = "/api/infinite/my-account", method = RequestMethod.GET)
	public @ResponseBody StockDetail getMyAccount(HttpServletRequest request) {
//		return stockService.getStock(symbol);
		return null;
	}
}
