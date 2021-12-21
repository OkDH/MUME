package com.ocko.aventador.service.infinite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample.Criteria;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthly;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.model.InfiniteDetail;

@Service
public class InfiniteIncomeService {
	
	@Autowired private ViewInfiniteListMapper viewInfiniteListMapper;
	
	/**
	 * 손익현황 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<InfiniteDetail> getIncomeProfit(int memberId, Map<String, Object> params){
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId).andInfiniteStateEqualTo(InfiniteState.DONE);
		if(params.get("accountId") != null)
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		if(params.get("doneDateStart") != null && params.get("doneDateEnd") != null)
			criteria.andDoneDateBetween(LocalDate.parse(params.get("doneDateStart").toString()), LocalDate.parse(params.get("doneDateEnd").toString()));
		if(params.get("order") != null)
			example.setOrderByClause("done_date " + params.get("order").toString());
		
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);
		
		List<InfiniteDetail> infiniteStockList = new ArrayList<InfiniteDetail>();
		for(ViewInfiniteList viewInfinite : list) {
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			infiniteStockList.add(infiniteDetail);
		}
		
		return infiniteStockList;
	}
	
	/**
	 * 종목별 손익 조회
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> getIncomeProfitStock(int memberId, Map<String, Object> params){
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId).andInfiniteStateEqualTo(InfiniteState.DONE);
		if(params.get("accountId") != null)
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		if(params.get("doneDateStart") != null && params.get("doneDateEnd") != null)
			criteria.andDoneDateBetween(LocalDate.parse(params.get("doneDateStart").toString()), LocalDate.parse(params.get("doneDateEnd").toString()));
		example.setOrderByClause("sell_count desc");
				
		List<Map<String, Object>> list = viewInfiniteListMapper.selectProfitStock(example);
		for(Map<String, Object> data : list) {
			BigDecimal buyPrice = (BigDecimal) data.get("total_buy_price");
			BigDecimal sellPrice = (BigDecimal) data.get("total_sell_price");
			
			BigDecimal buyFees = buyPrice.multiply(new BigDecimal("0.0007")).setScale(2, RoundingMode.DOWN);
			BigDecimal sellFees = sellPrice.multiply(new BigDecimal("0.0007")).setScale(2, RoundingMode.DOWN);
			
			BigDecimal income = sellPrice.subtract(buyPrice).subtract(buyFees).subtract(sellFees);
			BigDecimal incomePer = income.divide(buyPrice, 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
			
			data.put("income", income);
			data.put("incomePer", incomePer);
		}
		return list;
	}
	
	/**
	 * 월별 손익현황
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<ViewInfiniteProfitMonthly> getIncomeProfitMonthly(int memberId, Map<String, Object> params){
		return null;
	}

}
