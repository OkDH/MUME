/**
 * 
 */
package com.ocko.aventador.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.StockComponent;
import com.ocko.aventador.dao.model.aventador.StockHistory;
import com.ocko.aventador.dao.model.aventador.StockHistoryExample;
import com.ocko.aventador.dao.model.aventador.ViewTodayStock;
import com.ocko.aventador.dao.persistence.aventador.StockHistoryMapper;
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
	@Autowired private StockHistoryMapper stockHistoryMapper;
	
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
	 * 3X ETFs 정보 가져오기 (오늘 데이터)
	 * @return
	 */
	public Map<String, StockDetail> getTodayEtfStocks() {
		Map<String, StockDetail> result = new HashMap<String, StockDetail>();
		
		List<ViewTodayStock> etfs = viewTodayStockMapper.selectByExample(null);
		for(ViewTodayStock stock : etfs) {
			StockDetail stockDetail = stockComponent.processStockDetail(stock);
			if(stockDetail != null)
				result.put(stock.getSymbol(), stockDetail);
		}
		
		return result;
	}
	
	/**
	 * 3X ETFs 정보 가져오기 (History 데이터)
	 * @param localDate
	 * @return
	 */
	public Map<String, StockDetail> getEtfStocks(LocalDate localDate) {
		StockHistoryExample example = new StockHistoryExample();
		example.createCriteria().andStockDateEqualTo(localDate);
		List<StockHistory> etfs = stockHistoryMapper.selectByExample(example);
		
		Map<String, StockDetail> result = new HashMap<String, StockDetail>();
		
		for(StockHistory stock : etfs) {
			StockDetail stockDetail = stockComponent.processStockDetail(stock);
			if(stockDetail != null)
				result.put(stock.getSymbol(), stockDetail);
		}
		
		return result;
	}
	
}
