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
//		infiniteTradeScheduler.updateHistory(1128, 2360);
		infiniteTradeScheduler.updateHistory(null);
	}

}
