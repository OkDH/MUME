/**
 * 
 */
package com.ocko.aventador.service.infinite;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyDaily;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyDailyExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthly;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthlyExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthlyExample.Criteria;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitStock;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitStockExample;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteBuyDailyMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteProfitMonthlyMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteProfitStockMapper;


/**
 * @author ok
 * 대시보드 관련
 */
@Service
public class InfiniteDashboardService {
	
	@Autowired private ViewInfiniteProfitMonthlyMapper viewProfitMonthlyMapper; 
	@Autowired private ViewInfiniteProfitStockMapper viewProfitStockMapper;
	@Autowired private ViewInfiniteBuyDailyMapper ViewBuyDailyMapper;
	
	/**
	 * 월별 실현 손익 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<ViewInfiniteProfitMonthly> getProfitMonthly(int memberId, Map<String, Object> params){
		ViewInfiniteProfitMonthlyExample example = new ViewInfiniteProfitMonthlyExample();
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null)
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		
		// 최근 12개월
		LocalDate today = LocalDate.now();
		LocalDate before = LocalDate.now().minusMonths(11);
		String thisMonthText = today.getYear() + "-" + today.getMonthValue();
		String beforeMonthText = before.getYear() + "-" + before.getMonthValue();
		criteria.andMonthlyBetween(beforeMonthText, thisMonthText);
		
		example.setOrderByClause("monthly asc");
		
		return viewProfitMonthlyMapper.selectByExample(example);
	}

	/**
	 * 종목별 실현 손익 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<ViewInfiniteProfitStock> getProfitStock(int memberId, Map<String, Object> params) {
		ViewInfiniteProfitStockExample example = new ViewInfiniteProfitStockExample();
		com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitStockExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null)
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		example.setOrderByClause("symbol asc");
		
		return viewProfitStockMapper.selectByExample(example);
	}

	/**
	 * 최근 2개월 매입금액 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public Map<String, List<ViewInfiniteBuyDaily>> getBuyDaily(int memberId, Map<String, Object> params) {
		
		Map<String, List<ViewInfiniteBuyDaily>> result = new HashMap<String, List<ViewInfiniteBuyDaily>>();
		
		// 이번달
		LocalDate today = LocalDate.now();
		String thisMonthText = today.getYear() + "-" + today.getMonthValue();
		result.put("thisMonth", getBuyDaily(memberId, params, thisMonthText));
		
		
		// 최근 2개월
		LocalDate before = LocalDate.now().minusMonths(1);
		String beforeMonthText = before.getYear() + "-" + before.getMonthValue();
		result.put("beforeMonth", getBuyDaily(memberId, params, beforeMonthText));
		
		return result;
	}
	
	/**
	 * 매입금액 가져오기
	 * @param memberId
	 * @param params
	 * @param month
	 * @return
	 */
	private List<ViewInfiniteBuyDaily> getBuyDaily(int memberId, Map<String, Object> params, String month) {
		ViewInfiniteBuyDailyExample example = new ViewInfiniteBuyDailyExample();
		com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyDailyExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null)
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		criteria.andDailyLike(month + "%");
		
		example.setOrderByClause("daily asc");
	
		return ViewBuyDailyMapper.selectByExample(example);
	}

}
