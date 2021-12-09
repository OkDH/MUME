package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;

public class InfiniteDetail extends ViewInfiniteList {
	
	// 해당 심볼 주가 정보
	private StockDetail stockDetail;
	
	// 매수 정보 리스트
	private List<StockTradeInfo> buyTradeInfoList = new ArrayList<StockTradeInfo>();
	
	// 매도 정보 리스트
	private List<StockTradeInfo> sellTradeInfoList = new ArrayList<StockTradeInfo>();
	
	// 수수료율 (default 0.07%)
	private BigDecimal feesPer = new BigDecimal("0.0007");
	
	// 40분할, 1회 매수 금액
	public BigDecimal getOneBuySeed() {
		return getSeed().divide(new BigDecimal(40), 2, RoundingMode.DOWN);
	}
	
	// 1회 매수량
	public Integer getOneBuyQuantity() {
		if(stockDetail == null)
			return null;
		return getOneBuySeed().divide(stockDetail.getPriceClose().setScale(2, RoundingMode.HALF_UP), 0, RoundingMode.DOWN).intValue();
	}
	
	// 평가금액 : 종가 * 보유수량
	public BigDecimal getEvalPrice() {
		if(stockDetail == null)
			return null;
		return stockDetail.getPriceClose().setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(getHoldingQuantity()));
	}
	
	// 손익금 : 평가금액 - 매입금액 - 매수수수료 - 매도시 수수료
	public BigDecimal getIncome() {
		if(stockDetail == null)
			return null;
		// 매수 수수료(소수점 2자리에서 버림)
		BigDecimal buyFees = getBuyPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
		// 매도시 수수료(소수점 2자리에서 버림)
		BigDecimal sellFees = getEvalPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
		return getEvalPrice().subtract(getBuyPrice()).subtract(buyFees).subtract(sellFees);
	}
	
	// 손익률 : 손익금 / 매입금액 * 100
	public BigDecimal getIncomePer() {
		if(stockDetail == null)
			return null;
		return getIncome().divide(getBuyPrice(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}
	
	// 진행률 : 매입금액 / 배정시드 * 100
	public BigDecimal getProgressPer() {
		return getBuyPrice().divide(getSeed(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
	}

	/**
	 * @return {@link #stockDetail}
	 */
	public StockDetail getStockDetail() {
		return stockDetail;
	}

	/**
	 * @param stockDetail {@link #stockDetail}
	 */
	public void setStockDetail(StockDetail stockDetail) {
		this.stockDetail = stockDetail;
	}

	/**
	 * @return {@link #buyTradeInfoList}
	 */
	public List<StockTradeInfo> getBuyTradeInfoList() {
		return buyTradeInfoList;
	}

	/**
	 * @param buyTradeInfoList {@link #buyTradeInfoList}
	 */
	public void setBuyTradeInfoList(List<StockTradeInfo> buyTradeInfoList) {
		this.buyTradeInfoList = buyTradeInfoList;
	}

	/**
	 * @return {@link #sellTradeInfoList}
	 */
	public List<StockTradeInfo> getSellTradeInfoList() {
		return sellTradeInfoList;
	}

	/**
	 * @param sellTradeInfoList {@link #sellTradeInfoList}
	 */
	public void setSellTradeInfoList(List<StockTradeInfo> sellTradeInfoList) {
		this.sellTradeInfoList = sellTradeInfoList;
	}

	/**
	 * @return {@link #feesPer}
	 */
	public BigDecimal getFeesPer() {
		return feesPer;
	}

	/**
	 * @param feesPer {@link #feesPer}
	 */
	public void setFeesPer(BigDecimal feesPer) {
		this.feesPer = feesPer;
	}
	
	
}
