package com.ocko.aventador.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ocko.aventador.dao.model.aventador.InfiniteAccount;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.MemberInfo;
import com.ocko.aventador.exception.MyAccessDeniedException;
import com.ocko.aventador.exception.MyArgumentException;
import com.ocko.aventador.service.AuthenticationService;
import com.ocko.aventador.service.infinite.InfiniteAccountService;
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
	
	@RequestMapping(value = "/api/infinite/stocks", method = RequestMethod.GET)
	public @ResponseBody List<InfiniteStock> getMyStocks(@RequestBody Map<String, Object> param) {
		MemberInfo memberInfo = authenticationService.getCurrentMember();
		if(memberInfo == null)
			return null;
		if(param.get("accountId") == null)
			throw new MyArgumentException();
		if(!accountService.isMyAccount(memberInfo.getMemberId(), Integer.parseInt((String) param.get("accountId"))))
			throw new MyAccessDeniedException();
		
		return null;
	}
}
