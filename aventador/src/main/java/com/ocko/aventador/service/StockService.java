/**
 * 
 */
package com.ocko.aventador.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocko.aventador.component.StockComponent;
import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.model.StockDetail;


/**
 * @author ok
 *
 */
@Service
public class StockService {
	
	@Autowired private StockComponent stockComponent;

	/**
	 * 하나의 심볼에 대해서 stock 정보 가져오기
	 * @param symbol
	 * @return
	 */
	public StockDetail getStock(String symbol) {
		return stockComponent.getStock(symbol);
	}
	
	/**
	 * 복수의 심볼에 대해서 stock 정보 가져오기
	 * @param symbols
	 * @return
	 */
	public Map<String, StockDetail> getStocks(String[] symbols) {
		return stockComponent.getStocks(symbols);
	}
	
	/**
	 * 3X ETFs 정보 가져오기
	 * @return
	 */
	public Map<String, StockDetail> getEtfStocks() {
		
		List<String> symbolList = new ArrayList<String>();
		for(EtfSymbol symbol : EtfSymbol.values()) {
			symbolList.add(symbol.name());
		}
		
		String[] symbols = symbolList.toArray(new String[symbolList.size()]);
		
		return stockComponent.getStocks(symbols);
	}
}
