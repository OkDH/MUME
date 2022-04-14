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
import com.ocko.aventador.constant.InfiniteVersion;
import com.ocko.aventador.constant.RegisteredType;
import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteHistoryExample;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.InfiniteAccountMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.model.StockDetail;
import com.ocko.aventador.model.infinite.InfiniteDetail;
import com.ocko.aventador.service.StockService;

@Service
public class InfiniteStockService {

	@Autowired private StockService stockService;
	@Autowired private InfiniteTradeComponent tradeComponent;
	@Autowired private InfiniteAccountMapper infiniteAccountMapper;
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
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null && !params.get("accountId").toString().equals("ALL"))
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		if(params.get("infiniteState") != null) {
			if(!((List<String>) params.get("infiniteState")).isEmpty())
				criteria.andInfiniteStateIn((List<String>) params.get("infiniteState"));
		}
		if(params.get("infiniteType") != null) {
			if(!((List<String>) params.get("infiniteType")).isEmpty())
				criteria.andInfiniteTypeIn((List<String>) params.get("infiniteType"));
		}
		if(params.get("infiniteVersion") != null) {
			if(!((List<String>) params.get("infiniteVersion")).isEmpty())
				criteria.andInfiniteVersionIn((List<String>) params.get("infiniteVersion"));
		}
		if(params.get("offset") != null) {
			example.setOffset(Integer.parseInt(params.get("offset").toString()));
		}
		if(params.get("limit") != null) {
			example.setLimit(Integer.parseInt(params.get("limit").toString()));
		}
		if(params.get("orderBy") != null) {
			example.setOrderByClause(params.get("orderBy").toString());
		} else {
			example.setOrderByClause("registered_date desc");
		}
		
		// view 조회
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);
		
		// etf 주가 정보 조회
		Map<String, StockDetail> stockMap = stockService.getTodayEtfStocks();
		
		// 손익정보 등 값 추가
		List<InfiniteDetail> infiniteStockList = new ArrayList<InfiniteDetail>();
		for(ViewInfiniteList viewInfinite : list) {
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			
			// etf 주가 정보 추가
			infiniteDetail.setStockDetail(stockMap.get(viewInfinite.getSymbol()));
			
			// 매매내역
			InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
			historyExample.createCriteria().andInfiniteIdEqualTo(viewInfinite.getInfiniteId()).andIsDeletedEqualTo(false);
			historyExample.setOrderByClause("trade_date asc, trade_type asc, registered_date asc");
			infiniteDetail.setHistoryList(infiniteHistoryMapper.selectByExample(historyExample));
			
			// 매수 정보
			infiniteDetail.setBuyTradeInfoList(tradeComponent.getBuyInfo(infiniteDetail));
			
			// 매도 정보
			infiniteDetail.setSellTradeInfoList(tradeComponent.getSellInfo(infiniteDetail));
			
			infiniteStockList.add(infiniteDetail);
		}
		
		return infiniteStockList;
	}
	
	/**
	 * 무매 종목 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public InfiniteDetail getStock(int memberId, Map<String, Object> params){
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		example.createCriteria().andMemberIdEqualTo(memberId)
				.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()))
				.andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()));
		
		// 조회
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);
		
		if(list.isEmpty())
			return null;
		
		InfiniteDetail infiniteDetail = new InfiniteDetail();
		
		// 객체복사
		BeanUtils.copyProperties(list.get(0), infiniteDetail);
		
		// 매매내역
		InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
		historyExample.createCriteria().andInfiniteIdEqualTo(list.get(0).getInfiniteId()).andIsDeletedEqualTo(false);
		historyExample.setOrderByClause("trade_date asc, trade_type asc, registered_date asc");
		List<InfiniteHistory> historyList = infiniteHistoryMapper.selectByExample(historyExample);
		infiniteDetail.setHistoryList(historyList);
		
		// 주가 히스토리
		if(!historyList.isEmpty()) {
			LocalDate startDate = historyList.get(0).getTradeDate();
			LocalDate endDate = null;
			
			// 매도 완료 종목이라면 매매내역 마지막날짜까지만 조회
			// 그 외는 오늘날짜까지(null은 오늘날짜까지)
			if(infiniteDetail.getInfiniteState().equals(InfiniteState.DONE))
				endDate = historyList.get(historyList.size()-1).getTradeDate();
			
			infiniteDetail.setStockList(stockService.getStockHistoryBetweenDate(list.get(0).getSymbol(), startDate, endDate));
		}
		
		return infiniteDetail;
	}
	
	/**
	 * 내 계좌 통계 정보
	 * @param memberId
	 * @param params accountId가 null일 경우 전체 전체 계좌로 통계 
	 * @return
	 */
	public Map<String, Object> getMyAccountState(int memberId, Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<String> infiniteStateList = new ArrayList<String>();
		infiniteStateList.add(InfiniteState.ING);
		infiniteStateList.add(InfiniteState.OUT);
		infiniteStateList.add(InfiniteState.STOP);
		
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId).andInfiniteStateIn(infiniteStateList);
		
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("memberId", memberId);
		query.put("infiniteStateList", infiniteStateList); 
		if(params.get("accountId") != null && !params.get("accountId").equals("ALL")) {
			query.put("accountId", params.get("accountId"));
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		}
		
		// 종목수
		result.put("ingInfiniteCount", viewInfiniteListMapper.countByInfinite(query));
		
		// 배정 시드 총합
		result.put("sumInfiniteSeed", viewInfiniteListMapper.sumByInfiniteSeed(query));
		
		// 원금
		result.put("sumAccountSeed", infiniteAccountMapper.sumByAccountSeed(query));
		
		// 총 매입금액
		BigDecimal sumByBuyPrice = BigDecimal.ZERO;
		
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);
		for(ViewInfiniteList viewInfinite : list) {
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			
			// 매매내역
			InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
			historyExample.createCriteria().andInfiniteIdEqualTo(viewInfinite.getInfiniteId()).andIsDeletedEqualTo(false);
			historyExample.setOrderByClause("trade_date asc, trade_type asc, registered_date asc");
			infiniteDetail.setHistoryList(infiniteHistoryMapper.selectByExample(historyExample));
			
			sumByBuyPrice = sumByBuyPrice.add(infiniteDetail.getBuyPrice());
		}
		
		result.put("sumInfiniteBuyPrice", sumByBuyPrice);
		
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
		stock.setInfiniteVersion(params.get("infiniteVersion").toString());
		stock.setDivisions(Integer.parseInt(params.get("divisions").toString()));
		stock.setIsDeleted(false);
		stock.setIsKskyj(false);
		stock.setIsAutoTrade(Boolean.parseBoolean(params.get("isAutoTrade").toString()));
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
		
		history.setRegisteredType(RegisteredType.MANUAL.name());
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
		
		InfiniteStock infiniteStock = new InfiniteStock();
		
		if(params.get("infiniteState") != null) {
			switch ((String) params.get("infiniteState")) {
			case InfiniteState.DONE:
				if(params.get("doneDate") != null) {
					// 매도완료일때 완료일 넣어주기
					infiniteStock.setDoneDate(LocalDate.parse(params.get("doneDate").toString()));
				} else { // null이라면 오늘 날짜
					infiniteStock.setDoneDate(LocalDate.now());
				}
			case InfiniteState.ING:
			case InfiniteState.STOP:
			case InfiniteState.OUT:
				infiniteStock.setInfiniteState((String) params.get("infiniteState"));
				break;
			default:
				return false;
			}
		}
		
		if(params.get("infiniteType") != null) {
			switch ((String) params.get("infiniteType")) {
			case InfiniteType.INFINITE:
			case InfiniteType.TLP:
				infiniteStock.setInfiniteType((String) params.get("infiniteType"));
				break;
			default:
				return false;
			}
		}
		
		if(params.get("infiniteVersion") != null) {
			switch ((String) params.get("infiniteVersion")) {
			case InfiniteVersion.V1:
			case InfiniteVersion.V2:
			case InfiniteVersion.V2_1:
			case InfiniteVersion.V2_1_SH:
				infiniteStock.setInfiniteVersion((String) params.get("infiniteVersion"));
				break;
			default:
				return false;
			}
		}
		
		if(params.get("seed") != null) {
			infiniteStock.setSeed(new BigDecimal(params.get("seed").toString()));
		}
		
		if(params.get("divisions") != null) {
			infiniteStock.setDivisions(Integer.parseInt(params.get("divisions").toString()));
		}
		
		if(params.get("startedDate") != null) {
			infiniteStock.setStartedDate(LocalDate.parse(params.get("startedDate").toString()));
		}
		
		if(params.get("isDeleted") != null) {
			infiniteStock.setIsDeleted(Boolean.parseBoolean(params.get("isDeleted").toString()));
		}
		
		if(params.get("isKskyj") != null) {
			infiniteStock.setIsKskyj(Boolean.parseBoolean(params.get("isKskyj").toString()));
		}
		
		if(params.get("isAutoTrade") != null) {
			infiniteStock.setIsAutoTrade(Boolean.parseBoolean(params.get("isAutoTrade").toString()));
		}
		
		InfiniteStockExample example = new InfiniteStockExample();
		example.createCriteria().andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()))
			.andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()));
		
		infiniteStockMapper.updateByExampleSelective(infiniteStock, example);
		
		// 종목이 삭제 된다면 매매 기록도 삭제 처리
		if(params.get("isDeleted") != null) {
			if(Boolean.parseBoolean(params.get("isDeleted").toString())) {
				InfiniteHistory history = new InfiniteHistory();
				history.setIsDeleted(true);
				history.setUpdatedDate(LocalDateTime.now());
				
				InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
				historyExample.createCriteria().andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()));
				
				infiniteHistoryMapper.updateByExampleSelective(history, historyExample);
			}
		}
		
		return true;
	}
	
	/**
	 * 종목 매매 내역 조회
	 * @param params
	 * @return
	 */
	public List<InfiniteHistory> getStockHistory(Map<String, Object> params){
		InfiniteHistoryExample example = new InfiniteHistoryExample();
		example.createCriteria()
			.andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()))
			.andIsDeletedEqualTo(false);
		example.setOrderByClause("trade_date desc, registered_date desc");
		
		return infiniteHistoryMapper.selectByExample(example);
	}

	/**
	 * 종목 매매 내역 추가
	 * @param params
	 * @return
	 */
	public Boolean addStockHistory(Map<String, Object> params) {
		InfiniteHistory history = new InfiniteHistory();
		history.setInfiniteId(Integer.parseInt(params.get("infiniteId").toString()));
		history.setTradeDate(LocalDate.parse(params.get("tradeDate").toString()));
		history.setTradeType(params.get("tradeType").toString());
		history.setUnitPrice(new BigDecimal(params.get("unitPrice").toString()));
		history.setQuantity(Integer.parseInt(params.get("quantity").toString()));
		history.setIsDeleted(false);
		history.setRegisteredDate(LocalDateTime.now());
		history.setRegisteredType(RegisteredType.MANUAL.name());
		infiniteHistoryMapper.insert(history);
		
		// 매도 내역 추가인 경우 손익추가
		if(history.getTradeType().equals(TradeType.SELL)) {
			
		}
		
		return true;
	}
	
	/**
	 * 종목 매매 내역 변경
	 * @param params
	 * @return
	 */
	public Boolean updateStockHistory(Map<String, Object> params) {
		
		InfiniteHistory history = new InfiniteHistory();
		
		if(params.get("tradeDate") != null) {
			history.setTradeDate(LocalDate.parse(params.get("tradeDate").toString()));
		}
		
		if(params.get("unitPrice") != null) {
			history.setUnitPrice(new BigDecimal(params.get("unitPrice").toString()));
		}
		
		if(params.get("quantity") != null) {
			history.setQuantity(Integer.parseInt(params.get("quantity").toString()));
		}
		
		if(params.get("isDeleted") != null) {
			history.setIsDeleted(Boolean.parseBoolean(params.get("isDeleted").toString()));
		}
		
		history.setUpdatedDate(LocalDateTime.now());
		history.setRegisteredType(RegisteredType.UPDATE.name());
		
		InfiniteHistoryExample example = new InfiniteHistoryExample();
		example.createCriteria().andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()))
			.andInfiniteHistoryIdEqualTo(Integer.parseInt(params.get("infiniteHistoryId").toString()));
		
		infiniteHistoryMapper.updateByExampleSelective(history, example);
		
		return true;
	}
}
