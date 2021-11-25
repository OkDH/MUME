/**
 * 
 */
package com.ocko.aventador.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.StockComponent;
import com.ocko.aventador.dao.model.aventador.ViewTodayStock;
import com.ocko.aventador.dao.persistence.aventador.ViewTodayStockMapper;
import com.ocko.aventador.model.StockDetail;


/**
 * @author ok
 *
 */
@Service
public class StockService {
	
	@Autowired private StockComponent stockComponent;
	@Autowired private ViewTodayStockMapper viewTodayStockMapper;

	/**
	 * 하나의 심볼에 대해서 stock 정보 가져오기
	 * @param symbol
	 * @return
	 */
	public StockDetail getStock(String symbol) {
		return stockComponent.getStock(symbol);
	}
	
	/**
	 * 복수의 심볼에 대해서 stock 정보 가져오기
	 * @param symbols
	 * @return
	 */
	public Map<String, StockDetail> getStocks(String[] symbols) {
		return stockComponent.getStocks(symbols);
	}
	
	/**
	 * 3X ETFs 정보 가져오기
	 * @return
	 */
	public Map<String, StockDetail> getEtfStocks() {
		Map<String, StockDetail> result = new HashMap<String, StockDetail>();
		
		List<ViewTodayStock> etfs = viewTodayStockMapper.selectByExample(null);
		for(ViewTodayStock stock : etfs) {
			StockDetail stockDetail = stockComponent.processStockDetail(stock);
			if(stockDetail != null)
				result.put(stock.getSymbol(), stockDetail);
		}
		
		return result;
	}
	
}
