/**
 * 
 */
package com.ocko.aventador.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ocko.aventador.dao.model.aventador.ViewTodayStock;
import com.ocko.aventador.dao.persistence.aventador.ViewTodayStockMapper;
import com.ocko.aventador.job.InfiniteTradeJob;

/**
 * @author ok
 *
 */
@Component
public class InfiniteTradeScheduler {
	
	@Autowired private InfiniteTradeJob infiniteJob;
	@Autowired private ViewTodayStockMapper viewTodayStockMapper;
	
	/**
	 * 무한매수 매매내역 업데이트
	 * 화~토 오전 7시 5분에 작동
	 */
	@Scheduled(cron="0 5 7 * * 2-6")
	public void updateInfiniteHistory() {
		boolean isOpen = false;
		
		// 전일 데이터 날짜로 전날 미장 오픈 했는지 확인
		List<ViewTodayStock> list = viewTodayStockMapper.selectByExample(null);
		for(ViewTodayStock stock : list){
			if (stock.getStockDate().isEqual(LocalDate.now().minusDays(1))) {
				isOpen = true;
				break;
			}
		}
		if(isOpen)
			infiniteJob.updateHistory();
	}

}
