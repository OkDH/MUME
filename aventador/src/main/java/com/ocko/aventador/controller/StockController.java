/**
 * 
 */
package com.ocko.aventador.controller;

import com.ocko.aventador.dao.model.aventador.StockHistory;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ocko112
 *
 */
@RestController
public class StockController {

	@Autowired private StockService stockService;

	/**
	 * 시장지수 전체 조회
	 * @return
	 */
	@RequestMapping(value = "/api/stocks/market-index", method = RequestMethod.GET)
	public Map<String, StockDetail> getStock() {
		return stockService.getMarketIndex();
	}

	/**
	 * 무매 ETF 전체 조회
	 * @return
	 */
	@RequestMapping(value = "/api/stocks/etfs", method = RequestMethod.GET)
	public Map<String, StockDetail> getEtfs() {
		return stockService.getTodayEtfStocks();
	}

	@RequestMapping(value = "/api/stock/init", method = RequestMethod.GET)
	public Map<String, Object> init() {
		Map<String, Object> initData = new HashMap<String, Object>();
		initData.put("etfs", stockService.getTodayEtfStocks());
		return initData;
	}

	/**
	 * 특정 ETF History 조회
	 * @param params
	 * 			symbol
	 * 			startDate
	 * 			endDate
	 * @return List<StockHistory>
	 */
	@RequestMapping(value = "/api/stocks/etfs/history", method = RequestMethod.POST)
	public List<StockHistory> getEtfHistory(@RequestBody Map<String, Object> params) {
		return stockService.getStockHistoryBetweenDate(
			(String) params.get("symbol"),
			LocalDate.parse((String) params.get("startDate")),
			LocalDate.parse((String) params.get("endDate"))
		);
	}
}
