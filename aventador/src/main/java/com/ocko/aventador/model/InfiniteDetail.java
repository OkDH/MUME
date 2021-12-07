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
	
	// 평가금액 : 종가 * 보유수량
	public BigDecimal getEvalPrice() {
		if(stockDetail == null)
			return null;
		return stockDetail.getPriceClose().multiply(new BigDecimal(getHoldingQuantity()));
	}
	
	// 손익금 : 평가금액 - 매입금액 - 수수료
	public BigDecimal getIncome() {
		if(stockDetail == null)
			return null;
		return getEvalPrice().subtract(getBuyPrice()).subtract(getFees());
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
	
}
