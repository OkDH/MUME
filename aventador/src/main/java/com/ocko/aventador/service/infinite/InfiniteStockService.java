package com.ocko.aventador.service.infinite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.dao.model.aventador.InfiniteStock;
import com.ocko.aventador.dao.model.aventador.InfiniteStockExample;
import com.ocko.aventador.dao.persistence.aventador.InfiniteStockMapper;

@Service
public class InfiniteStockService {

	@Autowired private InfiniteStockMapper infiniteStockMapper;
	
	/**
	 * 무매 종목리스트 조회
	 * @param infiniteStockExample
	 * @return
	 */
	public List<InfiniteStock> getStocks(InfiniteStockExample infiniteStockExample){
		return infiniteStockMapper.selectByExample(infiniteStockExample);
	}
}
