package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitMonthly;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteProfitStock;

public class ProfitStockDetail extends ViewInfiniteProfitStock {

	// 수수료율 (default 0.07%)
	private BigDecimal feesPer = new BigDecimal("0.0007");
	
	// 손익금 
	// 전체 매도금액 - 전체 매수금액 - 매수수수료 - 매도수수료
	public BigDecimal getIncome() {
		// 매수 수수료(소수점 2자리에서 버림)
		BigDecimal buyFees = getTotalBuyPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
		// 매도시 수수료(소수점 2자리에서 버림)
		BigDecimal sellFees = getTotalSellPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
		return getTotalSellPrice().subtract(getTotalBuyPrice()).subtract(buyFees).subtract(sellFees);
	}
	
	// 손익률
	// 손익금 / 전체매수금액 * 100
	public BigDecimal getIncomePer() {
		return getIncome().divide(getTotalBuyPrice(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}

	public BigDecimal getFeesPer() {
		return feesPer;
	}

	public void setFeesPer(BigDecimal feesPer) {
		this.feesPer = feesPer;
	}
	
}
