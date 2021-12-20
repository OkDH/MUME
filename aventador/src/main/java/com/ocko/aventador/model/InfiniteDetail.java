package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.ocko.aventador.constant.InfiniteState;
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
	
	// 손익금 
	// 매도완료일 때 : 전체 매도금액 - 전체 매수금액 - 매수수수료 - 매도수수료
	// 나머지 : 평가금액 - 매입금액 - 매수수수료 - 매도수수료
	public BigDecimal getIncome() {
		if(getInfiniteState().equals(InfiniteState.DONE) || getBuyPrice().compareTo(new BigDecimal("0.0")) == 0) {
			// 매수 수수료(소수점 2자리에서 버림)
			BigDecimal buyFees = getTotalBuyPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
			// 매도시 수수료(소수점 2자리에서 버림)
			BigDecimal sellFees = getTotalSellPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
			return getTotalSellPrice().subtract(getTotalBuyPrice()).subtract(buyFees).subtract(sellFees);
		} else {
			// 매수 수수료(소수점 2자리에서 버림)
			BigDecimal buyFees = getBuyPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
			// 매도시 수수료(소수점 2자리에서 버림)
			BigDecimal sellFees = getEvalPrice().multiply(feesPer).setScale(2, RoundingMode.DOWN);
			return getEvalPrice().subtract(getBuyPrice()).subtract(buyFees).subtract(sellFees);
		}
	}
	
	// 손익률
	// 매도완료일 때 : 손익금 / 전체매입금액 * 100
	// 나머지 : 손익금 / 매입금액 * 100
	public BigDecimal getIncomePer() {
		if(getInfiniteState().equals(InfiniteState.DONE) || getBuyPrice().compareTo(new BigDecimal("0.0")) == 0) {
			return getIncome().divide(getTotalBuyPrice(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
		} else {
			return getIncome().divide(getBuyPrice(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
		}
	}
	
	// 진행률
	// 매도완료일 때 : 전체매입금 / 배정시드 * 100 ( 100%가 큰 경우 return 100)
	// 나머지 : 매입금액 / 배정시드 * 100
	public BigDecimal getProgressPer() {
		if(getInfiniteState().equals(InfiniteState.DONE) || getBuyPrice().compareTo(new BigDecimal("0.0")) == 0) {
			return getTotalBuyPrice().divide(getSeed(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).min(new BigDecimal("100"));
		} else {
			return getBuyPrice().divide(getSeed(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
		}
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
