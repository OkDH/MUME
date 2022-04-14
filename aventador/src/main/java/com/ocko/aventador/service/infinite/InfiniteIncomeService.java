package com.ocko.aventador.service.infinite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.dao.model.aventador.InfiniteIncome;
import com.ocko.aventador.dao.model.aventador.InfiniteIncomeExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncome;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncomeExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncomeExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.InfiniteIncomeMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteIncomeMapper;
import com.ocko.aventador.model.infinite.IncomeByMonthly;
import com.ocko.aventador.model.infinite.IncomeByStock;

@Service
public class InfiniteIncomeService {
	
	@Autowired private ViewInfiniteIncomeMapper viewInfiniteIncomeMapper;
	@Autowired private InfiniteIncomeMapper infiniteIncomeMapper;
	
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
	
}
