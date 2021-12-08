/**
 * 
 */
package com.ocko.aventador;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * @author ok
 *
 */
public class StockTest {

	@Test
	public void test() throws IOException {
		Stock stock = YahooFinance.get("BULZ");
		System.out.println("close : " + stock.getQuote().getPrice());
		System.out.println("prevClose : " + stock.getQuote().getPreviousClose());
		System.out.println("change : " + stock.getQuote().getChange());
		System.out.println("changePer : " + stock.getQuote().getChangeInPercent());
		LocalDateTime date = LocalDateTime.ofInstant(stock.getQuote().getLastTradeTime().toInstant(), ZoneId.systemDefault());
		System.out.println(date);
	}
	
	@Test
	public void test2() throws IOException {
		String[] symbols = {"BULZ"};
		Map<String, Stock> stocks = YahooFinance.get(symbols);
		for(String symbol : symbols) {
			Stock stock = stocks.get(symbol);
			
			System.out.println("close : " + stock.getQuote().getPrice());
			System.out.println("prevClose : " + stock.getQuote().getPreviousClose());
			System.out.println("change : " + stock.getQuote().getChange());
			System.out.println("changePer : " + stock.getQuote().getChangeInPercent());
			LocalDateTime date = LocalDateTime.ofInstant(stock.getQuote().getLastTradeTime().toInstant(), ZoneId.systemDefault());
			System.out.println(date);
		}
	}
	
	@Test
	public void getSingleHistory() throws IOException {
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -1);
		
		Stock stock = YahooFinance.get("BULZ");
		List<HistoricalQuote> googleHistQuotes = stock.getHistory(from, to, Interval.DAILY);
		System.out.println("history length : " + googleHistQuotes.size());
	}
	
}
