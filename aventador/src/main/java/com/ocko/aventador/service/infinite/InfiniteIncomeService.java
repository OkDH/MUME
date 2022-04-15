package com.ocko.aventador.service.infinite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteHistoryExample;
import com.ocko.aventador.dao.model.aventador.InfiniteIncome;
import com.ocko.aventador.dao.model.aventador.InfiniteIncomeExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncome;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncomeExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncomeExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteIncomeMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteIncomeMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.model.AveragePriceInfo;
import com.ocko.aventador.model.infinite.IncomeByMonthly;
import com.ocko.aventador.model.infinite.IncomeByStock;
import com.ocko.aventador.model.infinite.InfiniteDetail;

@Service
public class InfiniteIncomeService {
	
	@Autowired private ViewInfiniteIncomeMapper viewInfiniteIncomeMapper;
	@Autowired private InfiniteIncomeMapper infiniteIncomeMapper;
	@Autowired private ViewInfiniteListMapper viewInfiniteListMapper;
	@Autowired private InfiniteHistoryMapper infiniteHistoryMapper;
	
	/**
	 * 손익현황 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<ViewInfiniteIncome> getIncomeList(int memberId, Map<String, Object> params){
		
		ViewInfiniteIncomeExample example = new ViewInfiniteIncomeExample();
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null && !params.get("accountId").toString().equals("ALL"))
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		if(params.get("sellDateStart") != null && params.get("sellDateEnd") != null)
			criteria.andSellDateBetween(LocalDate.parse(params.get("sellDateStart").toString()), LocalDate.parse(params.get("sellDateEnd").toString()));
		if(params.get("order") != null)
			example.setOrderByClause("sell_date " + params.get("order").toString() + ", registered_date desc");
		
		return viewInfiniteIncomeMapper.selectByExample(example);
	}
	
	/**
	 * 종목별 손익 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<IncomeByStock> getIncomeByStock(int memberId, Map<String, Object> params){
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberId", memberId);
		
		// 계좌별 조회
		if(params.get("accountId") != null && !params.get("accountId").toString().equals("ALL")) {
			param.put("accountId", params.get("accountId"));
		}
		
		if(params.get("sellDateStart") != null && params.get("sellDateEnd") != null) {
			param.put("sellDateStart", LocalDate.parse(params.get("sellDateStart").toString()));
			param.put("sellDateEnd", LocalDate.parse(params.get("sellDateEnd").toString()));
		}
		
		return viewInfiniteIncomeMapper.selectIncomeByStock(param);
	}
	
	/**
	 * 월별 손익 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<IncomeByMonthly> getIncomeByMonthly(int memberId, Map<String, Object> params){
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("memberId", memberId);
		
		// 계좌별 조회
		if(params.get("accountId") != null && !params.get("accountId").toString().equals("ALL")) {
			param.put("accountId", params.get("accountId"));
		}
		
		if(params.get("sellDateStart") != null && params.get("sellDateEnd") != null) {
			param.put("sellDateStart", LocalDate.parse(params.get("sellDateStart").toString()));
			param.put("sellDateEnd", LocalDate.parse(params.get("sellDateEnd").toString()));
		}
		
		if(params.get("order") != null)
			param.put("order", params.get("order").toString());
		
		return viewInfiniteIncomeMapper.selectIncomeByMonthly(param);
	}

	/**
	 * 손익현황 수정
	 * @param params
	 * @return
	 */
	public Boolean updateIncome(Map<String, Object> params) {
		InfiniteIncome infiniteIncome = new InfiniteIncome();
		
		if(params.get("buyPrice") != null) {
			infiniteIncome.setBuyPrice(new BigDecimal(params.get("buyPrice").toString()).setScale(2, RoundingMode.HALF_UP));
		}
		
		if(params.get("sellPrice") != null) {
			infiniteIncome.setSellPrice(new BigDecimal(params.get("sellPrice").toString()).setScale(2, RoundingMode.HALF_UP));
		}
		
		if(params.get("income") != null) {
			infiniteIncome.setIncome(new BigDecimal(params.get("income").toString()).setScale(2, RoundingMode.HALF_UP));
		}
		
		if(params.get("fees") != null) {
			infiniteIncome.setFees(new BigDecimal(params.get("fees").toString()).setScale(2, RoundingMode.HALF_UP));
		}
		
		InfiniteIncomeExample example = new InfiniteIncomeExample();
		example.createCriteria().andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()))
			.andInfiniteIdEqualTo(Integer.parseInt(params.get("infiniteId").toString()))
			.andInfiniteHistoryIdEqualTo(Integer.parseInt(params.get("infiniteHistoryId").toString()))
			.andIncomeIdEqualTo(Integer.parseInt(params.get("incomeId").toString()));
		
		infiniteIncomeMapper.updateByExampleSelective(infiniteIncome, example);
		
		return true;
	}
	
	/**
	 * 손익현황 추가
	 * @param memberId
	 * @param infiniteId
	 * @param infiniteHistoryId
	 */
	public void addIncome(int memberId, int infiniteId, int infiniteHistoryId) {
		
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		example.createCriteria().andMemberIdEqualTo(memberId).andInfiniteIdEqualTo(infiniteId);
		
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);
		
		if(!list.isEmpty()) {
			
			ViewInfiniteList viewInfinite = list.get(0);
			
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			
			// 매매내역
			InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
			historyExample.createCriteria().andInfiniteIdEqualTo(viewInfinite.getInfiniteId()).andIsDeletedEqualTo(false);
			historyExample.setOrderByClause("trade_date asc, trade_type asc, registered_date asc");
			List<InfiniteHistory> historyList = infiniteHistoryMapper.selectByExample(historyExample);

			// 매매내역에서 처음이 매도라면, 매수를 찾아서 맨앞으로 조정해줌 
			if(!historyList.isEmpty()) {
				if(historyList.get(0).getTradeType().equals(TradeType.SELL)) {
					for(int i = 0; i < historyList.size(); i++) {
						if(historyList.get(i).getTradeType().equals(TradeType.BUY)) {
							InfiniteHistory history = historyList.get(i);
							historyList.remove(i);
							historyList.add(0, history);
							break;
						}
					}
				}
			}
			
			// 매매내역 순회하면서 insert 된 history를 만나면 평단가 및 손익 계산해서 손익현황 추가
			for(int i = 0; i < historyList.size(); i++) {
				if(i == 0) 
					continue;
				if(historyList.get(i).getQuantity() == 0) 
					continue;
					
				// insert 된 history를 만나면 평단가 및 손익 계산해서 손익현황 추가
				if(historyList.get(i).getInfiniteHistoryId() == infiniteHistoryId) {
					// 바로 전 매매내역까지만 history 추가
					infiniteDetail.setHistoryList(new ArrayList<>(historyList.subList(0, i)));
					
					// 마지막 매매 후 평단가 조회
					List<AveragePriceInfo> averagePriceList = infiniteDetail.getAveragePriceList();
					AveragePriceInfo averagePriceInfo = infiniteDetail.getAveragePriceList().get(averagePriceList.size()-1);
					
					// 매수금액
					BigDecimal buyPrice = averagePriceInfo.getAveragePrice().multiply(new BigDecimal(historyList.get(i).getQuantity()));
					// 매도금액
					BigDecimal sellPrice = historyList.get(i).getUnitPrice().multiply(new BigDecimal(historyList.get(i).getQuantity()));
					
					// 수수료(소수점 2자리에서 버림) : (매수금액 + 매도금액) / 수수료율
					BigDecimal fees = buyPrice.add(sellPrice).multiply(infiniteDetail.getRealFeesPer()).setScale(2, RoundingMode.DOWN);
					// 손익금
					BigDecimal income = sellPrice.subtract(buyPrice).subtract(fees);
					
					// 진행률
					BigDecimal progressPer = BigDecimal.ZERO;
					if(infiniteDetail.getSeed() != null && infiniteDetail.getSeed().compareTo(new BigDecimal("0.0")) > 0)
						progressPer = buyPrice.divide(infiniteDetail.getSeed(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
					
					// 손익현황 추가
					InfiniteIncome infiniteIncome = new InfiniteIncome();
					infiniteIncome.setAccountId(infiniteDetail.getAccountId());
					infiniteIncome.setInfiniteId(infiniteDetail.getInfiniteId());
					infiniteIncome.setInfiniteHistoryId(historyList.get(i).getInfiniteHistoryId());
					infiniteIncome.setSellDate(historyList.get(i).getTradeDate()); // sellDate로 수정
					infiniteIncome.setProgressPer(progressPer);
					infiniteIncome.setAveragePrice(averagePriceInfo.getAveragePrice());
					infiniteIncome.setBuyPrice(buyPrice.setScale(2, RoundingMode.HALF_UP));
					infiniteIncome.setSellPrice(sellPrice.setScale(2, RoundingMode.HALF_UP));
					infiniteIncome.setIncome(income.setScale(2, RoundingMode.HALF_UP));
					infiniteIncome.setFees(fees);
					infiniteIncome.setRegisteredDate(LocalDateTime.now());
					infiniteIncome.setIsDeleted(false);
					
					infiniteIncomeMapper.insert(infiniteIncome);
					
					break;
				}
			}
		}
	}
	
}
