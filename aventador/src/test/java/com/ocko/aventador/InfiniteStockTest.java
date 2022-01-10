/**
 * 
 */
package com.ocko.aventador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.service.infinite.InfiniteStockService;

/**
 * @author ok
 *
 */
@SpringBootTest
public class InfiniteStockTest {
	
	
	@Autowired private InfiniteStockService infiniteStockService;
	
	@Test
	public void getStocks() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule()); // 추가
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 추가
        
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accountId", 179);
		List<InfiniteDetail> list = infiniteStockService.getStocks(157, params);
		
//		params.put("accountId", 1);
//		List<InfiniteDetail> list = infiniteStockService.getStocks(1, params);
		
		for(InfiniteDetail detail : list) {
			System.out.println(objectMapper.writeValueAsString(detail));
			
//			System.out.println(detail.getInfiniteId());
//			System.out.println(detail.getIncomePer());
		}
	}

}
