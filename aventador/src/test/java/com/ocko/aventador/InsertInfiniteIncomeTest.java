/**
 * 
 */
package com.ocko.aventador;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteHistoryExample;
import com.ocko.aventador.dao.model.aventador.InfiniteIncome;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteIncomeMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
import com.ocko.aventador.model.AveragePriceInfo;
import com.ocko.aventador.model.InfiniteDetail;

/**
 * @author ok
 *
 */
@SpringBootTest
public class InsertInfiniteIncomeTest {
	
	
	private static final Logger log = LoggerFactory.getLogger(InsertInfiniteIncomeTest.class);


	@Autowired
	private ViewInfiniteListMapper viewInfiniteMapper;
	@Autowired
	private InfiniteHistoryMapper historyMapper;
	@Autowired
	private InfiniteIncomeMapper incomeMapper;
	
	@Test
	@Transactional
	@Rollback(false)
	public void start() {
		ViewInfiniteListExample example = new ViewInfiniteListExample();
		example.createCriteria().andAccountIdEqualTo(1);
		
		example.setOrderByClause("started_date asc");
		
		for(ViewInfiniteList viewInfinite : viewInfiniteMapper.cursorByExample(example)) {
			log.info(viewInfinite.getInfiniteId() + "." + viewInfinite.getSymbol());
			
			InfiniteDetail infiniteDetail = new InfiniteDetail();
			// 객체복사
			BeanUtils.copyProperties(viewInfinite, infiniteDetail);
			
			// 매매내역
			InfiniteHistoryExample historyExample = new InfiniteHistoryExample();
			historyExample.createCriteria().andInfiniteIdEqualTo(viewInfinite.getInfiniteId()).andIsDeletedEqualTo(false);
			historyExample.setOrderByClause("trade_date asc, trade_type asc");
			List<InfiniteHistory> historyList = historyMapper.selectByExample(historyExample);

			// 매도 일때마다 평단가 계산 후 손익현황 추가
			for(int i = 0; i < historyList.size(); i++) {
				if(i == 0) 
					continue;
				if(historyList.get(i).getQuantity() == 0) 
					continue;
					
				// 매도일 경우
				if(historyList.get(i).getTradeType().equals(TradeType.SELL)) {
					// 바로 전 매매내역까지만 history 추가
					infiniteDetail.setHistoryList(new ArrayList<>(historyList.subList(0, i)));
					
					// 마지막 매매 후 평단가 조회
					List<AveragePriceInfo> averagePriceList = infiniteDetail.getAveragePriceList();
					AveragePriceInfo averagePriceInfo = infiniteDetail.getAveragePriceList().get(averagePriceList.size()-1);
					
					// 매수금액
					BigDecimal buyPrice = averagePriceInfo.getAveragePrice().multiply(new BigDecimal(historyList.get(i).getQuantity()));
					// 매도금액
					BigDecimal sellPrice = historyList.get(i).getUnitPrice().multiply(new BigDecimal(historyList.get(i).getQuantity()));
					
					// 수수료(소수점 2자리에서 버림) : (매수금액 + 매도금액) / 수수료율
					BigDecimal fees = buyPrice.add(sellPrice).multiply(infiniteDetail.getRealFeesPer()).setScale(2, RoundingMode.DOWN);
					// 손익금
					BigDecimal income = sellPrice.subtract(buyPrice).subtract(fees);
					// 손익률
					BigDecimal incomePer = income.divide(buyPrice, 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
					
					// 진행률
					BigDecimal progressPer = buyPrice.divide(infiniteDetail.getSeed(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
					
					// 손익현황 추가
					InfiniteIncome infiniteIncome = new InfiniteIncome();
					infiniteIncome.setAccountId(infiniteDetail.getAccountId());
					infiniteIncome.setInfiniteId(infiniteDetail.getInfiniteId());
					infiniteIncome.setInfiniteHistoryId(historyList.get(i).getInfiniteHistoryId());
					infiniteIncome.setSymbol(infiniteDetail.getSymbol()); // 삭제될수도
					//infiniteIncome.setSeed(infiniteDetail.getSeed()); // numeric(17,8)수정
					infiniteIncome.setStartedDate(infiniteDetail.getStartedDate()); // 삭제될수도
					infiniteIncome.setDoneDate(historyList.get(i).getTradeDate()); // sellDate로 수정
					infiniteIncome.setProgressPer(progressPer);
					infiniteIncome.setAveragePrice(averagePriceInfo.getAveragePrice());
					infiniteIncome.setBuyPrice(buyPrice);
					infiniteIncome.setSellPrice(sellPrice);
					infiniteIncome.setIncome(income);
					//infiniteIncome.setIncomePer(incomePer); // income_per 추가
					infiniteIncome.setFees(fees);
					infiniteIncome.setRegisteredDate(LocalDateTime.now());
					infiniteIncome.setIsDeleted(false);
					
					incomeMapper.insert(infiniteIncome);
				}
			}
			
		}
	}
	
	
}
