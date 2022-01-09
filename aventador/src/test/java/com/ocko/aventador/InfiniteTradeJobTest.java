/**
 * 
 */
package com.ocko.aventador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ocko.aventador.scheduler.InfiniteTradeScheduler;

/**
 * @author ok
 *
 */
@SpringBootTest
public class InfiniteTradeJobTest {
	
	@Autowired private InfiniteTradeScheduler infiniteTradeScheduler;
	
	@Test
	public void test() {
		infiniteTradeScheduler.updateHistory(490, 1006);
//		infiniteTradeScheduler.updateHistory(null);
	}

}
