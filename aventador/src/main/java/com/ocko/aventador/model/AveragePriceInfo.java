package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 일별 평단가 모델
 * @author ocko1
 *
 */
public class AveragePriceInfo {

	private LocalDate tradeDate;
	private BigDecimal averagePrice;
	private Integer holdingQuantity;
	
	public LocalDate getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(LocalDate tradeDate) {
		this.tradeDate = tradeDate;
	}
	public BigDecimal getAveragePrice() {
		return averagePrice;
	}
	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}
	public Integer getHoldingQuantity() {
		return holdingQuantity;
	}
	public void setHoldingQuantity(Integer holdingQuantity) {
		this.holdingQuantity = holdingQuantity;
	}
	
}
