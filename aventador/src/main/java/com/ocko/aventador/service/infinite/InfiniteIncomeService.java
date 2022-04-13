package com.ocko.aventador.service.infinite;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncome;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncomeExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteIncomeExample.Criteria;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteIncomeMapper;
import com.ocko.aventador.model.infinite.IncomeByMonthly;
import com.ocko.aventador.model.infinite.IncomeByStock;

@Service
public class InfiniteIncomeService {
	
	@Autowired private ViewInfiniteIncomeMapper viewInfiniteIncomeMapper;
	
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
			example.setOrderByClause("sell_date " + params.get("order").toString());
		
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
	
}
