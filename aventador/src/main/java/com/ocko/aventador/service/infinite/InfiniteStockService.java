package com.ocko.aventador.service.infinite;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ocko.aventador.component.InfiniteTradeComponent;
import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.constant.InfiniteType;
import com.ocko.aventador.constant.RregisteredType;
import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.service.StockService;

@Service
public class InfiniteStockService {

	@Autowired private StockService stockService;
	@Autowired private InfiniteTradeComponent tradeComponent;
	@Autowired private InfiniteStockMapper infiniteStockMapper;
	@Autowired private InfiniteHistoryMapper infiniteHistoryMapper;
	@Autowired private ViewInfiniteListMapper viewInfiniteListMapper;
	
	/**
	 * 무매 종목리스트 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<InfiniteDetail> getStocks(int memberId, Map<String, Object> params){
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		example.setOrderByClause("registered_date desc");
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null)
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		if(params.get("infiniteState") != null)
			criteria.andInfiniteStateEqualTo(params.get("infiniteState").toString());
		
		// view 조회
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);
		
		// etf 주가 정보 조회
		Map<String, StockDetail> stockMap = stockService.getEtfStocks();
		
		// 손익정보 등 값 추가
		List<InfiniteDetail> infiniteStockList = new ArrayList<InfiniteDetail>();
		for(ViewInfiniteList viewInfinite : list) {
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			
			// etf 주가 정보 추가
			infiniteDetail.setStockDetail(stockMap.get(viewInfinite.getSymbol()));
			
			// 매수 정보
			infiniteDetail.setBuyTradeInfoList(tradeComponent.getBuyInfo(infiniteDetail));
			
			// 매도 정보
			infiniteDetail.setSellTradeInfoList(tradeComponent.getSellInfo(infiniteDetail));
			
			infiniteStockList.add(infiniteDetail);
		}
		
		return infiniteStockList;
	}
	
	/**
	 * 내 계좌 통계 정보
	 * @param memberId
	 * @param params accountId가 null일 경우 전체 전체 계좌로 통계 
	 * @return
	 */
	public Map<String, Object> getMyAccountState(int memberId, Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("memberId", memberId);
		query.put("infiniteState", InfiniteState.ING);
		if(params.get("accountId") != null)
			query.put("accountId", params.get("accountId"));
		
		// 종목수
		result.put("ingInfiniteCount", viewInfiniteListMapper.countByInfinite(query));
		
		// 배정 시드 총합
		result.put("sumInfiniteSeed", viewInfiniteListMapper.sumByInfiniteSeed(query));
		
		// 총 매입금액
		result.put("sumInfiniteBuyPrice", viewInfiniteListMapper.sumByBuyPrice(query));
		
		return result;
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
		history.setFees(fees.setScale(2, BigDecimal.ROUND_FLOOR));
		
		history.setRegisteredType(RregisteredType.MANUAL.name());
		history.setRegisteredDate(LocalDateTime.now());
		history.setIsDeleted(false);
		infiniteHistoryMapper.insert(history);
		return true;
	}

	/**
	 * 무한매수 종목 수정
	 * @param params
	 * @return
	 */
	public Boolean updateinfiniteStock(Map<String, Object> params) {
		if(params.get("infiniteId") == null)
			return false;
		
		InfiniteStock infiniteStock = new InfiniteStock();
		
		if(params.get("infiniteState") != null) {
			switch ((String) params.get("state")) {
			case InfiniteState.ING:
			case InfiniteState.STOP:
				infiniteStock.setInfiniteState((String) params.get("state"));
			default:
				return false;
			}
		}
		
		if(params.get("infiniteType") != null) {
			switch ((String) params.get("infiniteType")) {
			case InfiniteType.V1:
			case InfiniteType.V2:
			case InfiniteType.V2_1:
				infiniteStock.setInfiniteType((String) params.get("infiniteType"));
			default:
				return false;
			}
		}
		
		if(params.get("seed") != null) {
			infiniteStock.setSeed(new BigDecimal(params.get("seed").toString()));
		}
		
		if(params.get("startedDate") != null) {
			infiniteStock.setStartedDate(LocalDate.parse(params.get("startedDate").toString()));
		}
		
		InfiniteStockExample example = new InfiniteStockExample();
		example.createCriteria().andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()))
			.andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()));
		
		infiniteStockMapper.updateByExampleSelective(infiniteStock, example);
		
		return true;
	}
}
