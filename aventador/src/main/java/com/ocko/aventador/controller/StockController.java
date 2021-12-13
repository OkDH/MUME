/**
 * 
 */
package com.ocko.aventador.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.constant.InfiniteType;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.service.StockService;

/**
 * @author ocko112
 *
 */
@Controller
public class StockController {

	@Autowired private StockService stockService;
	
	@RequestMapping(value = "/api/stock/{symbol}", method = RequestMethod.GET)
	public @ResponseBody StockDetail getStock(@PathVariable String symbol) {
		return stockService.getStock(symbol);
	}
	
	@RequestMapping(value = "/api/stocks/{symbols}", method = RequestMethod.GET)
	public @ResponseBody Map<String, StockDetail> getStocks(@PathVariable String[] symbols) {
		return stockService.getStocks(symbols);
	}
	
	@RequestMapping(value = "/api/stocks/etfs", method = RequestMethod.GET)
	public @ResponseBody Map<String, StockDetail> getEtfs() {
		return stockService.getTodayEtfStocks();
	}
	
	@RequestMapping(value = "/api/stock/init", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> init() {
		Map<String, Object> initData = new HashMap<String, Object>();
		initData.put("symbols", EtfSymbol.values());
		return initData;
	}
}
