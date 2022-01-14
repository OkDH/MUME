package com.ocko.aventador.service.infinite;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.dao.model.aventador.InfiniteAccount;
import com.ocko.aventador.dao.model.aventador.InfiniteAccountExample;
import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteAccountMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;

@Service
public class InfiniteAccountService {

	@Autowired private InfiniteAccountMapper infiniteAccountMapper;
	@Autowired private InfiniteStockMapper infiniteStockMapper;
	
	/**
	 * 내 계좌 리스트 불러오기
	 * 내 계좌가 없다면 하나 생성
	 * @param memberId
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public List<InfiniteAccount> getMyAccounts(int memberId) throws Exception {
		InfiniteAccountExample example = new InfiniteAccountExample();
		example.createCriteria().andMemberIdEqualTo(memberId).andIsDeletedEqualTo(false);
		example.setOrderByClause("account_order asc");
		
		List<InfiniteAccount> myAccounts = infiniteAccountMapper.selectByExample(example);
		
		// 신규 계좌 생성
		if(myAccounts.isEmpty()) {
			InfiniteAccount infiniteAccount = new InfiniteAccount();
			infiniteAccount.setMemberId(memberId);
			infiniteAccount.setAccountOrder(1);
			infiniteAccount.setAccountAlias("MUME계좌");
			infiniteAccount.setFeesPer(new BigDecimal("0.07"));
			infiniteAccount.setSeed(BigDecimal.ZERO);
			infiniteAccount.setIsDeleted(false);
			infiniteAccount.setRegisteredDate(LocalDateTime.now());
			infiniteAccountMapper.insert(infiniteAccount);
			
			myAccounts.add(infiniteAccount);
		}
		
		return myAccounts;
	}
	
	/**
	 * 계좌 권한 확인
	 * @param memberId
	 * @param accountId
	 * @return
	 */
	public boolean isMyAccount(int memberId, int accountId) {
		InfiniteAccountExample example = new InfiniteAccountExample();
		example.createCriteria().andMemberIdEqualTo(memberId).andAccountIdEqualTo(accountId);
		List<InfiniteAccount> myAccount = infiniteAccountMapper.selectByExample(example);
		if(myAccount.isEmpty())
			return false;
		return true;
	}

	/**
	 * 계좌 추가
	 * @param params
	 * @return
	 */
	public Boolean addAccount(int memberId, Map<String, Object> params) {
		InfiniteAccount infiniteAccount = new InfiniteAccount();
		infiniteAccount.setMemberId(memberId);
		infiniteAccount.setAccountOrder(Integer.parseInt(params.get("accountOrder").toString()));
		infiniteAccount.setAccountAlias(params.get("accountAlias").toString());
		infiniteAccount.setFeesPer(new BigDecimal(params.get("feesPer").toString()));
		infiniteAccount.setSeed(new BigDecimal(params.get("seed").toString()));
		infiniteAccount.setIsDeleted(false);
		infiniteAccount.setRegisteredDate(LocalDateTime.now());
		infiniteAccountMapper.insert(infiniteAccount);
		return true;
	}
	
	/**
	 * 계좌 변경
	 * @param params
	 * @return
	 */
	public Boolean updateAccount(int memberId, Map<String, Object> params) {
		InfiniteAccount infiniteAccount = new InfiniteAccount();
		infiniteAccount.setMemberId(memberId);
		
		if(params.get("accountAlias") != null) {
			infiniteAccount.setAccountAlias(params.get("accountAlias").toString());
		}
		
		if(params.get("accountOrder") != null) {
			infiniteAccount.setAccountOrder(Integer.parseInt(params.get("accountOrder").toString()));
		}
		
		if(params.get("seed") != null) {
			infiniteAccount.setSeed(new BigDecimal(params.get("seed").toString()));
		}
		
		if(params.get("feesPer") != null) {
			infiniteAccount.setFeesPer(new BigDecimal(params.get("feesPer").toString()));
		}
		
		if(params.get("isDeleted") != null) {
			infiniteAccount.setIsDeleted(Boolean.parseBoolean(params.get("isDeleted").toString()));
		}
		
		infiniteAccount.setUpdatedDate(LocalDateTime.now());
		
		InfiniteAccountExample example = new InfiniteAccountExample();
		example.createCriteria().andMemberIdEqualTo(memberId)
			.andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
		
		infiniteAccountMapper.updateByExampleSelective(infiniteAccount, example);
		
		// 계좌 삭제일 경우 종목도 삭제
		if(params.get("isDeleted") != null) {
			if(Boolean.parseBoolean(params.get("isDeleted").toString())) {
				InfiniteStock stock = new InfiniteStock();
				stock.setIsDeleted(true);
				
				InfiniteStockExample stockExample = new InfiniteStockExample();
				stockExample.createCriteria().andAccountIdEqualTo(Integer.parseInt(params.get("accountId").toString()));
				
				infiniteStockMapper.updateByExampleSelective(stock, stockExample);
			}
		}
		
		return true;
	}
}
