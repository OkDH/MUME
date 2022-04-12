/**
 * 
 */
package com.ocko.aventador;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.InfiniteHistoryExample;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteListExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteHistoryMapper;
import com.ocko.aventador.dao.persistence.aventador.InfiniteIncomeMapper;
import com.ocko.aventador.dao.persistence.aventador.ViewInfiniteListMapper;
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
			example.setOrderByClause("trade_date asc, trade_type asc");
			infiniteDetail.setHistoryList(historyMapper.selectByExample(historyExample));
			
			
		}
	}
	
	
}
