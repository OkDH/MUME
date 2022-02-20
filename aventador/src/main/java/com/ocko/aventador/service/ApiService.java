package com.ocko.aventador.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.SimpleTokenComponent;
import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.constant.InfiniteType;
import com.ocko.aventador.constant.RegisteredType;
import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteAccount;
import com.ocko.aventador.dao.model.aventador.InfiniteAccountExample;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample.Criteria;
import com.ocko.aventador.dao.model.aventador.MemberSetting;
import com.ocko.aventador.dao.model.aventador.MemberSettingExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteAccountMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;
import com.ocko.aventador.dao.persistence.aventador.MemberSettingMapper;
import com.ocko.aventador.model.api.ResponseDto;

@Service
public class ApiService {

	@Autowired private SimpleTokenComponent simpleTokenComponent;
	@Autowired private MemberSettingMapper memberSettingMapper;
	@Autowired private InfiniteAccountMapper infiniteAccountMapper;
	@Autowired private InfiniteStockMapper infiniteStockMapper;
	@Autowired private InfiniteHistoryMapper infiniteHistoryMapper;
	
	/**
	 * 지니 프로그램 연동 API key 가져오기
	 * @param memberId
	 * @return
	 */
	public String getApiKey(Integer memberId) {
		MemberSetting memberSetting = memberSettingMapper.selectByPrimaryKey(memberId);
		
		if(memberSetting.getApiKey() != null)
			return memberSetting.getApiKey();
		
		String apiKey = simpleTokenComponent.generatorToken(memberId);
		
		MemberSetting updateSetting = new MemberSetting();
		updateSetting.setMemberId(memberId);
		updateSetting.setApiKey(apiKey);
		memberSettingMapper.updateByPrimaryKeySelective(updateSetting);
		
		return apiKey;
	}
	
	/**
	 * 지니 프로그램 데이터 업데이트
	 * @param params
	 * @return
	 */
	public ResponseDto updateKskyjData(Map<String, Object> params) {
		ResponseDto response = new ResponseDto();
		response.setState("fail");
		
		if(params.get("api_key") == null) {
			response.setMessage("API Key를 확인하세요.");
			return response;
		}
		
		// api key 확인
		MemberSettingExample settingExample = new MemberSettingExample();
		settingExample.createCriteria().andApiKeyEqualTo(params.get("api_key").toString());
		List<MemberSetting> member = memberSettingMapper.selectByExample(settingExample);
		if(member.isEmpty()) {
			response.setMessage("유효하지 않은 API Key 입니다.");
			return response;
		}
		
		// 계좌
		if(params.get("accounts") != null) {
			
			List<Map<String, Object>> accountList = (List<Map<String, Object>>) params.get("accounts");
			
			for(Map<String, Object> account : accountList) {
				// 계좌 조회 
				// 계좌 순서(order 매칭)
				InfiniteAccountExample accountExample = new InfiniteAccountExample();
				accountExample.createCriteria().andMemberIdEqualTo(member.get(0).getMemberId())
					.andIsDeletedEqualTo(false)
					.andAccountOrderEqualTo(Integer.parseInt(account.get("account_order").toString()));
				
				List<InfiniteAccount> infiniteAccountList = infiniteAccountMapper.selectByExample(accountExample);
				
				if(!infiniteAccountList.isEmpty()) {
					
					int accountId = infiniteAccountList.get(0).getAccountId();
					
					// 계좌 전체 시드
					if(account.get("total_seed") != null) {
						InfiniteAccount updateAccount = new InfiniteAccount();
						updateAccount.setAccountId(accountId);
						updateAccount.setKskyjSeed(new BigDecimal(account.get("total_seed").toString()));
						updateAccount.setKskyjUpdateDate(LocalDateTime.now());
						infiniteAccountMapper.updateByPrimaryKeySelective(updateAccount);
					}
					
					// 계좌 내 종목
					if(params.get("balances") != null) {
						List<Map<String, Object>> balances = (List<Map<String, Object>>) account.get("balances");
						
						for(Map<String, Object> balance : balances) {
							if(balance.get("ticker") != null && balance.get("infinite_buying_type") != null
									&& balance.get("principal") != null && balance.get("purchase_amount") != null
									&& balance.get("cost_price") != null && balance.get("holding_quantity") != null) { 
								
								// 종목 업데이트
								Integer infiniteId = upsertInfiniteStock(accountId, balance);
							}
						}
					}
				}
			}
			
		}
		
		response.setState("success");
		response.setMessage("데이터 업데이트 완료");
		
		return response;
	}
	
	/**
	 * 종목 업데이트
	 * @param accountId
	 * @param balance
	 * @return InfiniteId
	 */
	private Integer upsertInfiniteStock(Integer accountId, Map<String, Object> balance) {
		
		String symbol = balance.get("ticker").toString();
		String version = balance.get("infinite_buying_type").toString();
		String type = InfiniteType.INFINITE;
		
		InfiniteStockExample stockExample = new InfiniteStockExample();
		Criteria stockCreiteria =  stockExample.createCriteria().andAccountIdEqualTo(accountId)
				.andIsDeletedEqualTo(false).andInfiniteStateNotEqualTo(InfiniteState.DONE)
				.andSymbolEqualTo(symbol);
		
		// 무매 버전
		if(version.equals("v1") || version.equals("v2") || version.equals("v2.1") || version.equals("v2.1후반")) {
			stockCreiteria.andInfiniteVersionEqualTo(version);
		} else if(version.startsWith("TLP_")) { // TLP
			stockCreiteria.andInfiniteTypeEqualTo(InfiniteType.TLP);
			version = version.replace("TLP_", "");
			type = InfiniteType.TLP;
			stockCreiteria.andInfiniteVersionEqualTo(version);
		}
		
		InfiniteStock infiniteStock = new InfiniteStock();
		infiniteStock.setKskyjSeed(new BigDecimal(balance.get("principal").toString())); // 종목 원금
		infiniteStock.setKskyjBuyPrice(new BigDecimal(balance.get("purchase_amount").toString())); // 매입금액
		infiniteStock.setKskyjAveragePrice(new BigDecimal(balance.get("cost_price").toString())); // 평단가
		infiniteStock.setKskyjHoldingQuantity(Integer.parseInt(balance.get("holding_quantity").toString())); // 보유수량
		infiniteStock.setKskyjUpdateDate(LocalDateTime.now());
		
		// 원금소진 확인
		if(infiniteStock.getKskyjBuyPrice().compareTo(infiniteStock.getKskyjSeed()) >= 0) {
			infiniteStock.setInfiniteState(InfiniteState.OUT);
		} else if(balance.get("is_buying") != null) { // 매수 중지 상태
			String isBuying = balance.get("is_buying").toString();
			if(isBuying.equals("true"))
				infiniteStock.setInfiniteState(InfiniteState.ING);
			else if(isBuying.equals("false"))
				infiniteStock.setInfiniteState(InfiniteState.STOP);
		}
		
		// 종목 조회
		List<InfiniteStock> stock = infiniteStockMapper.selectByExample(stockExample);
		if(!stock.isEmpty()) {
			if(stock.size() == 1) { // 기존 종목에 업데이트
				infiniteStock.setInfiniteId(stock.get(0).getInfiniteId());
				infiniteStockMapper.updateByPrimaryKeySelective(infiniteStock);
			} else {
				// TODO : 중복 종목 처리
			}
		} else { // 신규 추가
			infiniteStock.setAccountId(accountId);
			infiniteStock.setSymbol(symbol);
			infiniteStock.setSeed(infiniteStock.getKskyjSeed());
			infiniteStock.setInfiniteVersion(version);
			infiniteStock.setInfiniteType(type);
			infiniteStock.setStartedDate(LocalDate.now().minusDays(1));
			infiniteStock.setIsDeleted(false);
			infiniteStock.setRegisteredDate(infiniteStock.getKskyjUpdateDate());
			
			infiniteStockMapper.insert(infiniteStock);
			
			// 매매 내역 추가
			InfiniteHistory history = new InfiniteHistory();
			history.setInfiniteId(infiniteStock.getInfiniteId());
			history.setTradeDate(infiniteStock.getStartedDate());
			history.setTradeType(TradeType.BUY);
			
			history.setUnitPrice(infiniteStock.getKskyjAveragePrice());
			history.setQuantity(infiniteStock.getKskyjHoldingQuantity());
			
			history.setRegisteredType(RegisteredType.KSKYJ.name());
			history.setRegisteredDate(LocalDateTime.now());
			history.setIsDeleted(false);
			infiniteHistoryMapper.insert(history);
		}
		
		return infiniteStock.getInfiniteId();
	}
}
