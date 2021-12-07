/**
 * 
 */
package com.ocko.aventador.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ocko.aventador.job.InfiniteTradeJob;

/**
 * @author ok
 *
 */
@Component
public class InfiniteTradeScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(InfiniteTradeScheduler.class);
	
	@Autowired InfiniteTradeJob infiniteJob;
	
	public void updateInfiniteHistorys() {

	}

}
