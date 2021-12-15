package com.ocko.aventador.controller;

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
import com.ocko.aventador.service.infinite.InfiniteAccountService;
import com.ocko.aventador.service.infinite.InfiniteDashboardService;
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
	@Autowired private InfiniteStockService stockService;
	@Autowired private InfiniteDashboardService dashboardService;
	
	/**
	 * 내 계좌 리스트 조회
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/my-account", method = RequestMethod.GET)
	public @ResponseBody List<InfiniteAccount> getMyAccount() {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		return accountService.getMyAccounts(memberInfo.getMemberId());
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
			if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
				throw new MyAccessDeniedException();
		}
		
		return new ResponseEntity<List<InfiniteDetail>>(stockService.getStocks(memberInfo.getMemberId(), params), HttpStatus.OK);
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
			if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
				throw new MyAccessDeniedException();
		}
		
		return new ResponseEntity<Map<String, Object>>(stockService.getMyAccountState(memberInfo.getMemberId(), params), HttpStatus.OK);
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
		return new ResponseEntity<Boolean>(stockService.addStock(params), HttpStatus.OK);
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
		
		return new ResponseEntity<Boolean>(stockService.updateinfiniteStock(params), HttpStatus.OK);
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
		
		return new ResponseEntity<List<InfiniteHistory>>(stockService.getStockHistory(params), HttpStatus.OK);
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
		
		return new ResponseEntity<Boolean>(stockService.addStockHistory(params), HttpStatus.OK);
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
		
		return new ResponseEntity<Boolean>(stockService.updateStockHistory(params), HttpStatus.OK);
	}
	
	/**
	 * 대시보드 통계 
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/api/infinite/stock/statistics/{type}", method = RequestMethod.POST)
	public ResponseEntity<Object> getInfiniteStatistics(@PathVariable String type, @RequestBody Map<String, Object> params) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(params.get("accountId") != null) {
			if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt(params.get("accountId").toString())))
				throw new MyAccessDeniedException();
		}
		
		if(type.equals("profit-monthly"))
			return new ResponseEntity<Object>(dashboardService.getProfitMonthly(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("profit-stock"))
			return new ResponseEntity<Object>(dashboardService.getProfitStock(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		if(type.equals("buy-daily"))
			return new ResponseEntity<Object>(dashboardService.getBuyDaily(memberInfo.getMemberId(), params) ,HttpStatus.OK);
		
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
}
