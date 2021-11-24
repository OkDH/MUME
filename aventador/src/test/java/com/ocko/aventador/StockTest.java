/**
 * 
 */
package com.ocko.aventador;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * @author ok
 *
 */
@SpringBootTest
public class StockTest {

	@Test
	public void getSingleStock() throws IOException {
		Stock stock = YahooFinance.get("^IXIC");
		stock.print();
	}
	
	@Test
	public void getSingleHistory() throws IOException {
		Stock stock = YahooFinance.get("^IXIC", true);
		System.out.println("history length : " + stock.getHistory().size());
	}
}
