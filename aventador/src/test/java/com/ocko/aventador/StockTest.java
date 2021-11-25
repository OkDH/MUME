/**
 * 
 */
package com.ocko.aventador;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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
	public void getSingleStock() throws IOException {
		Stock stock = YahooFinance.get("^IXIC");
		System.out.println("close : " + stock.getQuote().getPrice());
		System.out.println("prevClose : " + stock.getQuote().getPreviousClose());
		System.out.println("change : " + stock.getQuote().getChange());
		System.out.println("changePer : " + stock.getQuote().getChangeInPercent());
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
