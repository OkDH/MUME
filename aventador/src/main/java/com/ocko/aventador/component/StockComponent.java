/**
 * 
 */
package com.ocko.aventador.component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.dao.model.aventador.ViewTodayStock;
import com.ocko.aventador.model.StockDetail;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * @author ok
 *
 */
@Component
public class StockComponent {

	/**
	 * 종가(현재가), 전일 종가 조회
	 * @param symbol
	 * @return
	 */
	public StockDetail getStock(String symbol) {
		
		try {
			Stock stock = YahooFinance.get(symbol);
			if(stock != null) {
				StockDetail stockDetail = processStockDetail(symbol, stock);
				return stockDetail;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 여러 종목 종가(현재가), 전일 종가 조회 
	 * @param symbols
	 * @return
	 */
	public Map<String, StockDetail> getStocks(String[] symbols){
		Map<String, StockDetail> result = new HashMap<String, StockDetail>();
		
		try {
			Map<String, Stock> stocks = YahooFinance.get(symbols);
			
			if(stocks != null) {
				for(String symbol : symbols) {
					Stock stock = stocks.get(symbol);
					
					if(stock != null) {
						if(symbol.startsWith("^")) { // 마켓지수는 symbol에 ^가 붙는데 ^로 시작하는 변수는 자바 스크립트에서 접근이 안돼서 변경해줌
							symbol = symbol.substring(1, symbol.length());
						}
						StockDetail stockDetail = processStockDetail(symbol, stock);
						result.put(symbol, stockDetail);
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * YahooFinance stock > StockDetail
	 * @param symbol
	 * @param stock
	 * @return
	 */
	private StockDetail processStockDetail(String symbol, Stock stock) {
		StockDetail stockDetail = new StockDetail();
		stockDetail.setSymbol(symbol);
		
		// 마지막 거래일
		LocalDateTime lastTradeDate = LocalDateTime.ofInstant(stock.getQuote().getLastTradeTime().toInstant(), ZoneId.systemDefault());
		stockDetail.setLastTradeTime(lastTradeDate);
		
		// 종가 또는 현재가
		stockDetail.setPriceClose(stock.getQuote().getPrice());
		// 시가
		stockDetail.setPriceOpen(stock.getQuote().getOpen());
		// 고가
		stockDetail.setPriceHigh(stock.getQuote().getDayHigh());
		// 저가
		stockDetail.setPriceLow(stock.getQuote().getDayLow());
		// 전일 종가
		stockDetail.setPrevClose(stock.getQuote().getPreviousClose());
		// 전일 대비 변화
		stockDetail.setChg(stock.getQuote().getChange());
		// 변화율
		stockDetail.setChgp(stock.getQuote().getChangeInPercent());
		// 거래량
		stockDetail.setVolume(stock.getQuote().getVolume());
		// 섹터
		for(EtfSymbol item : EtfSymbol.values()) {
			if(symbol.equals(item.name())) {
				stockDetail.setSector(EtfSymbol.valueOf(symbol).sector());
				stockDetail.setBaseRsi(EtfSymbol.valueOf(stock.getSymbol()).defaultRsi()); // TODO : baseRsi 개인화
				break;
			}
		}
		return stockDetail;
	}
	
	/**
	 * ViewTodayStock > StockDetail
	 * @param symbol
	 * @param stock
	 * @return
	 */
	public StockDetail processStockDetail(ViewTodayStock stock) {
		StockDetail stockDetail = new StockDetail();
		
		// 객체복사
		BeanUtils.copyProperties(stock, stockDetail);
		
		// 섹터, baseRsi
		for(EtfSymbol item : EtfSymbol.values()) {
			if(stock.getSymbol().equals(item.name())) {
				stockDetail.setSector(EtfSymbol.valueOf(stock.getSymbol()).sector());
				stockDetail.setBaseRsi(EtfSymbol.valueOf(stock.getSymbol()).defaultRsi()); // TODO : baseRsi 개인화
				break;
			}
		}
		return stockDetail;
	}
	
}
