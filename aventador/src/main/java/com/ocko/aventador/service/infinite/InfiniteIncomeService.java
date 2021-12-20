package com.ocko.aventador.service.infinite;

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
		example.setOrderByClause("done_date desc");
		
		List<ViewInfiniteList> list = viewInfiniteListMapper.selectByExample(example);;
		
		List<InfiniteDetail> infiniteStockList = new ArrayList<InfiniteDetail>();
		for(ViewInfiniteList viewInfinite : list) {
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			infiniteStockList.add(infiniteDetail);
		}
		
		return infiniteStockList;
	}

}
