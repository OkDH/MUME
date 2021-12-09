/**
 * 
 */
package com.ocko.aventador;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ocko.aventador.scheduler.StockDataScheduler;

/**
 * @author ok
 *
 */
@SpringBootTest
public class StockHistoryTest {
	
	@Autowired private StockDataScheduler StockDataScheduler;
	
	@Test
	public void firstCollectHistory() throws IOException {
		StockDataScheduler.firstCollectStockHistory();
	}
	
	@Test
	public void lastDayHistory() throws IOException {
		StockDataScheduler.schedulerLastUpdate();
	}

}
