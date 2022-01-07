/**
 * 
 */
package com.ocko.aventador;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.ocko.aventador.job.InfiniteTradeJob;

/**
 * @author ok
 *
 */
@SpringBootTest
public class InfiniteTradeJobTest {
	
	@Autowired private InfiniteTradeJob infiniteTradeJob;
	
	@Test
	public void test() {
		infiniteTradeJob.updateHistory();
	}

}
