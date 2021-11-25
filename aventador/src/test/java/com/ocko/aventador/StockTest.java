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
		Stock stock = YahooFinance.get("TQQQ");
		System.out.println("close : " + stock.getQuote().getPrice());
		System.out.println("prevClose : " + stock.getQuote().getPreviousClose());
		System.out.println("change : " + stock.getQuote().getChange());
		System.out.println("changePer : " + stock.getQuote().getChangeInPercent());
		LocalDate date = LocalDateTime.ofInstant(stock.getQuote().getLastTradeTime().toInstant(), ZoneId.systemDefault()).toLocalDate();
		System.out.println(date);
		LocalDate date2 = LocalDateTime.ofInstant(stock.getHistory().get(stock.getHistory().size()-1).getDate().toInstant(), ZoneId.systemDefault()).toLocalDate();
		System.out.println(date2);
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
