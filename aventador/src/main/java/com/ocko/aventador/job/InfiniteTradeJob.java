/**
 * 
 */
package com.ocko.aventador.job;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ocko.aventador.component.InfiniteTradeComponent;
import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.constant.RegisteredType;
import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteHistoryExample;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.model.StockTradeInfo;
import com.ocko.aventador.service.StockService;

/**
 * @author ok
 * 매매 내역 자동 기록 Job
 */
@Component
public class InfiniteTradeJob {

	private static final Logger log = LoggerFactory.getLogger(InfiniteTradeJob.class);

	@Autowired private InfiniteTradeComponent tradeComponent;
	@Autowired private InfiniteHistoryMapper historyMapper;
	@Autowired private InfiniteStockMapper infiniteStockMapper;
	@Autowired private InfiniteHistoryMapper infiniteHistoryMapper;
	
	@Transactional
	public void updateHistory(InfiniteDetail infiniteDetail, StockDetail todayStock) {
		
		// 매매내역
		InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
		historyExample.createCriteria().andInfiniteIdEqualTo(infiniteDetail.getInfiniteId()).andIsDeletedEqualTo(false);
		historyExample.setOrderByClause("trade_date asc, trade_type asc");
		infiniteDetail.setHistoryList(infiniteHistoryMapper.selectByExample(historyExample));
		
		
		// 진행률 100% 이상은 원금 소진으로 변경
		if(infiniteDetail.getProgressPer().compareTo(new BigDecimal(100)) >= 0) {
			InfiniteStock infiniteStock = new InfiniteStock();
			infiniteStock.setInfiniteState(InfiniteState.OUT);
			
			InfiniteStockExample example = new InfiniteStockExample();
			example.createCriteria().andInfiniteIdEqualTo(infiniteDetail.getInfiniteId());
			infiniteStockMapper.updateByExampleSelective(infiniteStock, example);
			return;
		}
		
		// 매수 정보
		infiniteDetail.setBuyTradeInfoList(tradeComponent.getBuyInfo(infiniteDetail));
		
		// 매도 정보
		infiniteDetail.setSellTradeInfoList(tradeComponent.getSellInfo(infiniteDetail));
		
		// 매도 내역 저장
		updateSell(infiniteDetail, todayStock);
		
		// 매수 내역 저장
		updateBuy(infiniteDetail, todayStock);
	}
	
	/**
	 * 매도 내역 저장
	 * @param infiniteDetail
	 * @param todayStock
	 */
	private void updateSell(InfiniteDetail infiniteDetail, StockDetail todayStock) {
		if(infiniteDetail == null)
			return;
		if(todayStock == null)
			return;
		if(infiniteDetail.getSellTradeInfoList() == null)
			return;
		
		// 보유수량
		Integer holdingQuantity = infiniteDetail.getHoldingQuantity();
		
		if(holdingQuantity > 0) {
			// 종가
			BigDecimal priceClose = todayStock.getPriceClose().setScale(2, RoundingMode.HALF_UP);
			// 고가
			BigDecimal priceHigh = todayStock.getPriceHigh().setScale(2, RoundingMode.HALF_UP);
			
			for(StockTradeInfo info : infiniteDetail.getSellTradeInfoList()) {
				InfiniteHistory infiniteHistory = new InfiniteHistory();
				infiniteHistory.setInfiniteId(infiniteDetail.getInfiniteId());
				infiniteHistory.setTradeDate(LocalDate.now().minusDays(1));
				infiniteHistory.setTradeType(TradeType.SELL);
				infiniteHistory.setQuantity(info.getQuantity());
				infiniteHistory.setRegisteredType(RegisteredType.AUTO.name());
				infiniteHistory.setRegisteredDate(LocalDateTime.now());
				infiniteHistory.setIsDeleted(false);
				
				switch (info.getConcludeType()) {
				case LOC:
					// 주문가격보다 종가가 크다면 체결
					if(info.getPrice().compareTo(priceClose) <= 0) { 
						BigDecimal unitPrice = priceClose.setScale(2, BigDecimal.ROUND_HALF_UP); // 소수점 2자리에서 반올림

						infiniteHistory.setUnitPrice(unitPrice);
						
						historyMapper.insert(infiniteHistory);
						
						// 수량 감소
						holdingQuantity -= info.getQuantity();
					}
					break;
				case PENDING_ORDER:
					// 주문가격보다 고가가 크다면 체결
					if(info.getPrice().compareTo(priceHigh) <= 0) { 
						BigDecimal unitPrice = info.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP); // 소수점 2자리에서 반올림
						infiniteHistory.setUnitPrice(unitPrice);
						
						historyMapper.insert(infiniteHistory);
						
						// 수량 감소
						holdingQuantity -= info.getQuantity();
					}
					break;
				default:
					break;
				}
			}
		}
		
		// 보유 수량이 0이라면 모두 매도 되었으므로 진행상태 완료로 변경
		if (holdingQuantity == 0) {
			InfiniteStock infiniteStock = new InfiniteStock();
			infiniteStock.setInfiniteState(InfiniteState.DONE);
			infiniteStock.setDoneDate(LocalDate.now().minusDays(1));
			
			InfiniteStockExample example = new InfiniteStockExample();
			example.createCriteria().andInfiniteIdEqualTo(infiniteDetail.getInfiniteId());
			
			infiniteStockMapper.updateByExampleSelective(infiniteStock, example);
		}
	}
	
	/**
	 * 매수 내역 저장
	 * @param infiniteDetail
	 * @param todayStock
	 */
	private void updateBuy(InfiniteDetail infiniteDetail, StockDetail todayStock) {
		if(infiniteDetail == null)
			return;
		if(todayStock == null)
			return;
		if(infiniteDetail.getBuyTradeInfoList() == null)
			return;
		
		// 매입금액
		BigDecimal buyPrice = infiniteDetail.getBuyPrice();
		
		// 종가
		BigDecimal priceClose = todayStock.getPriceClose();
		
		for(StockTradeInfo info : infiniteDetail.getBuyTradeInfoList()) {
			InfiniteHistory infiniteHistory = new InfiniteHistory();
			infiniteHistory.setInfiniteId(infiniteDetail.getInfiniteId());
			infiniteHistory.setTradeDate(LocalDate.now().minusDays(1));
			infiniteHistory.setTradeType(TradeType.BUY);
			infiniteHistory.setQuantity(info.getQuantity());
			infiniteHistory.setRegisteredType(RegisteredType.AUTO.name());
			infiniteHistory.setRegisteredDate(LocalDateTime.now());
			infiniteHistory.setIsDeleted(false);
			
			switch (info.getConcludeType()) {
			case LOC:
				
				// 주문가격이 종가보다 크다면 체결
				if(info.getPrice().compareTo(priceClose) >= 0) { 
					// 단가
					BigDecimal unitPrice = priceClose.setScale(2, BigDecimal.ROUND_HALF_UP); // 소수점 2자리에서 반올림
					infiniteHistory.setUnitPrice(unitPrice);
					
					System.out.println("buy, " +  info.getQuantity());
					
					historyMapper.insert(infiniteHistory);
					
					// 단가 * 수량
					BigDecimal tradePrice = unitPrice.multiply(new BigDecimal(info.getQuantity())); 
					// 매입금액 추가
					buyPrice.add(tradePrice);
				}
				break;
			default:
				break;
			}
		}
		
		// 매입급액이 배정시드보다 클 경우 진행상태 원금소진으로 변경
		if(buyPrice.compareTo(infiniteDetail.getSeed()) >= 0) {
			InfiniteStock infiniteStock = new InfiniteStock();
			infiniteStock.setInfiniteState(InfiniteState.OUT);
			
			InfiniteStockExample example = new InfiniteStockExample();
			example.createCriteria().andInfiniteIdEqualTo(infiniteDetail.getInfiniteId());
			
			infiniteStockMapper.updateByExampleSelective(infiniteStock, example);
		}
		
	}
}
