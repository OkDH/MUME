/**
 * 
 */
package com.ocko.aventador;

import java.io.IOException;
import java.time.LocalDate;

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
	
	@Autowired private StockDataScheduler stockDataScheduler;
	
	@Test
	public void updateStockHistory() throws IOException {
		stockDataScheduler.schedulerUpdateAfter();
	}
	

}
