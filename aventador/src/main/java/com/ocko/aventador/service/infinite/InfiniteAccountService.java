package com.ocko.aventador.service.infinite;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.constant.AccountType;
import com.ocko.aventador.dao.model.aventador.InfiniteAccount;
import com.ocko.aventador.dao.model.aventador.InfiniteAccountExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteAccountMapper;

@Service
public class InfiniteAccountService {

	@Autowired InfiniteAccountMapper infiniteAccountMapper;
	
	/**
	 * 내 계좌 리스트 불러오기
	 * 내 계좌가 없다면 하나 생성
	 * @param memberId
	 * @return
	 */
	public List<InfiniteAccount> getMyAccounts(int memberId){
		InfiniteAccountExample example = new InfiniteAccountExample();
		example.createCriteria().andMemberIdEqualTo(memberId);
		example.setOrderByClause("account_order asc");
		
		List<InfiniteAccount> myAccounts = infiniteAccountMapper.selectByExample(example);
		
		// 신규 계좌 생성
		if(myAccounts.isEmpty()) {
			InfiniteAccount infiniteAccount = new InfiniteAccount();
			infiniteAccount.setMemberId(memberId);
			infiniteAccount.setAccountOrder(1);
			infiniteAccount.setAccountAlias("무한매수 계좌 1");
			infiniteAccount.setAccountType(AccountType.INFINITE.name());
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
}
