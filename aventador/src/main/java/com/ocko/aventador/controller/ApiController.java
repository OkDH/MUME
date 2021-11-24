/**
 * 
 */
package com.ocko.aventador.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.service.StockService;

/**
 * @author ok
 *
 */
@Controller
@RequestMapping(value="/api")
public class ApiController {

	@Autowired private StockService stockService;
	
	@RequestMapping(value = "/stock/{symbol}", method = RequestMethod.GET)
	public @ResponseBody StockDetail getStock(@PathVariable String symbol) {
		return stockService.getStock(symbol);
	}
	
	@RequestMapping(value = "/stocks/{symbols}", method = RequestMethod.GET)
	public @ResponseBody Map<String, StockDetail> getStocks(@PathVariable String[] symbols) {
		return stockService.getStocks(symbols);
	}
}
