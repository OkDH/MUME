/**
 * 
 */
package com.ocko.aventador.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ocko.aventador.model.StockDetail;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * @author ok
 *
 */
public class StockHelper {

	/**
	 * 종가(현재가), 전일 종가 조회
	 * @param symbol
	 * @return
	 */
	public static StockDetail getStock(String symbol) {
		StockDetail stockDetail = new StockDetail();
		try {
			Stock stock = YahooFinance.get(symbol);
			stockDetail.setSymbol(symbol);
			if(stock != null) {
				// 종가 또는 현재가
				stockDetail.setClose(stock.getQuote().getPrice());
				// 전일 종가
				stockDetail.setPrevClose(stock.getQuote().getPreviousClose());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stockDetail;
	}
	
	/**
	 * 여러 종목 종가(현재가), 전일 종가 조회 
	 * @param symbols
	 * @return
	 */
	public static Map<String, StockDetail> getStocks(String[] symbols){
		Map<String, StockDetail> result = new HashMap<String, StockDetail>();
		
		try {
			Map<String, Stock> stocks = YahooFinance.get(symbols);
			
			if(stocks != null) {
				for(String symbol : symbols) {
					Stock stock = stocks.get(symbol);
					
					if(stock != null) {
						StockDetail stockDetail = new StockDetail();
						stockDetail.setSymbol(symbol);
						// 종가 또는 현재가
						stockDetail.setClose(stock.getQuote().getPrice());
						// 전일 종가
						stockDetail.setPrevClose(stock.getQuote().getPreviousClose());
						
						result.put(symbol, stockDetail);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
