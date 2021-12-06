package com.ocko.aventador.model;

import java.math.BigDecimal;

public class StockTradeInfo {

	// 매수 또는 매도 명(ex. LOC 매도 +5%)
	private String tradeName;
	// 단가
	private BigDecimal price;
	// 수량
	private Integer quantity;
	
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
