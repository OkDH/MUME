/**
 * 
 */
package com.ocko.aventador.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ocko.aventador.helper.StockHelper;
import com.ocko.aventador.model.StockDetail;


/**
 * @author ok
 *
 */
@Service
public class StockService {

	public StockDetail getStock(String symbol) {
		return StockHelper.getStock(symbol);
	}
	
	public Map<String, StockDetail> getStocks(String[] symbols) {
		return StockHelper.getStocks(symbols);
	}
}
