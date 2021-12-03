package com.ocko.aventador.service.infinite;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.constant.RregisteredType;
import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;

@Service
public class InfiniteStockService {

	@Autowired private InfiniteStockMapper infiniteStockMapper;
	@Autowired private InfiniteHistoryMapper infiniteHistoryMapper;
	
	/**
	 * 무매 종목리스트 조회
	 * @param params
	 * @return
	 */
	public List<InfiniteStock> getStocks(Map<String, Object> params){
		InfiniteStockExample example = new InfiniteStockExample();
		
		example.setOrderByClause("registered_date desc");
		
		return infiniteStockMapper.selectByExample(example);
	}
	
	/**
	 * 무매 종목 추가
	 * @param params
	 * @return
	 */
	@Transactional
	public boolean addStock(Map<String, Object> params) {
		
		// 종목 추가
		InfiniteStock stock = new InfiniteStock();
		stock.setAccountId(Integer.parseInt(params.get("accountId").toString()));
		stock.setSymbol(params.get("symbol").toString());
		stock.setStartedDate(LocalDate.parse(params.get("startedDate").toString()));
		stock.setSeed(new BigDecimal(params.get("seed").toString()));
		stock.setInfiniteState(InfiniteState.ING);
		stock.setRegisteredDate(LocalDateTime.now());
		stock.setInfiniteType(params.get("infiniteType").toString());
		stock.setIsDeleted(false);
		infiniteStockMapper.insert(stock);
		
		// 매매 내역 추가
		InfiniteHistory history = new InfiniteHistory();
		history.setInfiniteId(stock.getInfiniteId());
		history.setTradeDate(stock.getStartedDate());
		history.setTradeType(TradeType.BUY);
		
		BigDecimal unitPrice = new BigDecimal(params.get("unitPrice").toString());
		int quantity = Integer.parseInt(params.get("quantity").toString());
		history.setUnitPrice(unitPrice);
		history.setQuantity(quantity);
		
		// 수수료 TODO : 개인 설정 수수료
		BigDecimal fees = unitPrice.multiply(new BigDecimal(quantity)).multiply(new BigDecimal("0.0007"));
		history.setFees(fees);
		
		history.setRegisteredType(RregisteredType.MANUAL.name());
		history.setRegisteredDate(LocalDateTime.now());
		history.setIsDeleted(false);
		infiniteHistoryMapper.insert(history);
		return true;
	}
}
