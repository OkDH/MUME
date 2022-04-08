package com.ocko.aventador.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.dao.model.aventador.InfiniteAccount;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.StockService;
import com.ocko.aventador.service.infinite.InfiniteAccountService;
import com.ocko.aventador.service.infinite.InfiniteDashboardService;
import com.ocko.aventador.service.infinite.InfiniteIncomeService;
import com.ocko.aventador.service.infinite.InfiniteStockService;

/**
 * 
 * @author ocko112
 *
 */
@Controller
public class InfiniteController {
	
	@Autowired private AuthenticationService authenticationService;
	@Autowired private InfiniteAccountService accountService;
	@Autowired private InfiniteStockService infiniteStockService;
	@Autowired private InfiniteDashboardService dashboardService;
	@Autowired private InfiniteIncomeService incomeService;
	@Autowired private StockService stockService;
	
	/**
	 * 내 계좌 리스트 조회
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/api/infinite/my-account", method = RequestMethod.GET)
	public @ResponseBody List<InfiniteAccount> getMyAccount() throws Exception {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		return accountService.getMyAccounts(memberInfo.getMemberId());
	}
	
	/**
	 * 무한매수 계좌 추가
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/account/add", method = RequestMethod.POST)
	public ResponseEntity<Boolean> addInfiniteAccount(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		return new ResponseEntity<Boolean>(accountService.addAccount(memberInfo.getMemberId(), params), HttpStatus.OK);
	}
	
	/**
	 * 무한매수 계좌 변경
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/account/update", method = RequestMethod.POST)
	public ResponseEntity<Boolean> updateInfiniteAccount(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") != null) {
			if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
				throw new MyAccessDeniedException();
		}
		
		// 계좌 순서 변경일 경우
		if(params.get("updateOrderList") != null) {
			List<Map<String, Object>> updateOrderList = (List<Map<String, Object>>) params.get("updateOrderList");
			for(Map<String, Object> param : updateOrderList)
				accountService.updateAccount(memberInfo.getMemberId(), param);
			return new ResponseEntity<Boolean>(HttpStatus.OK);
		}
		// 일반 업데이트일 경우
		return new ResponseEntity<Boolean>(accountService.updateAccount(memberInfo.getMemberId(), params), HttpStatus.OK);
	}
	
	/**
	 * 계좌 내 종목 조회
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stocks", method = RequestMethod.POST)
	public ResponseEntity<List<InfiniteDetail>> getMyStocks(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		
		if(params.get("accountId") != null) {
			if(!params.get("accountId").toString().equals("ALL"))
				if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
					throw new MyAccessDeniedException();
		}
		
		return new ResponseEntity<List<InfiniteDetail>>(infiniteStockService.getStocks(memberInfo.getMemberId(), params), HttpStatus.OK);
	}
	
	/**
	 * 종목 조회
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock", method = RequestMethod.POST)
	public ResponseEntity<InfiniteDetail> getMyStock(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		
		if(params.get("accountId") != null) {
			if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
				throw new MyAccessDeniedException();
		}
		
		return new ResponseEntity<InfiniteDetail>(infiniteStockService.getStock(memberInfo.getMemberId(), params), HttpStatus.OK);
	}
	
	/**
	 * 계좌 내 종목들 현황 조회
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/account/state", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> getMyAccountState(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		
		if(params.get("accountId") != null) {
			if(!params.get("accountId").toString().equals("ALL"))
				if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
					throw new MyAccessDeniedException();
		}
		
		return new ResponseEntity<Map<String, Object>>(infiniteStockService.getMyAccountState(memberInfo.getMemberId(), params), HttpStatus.OK);
	}
	
	/**
	 * 무한매수 종목 추가
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock/add", method = RequestMethod.POST)
	public ResponseEntity<Boolean> addStock(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") == null)
			throw new MyArgumentException();
		if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
			throw new MyAccessDeniedException();
		return new ResponseEntity<Boolean>(infiniteStockService.addStock(params), HttpStatus.OK);
	}
	
	/**
	 * 무한매수 매수 수정
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock/update", method = RequestMethod.POST)
	public ResponseEntity<Boolean> updateinfiniteStock(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") == null || params.get("infiniteId") == null)
			throw new MyArgumentException();
		if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
			throw new MyAccessDeniedException();
		
		return new ResponseEntity<Boolean>(infiniteStockService.updateinfiniteStock(params), HttpStatus.OK);
	}
	
	/**
	 * 무한매수 매매 내역 조회
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock/history", method = RequestMethod.POST)
	public ResponseEntity<List<InfiniteHistory>> getStockHistory(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") == null || params.get("infiniteId") == null)
			throw new MyArgumentException();
		if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
			throw new MyAccessDeniedException();
		
		return new ResponseEntity<List<InfiniteHistory>>(infiniteStockService.getStockHistory(params), HttpStatus.OK);
	}
	
	/**
	 * 무한매수 매매 내역 추가
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock/history/add", method = RequestMethod.POST)
	public ResponseEntity<Boolean> addStockHistory(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") == null || params.get("infiniteId") == null)
			throw new MyArgumentException();
		if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
			throw new MyAccessDeniedException();
		
		return new ResponseEntity<Boolean>(infiniteStockService.addStockHistory(params), HttpStatus.OK);
	}
	
	/**
	 * 무한매수 매매 내역 변경
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock/history/update", method = RequestMethod.POST)
	public ResponseEntity<Boolean> updateStockHistory(@RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") == null || params.get("infiniteId") == null || params.get("infiniteHistoryId") == null)
			throw new MyArgumentException();
		if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
			throw new MyAccessDeniedException();
		
		return new ResponseEntity<Boolean>(infiniteStockService.updateStockHistory(params), HttpStatus.OK);
	}
	
	/**
	 * 대시보드 통계 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/statistics/{type}", method = RequestMethod.POST)
	public ResponseEntity<Object> getInfiniteStatistics(@PathVariable String type, @RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") != null) {
			if(!params.get("accountId").toString().equals("ALL"))
				if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
					throw new MyAccessDeniedException();
		}
		
		if(type.equals("profit-monthly"))
			return new ResponseEntity<Object>(dashboardService.getProfitMonthly(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("profit-stock"))
			return new ResponseEntity<Object>(dashboardService.getProfitStock(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("buy-daily"))
			return new ResponseEntity<Object>(dashboardService.getBuyDaily(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("runout-rate")) {
			Map<String, Object> result = new HashMap<String, Object>();
			// 소진률데이터를 위한 종목 및 종목 매매내역 조회
			List<InfiniteDetail> stockList = dashboardService.getRunoutRateList(memberInfo.getMemberId(), params);
			result.put("stockList", stockList);
			// 일자 라벨를 위한 일자 리스트 조회
			if(!stockList.isEmpty())
				result.put("dateList", stockService.getStockHistoryBetweenDate("TQQQ", stockList.get(0).getStartedDate(), null));
			return new ResponseEntity<Object>(result ,HttpStatus.OK);
		}
		
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * 손익현황
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/income/{type}", method = RequestMethod.POST)
	public ResponseEntity<Object> getInfiniteIncome(@PathVariable String type, @RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") != null) {
			if(!params.get("accountId").toString().equals("ALL"))
				if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
					throw new MyAccessDeniedException();
		}
		
		if(type.equals("profit"))
			return new ResponseEntity<Object>(incomeService.getIncomeProfit(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("stock"))
			return new ResponseEntity<Object>(incomeService.getIncomeProfitStock(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("monthly"))
			return new ResponseEntity<Object>(incomeService.getIncomeProfitMonthly(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
}
