/**
 * 
 */
package com.ocko.aventador.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.model.aventador.ViewTodayStock;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewTodayStockMapper;
import com.ocko.aventador.job.InfiniteTradeJob;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.service.StockService;

/**
 * @author ok
 *
 */
@Component
@Profile("master")
public class InfiniteTradeScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(InfiniteTradeScheduler.class);

	@Autowired private InfiniteTradeJob infiniteJob;
	@Autowired private ViewTodayStockMapper viewTodayStockMapper;
	@Autowired private ViewInfiniteListMapper viewInfiniteListMapper;
	@Autowired private StockService stockService;
	
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
			updateHistory();
	}
	
	public void updateHistory() {
		updateHistory(null, null);
	}
	
	public void updateHistory(Integer memberId, Integer InfiniteId) {
		log.info("Start Update Infinite History");
		
		// etf 주가 정보 조회
		Map<String, StockDetail> todayStockMap = stockService.getTodayEtfStocks();
		
		// 어제자 주가 정보 조회
		Map<String, StockDetail> yesterdayStockMap = stockService.getBeforeEtfStocks(LocalDate.now().minusDays(1));
		
		// 진행 중인 무매 리스트 조회
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		Criteria criteria = example.createCriteria().andInfiniteStateEqualTo(InfiniteState.ING).andIsAutoTradeEqualTo(true);
		example.or().andInfiniteStateEqualTo(InfiniteState.OUT).andIsAutoTradeEqualTo(true);
		
		if(memberId != null)
			criteria.andMemberIdEqualTo(memberId);
		if(InfiniteId != null)
			criteria.andInfiniteIdEqualTo(InfiniteId);
		
		int offset = 0;
		int limit = 100;
		
		long total = viewInfiniteListMapper.countByExample(example);
		
		log.info("Infinite Total : " + total);
		
		example.setLimit(limit);
		
		while(offset <= total) {
			example.setOffset(offset);
			List<ViewInfiniteList> infiniteList = viewInfiniteListMapper.selectByExample(example);

			for(ViewInfiniteList viewInfinite : infiniteList) {
				
				log.info("updateHistory infiniteId : " + viewInfinite.getInfiniteId());
				
				InfiniteDetail infiniteDetail = new InfiniteDetail();
				
				// 객체복사
				BeanUtils.copyProperties(viewInfinite, infiniteDetail);
				
				// etf 어제 날짜 주가 정보 추가
				infiniteDetail.setStockDetail(yesterdayStockMap.get(viewInfinite.getSymbol()));
				
				// 보유수량이 0보다 작다면
				if(viewInfinite.getHoldingQuantity() <= 0)
					continue;
				
				infiniteJob.updateHistory(infiniteDetail, todayStockMap.get(viewInfinite.getSymbol()));
			}
			
			offset += 100;
		}
		
		
		log.info("End Update Infinite History");
	}
	
}
