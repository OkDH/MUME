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
	 * 시장지수 조회
	 * @return
	 */
	public Map<String, StockDetail> getMarketIndex(){
		return stockComponent.getMarketIndex();
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
	 * 3X ETFs 정보 가져오기 (전거래일 데이터)
	 * @param todayDate 오늘 날짜
	 * @return
	 */
	public Map<String, StockDetail> getBeforeEtfStocks(LocalDate todayDate) {
		
		// 전거래일 날짜 가져오기
		LocalDate beforeDate = getBeforeTradeDate(todayDate);
		
		StockHistoryExample example = new StockHistoryExample();
		example.createCriteria().andStockDateEqualTo(beforeDate);
		List<StockHistory> etfs = stockHistoryMapper.selectByExample(example);
		
		Map<String, StockDetail> result = new HashMap<String, StockDetail>();
		
		for(StockHistory stock : etfs) {
			StockDetail stockDetail = stockComponent.processStockDetail(stock);
			if(stockDetail != null)
				result.put(stock.getSymbol(), stockDetail);
		}
		
		return result;
	}
	
	/**
	 * 전거래일 날짜 가져오기
	 * @param todayDate 오늘날짜
	 * @return
	 */
	private LocalDate getBeforeTradeDate(LocalDate todayDate) {
		StockHistoryExample example = new StockHistoryExample();
		example.createCriteria().andStockDateNotEqualTo(todayDate);
		example.setOrderByClause("stock_date desc");
		example.setLimit(1);
		List<StockHistory> etfs = stockHistoryMapper.selectByExample(example);
		return etfs.get(0).getStockDate();
	}
	
}
