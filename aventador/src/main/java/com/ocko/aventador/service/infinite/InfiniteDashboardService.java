/**
 * 
 */
package com.ocko.aventador.service.infinite;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.dao.model.aventador.InfiniteHistoryExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyTwoMonth;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyTwoMonthExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteBuyTwoMonthMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.model.infinite.IncomeByMonthly;
import com.ocko.aventador.model.infinite.IncomeByStock;
import com.ocko.aventador.model.infinite.InfiniteDetail;


/**
 * @author ok
 * 대시보드 관련
 */
@Service
public class InfiniteDashboardService {
	
	@Autowired private ViewInfiniteBuyTwoMonthMapper viewBuyMapper;
	@Autowired private ViewInfiniteListMapper viewInfiniteListMapper;
	@Autowired private InfiniteHistoryMapper infiniteHistoryMapper;
	@Autowired private InfiniteIncomeService infiniteIncomeservice;
	
	/**
	 * 월별 실현 손익 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<IncomeByMonthly> getIncomeByMonthly(int memberId, Map<String, Object> params){
		// 최근 12개월
		LocalDate today = LocalDate.now();
		LocalDate before = LocalDate.now().minusMonths(11);
		
		params.put("sellDateStart", before);
		params.put("sellDateEnd", today);
		params.put("order", "asc");
		
		return infiniteIncomeservice.getIncomeByMonthly(memberId, params);
	}

	/**
	 * 종목별 실현 손익 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<IncomeByStock> getIncomeByStock(int memberId, Map<String, Object> params) {
		return infiniteIncomeservice.getIncomeByStock(memberId, params);
	}

	/**
	 * 최근 2개월 매입금액 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public Map<String, List<ViewInfiniteBuyTwoMonth>> getBuyDaily(int memberId, Map<String, Object> params) {
		
		Map<String, List<ViewInfiniteBuyTwoMonth>> result = new HashMap<String, List<ViewInfiniteBuyTwoMonth>>();
		
		// 이번달
		YearMonth today = YearMonth.now();
		result.put("thisMonth", getBuyDaily(memberId, params, today));
		
		// 전월
		YearMonth before = YearMonth.now().minusMonths(1);
		result.put("beforeMonth", getBuyDaily(memberId, params, before));
		
		return result;
	}
	
	/**
	 * 매입금액 가져오기
	 * @param memberId
	 * @param params
	 * @param month
	 * @return
	 */
	private List<ViewInfiniteBuyTwoMonth> getBuyDaily(int memberId, Map<String, Object> params, YearMonth month) {
		ViewInfiniteBuyTwoMonthExample example = new ViewInfiniteBuyTwoMonthExample();
		com.ocko.aventador.dao.model.aventador.ViewInfiniteBuyTwoMonthExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		if(params.get("accountId") != null && !params.get("accountId").toString().equals("ALL"))
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		criteria.andTradeDateBetween(month.atDay(1), month.atEndOfMonth());
		
		example.setOrderByClause("trade_date asc");
	
		return viewBuyMapper.selectByExample(example);
	}

	/**
	 * 일별 소진률용 데이터 가져오기
	 * @param memberId
	 * @param params
	 * @return
	 */
	public List<InfiniteDetail> getRunoutRateList(Integer memberId, Map<String, Object> params) {
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample.Criteria criteria = example.createCriteria().andMemberIdEqualTo(memberId);
		criteria.andStartedDateGreaterThanOrEqualTo(LocalDate.now().minusMonths(6)); // 종목 시작일 6개월
		if(params.get("accountId") != null && !params.get("accountId").toString().equals("ALL"))
			criteria.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		example.setOrderByClause("started_date asc");
		
		List<InfiniteDetail> infiniteStockList = new ArrayList<InfiniteDetail>();
		
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
			
			infiniteStockList.add(infiniteDetail);
		}
		
		return infiniteStockList;
	}

}
