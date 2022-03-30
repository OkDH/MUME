package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ocko.aventador.constant.EtfSymbol;
import com.ocko.aventador.constant.InfiniteState;
import com.ocko.aventador.constant.InfiniteType;
import com.ocko.aventador.constant.TradeType;
import com.ocko.aventador.dao.model.aventador.InfiniteHistory;
import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;

public class InfiniteDetail extends ViewInfiniteList {
	
	// 해당 심볼 주가 정보
	private StockDetail stockDetail;
	
	// 매매 내역 리스트
	private List<InfiniteHistory> historyList = new ArrayList<InfiniteHistory>();
	
	// 매수 예약 정보 리스트
	private List<StockTradeInfo> buyTradeInfoList = new ArrayList<StockTradeInfo>();
	
	// 매도 예약 정보 리스트
	private List<StockTradeInfo> sellTradeInfoList = new ArrayList<StockTradeInfo>();
	
	// 평단가 변화 리스트
	private List<AveragePriceInfo> averagePriceList = new ArrayList<AveragePriceInfo>();
	
	// 종목 배정 시드
	public BigDecimal getSeed() {
		if(getIsKskyj() && getKskyjSeed() != null)
			return getKskyjSeed();
		else
			return super.getSeed();
	}
	
	// 수량
	public Integer getHoldingQuantity() {
		if(getIsKskyj() && getKskyjHoldingQuantity() != null)
			return getKskyjHoldingQuantity();
		else 
			return super.getHoldingQuantity();
	}
	
	// 계산용 수수료율 (수수료율 * 0.01)
	public BigDecimal getRealFeesPer() {
		if(getFeesPer() == null)
			return BigDecimal.ZERO;
		return getFeesPer().multiply(new BigDecimal("0.01"));
	}
	
	//1회 매수 금액
	public BigDecimal getOneBuySeed() {
		if(getDivisions() != null)
			return getSeed().divide(new BigDecimal(getDivisions()), 2, RoundingMode.DOWN);
		else 
			return BigDecimal.ZERO;
	}
	
	// 1회 매수량
	public Integer getOneBuyQuantity() {
		if(stockDetail == null)
			return 0;
		return getOneBuySeed().divide(stockDetail.getPriceClose().setScale(2, RoundingMode.HALF_UP), 0, RoundingMode.DOWN).intValue();
	}
	
	// 평가금액 : 종가 * 보유수량
	public BigDecimal getEvalPrice() {
		if(stockDetail == null)
			return BigDecimal.ZERO;
		return stockDetail.getPriceClose().setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal(getHoldingQuantity()));
	}
	
	// 평단가
	public BigDecimal getAveragePrice() {
		
		if(getIsKskyj() && getKskyjAveragePrice() != null)
			return getKskyjAveragePrice();
		
		if(averagePriceList.isEmpty())
			getAveragePriceList();
		
		return averagePriceList.get(averagePriceList.size()-1).getAveragePrice();
	}
	
	// 일별 평단가 리스트
	// 매수 : ((보유 평단가 * 보유수량) + (신규매수단가 * 신규매수수량)) / (보유수량 + 신규매수수량))
	// 매도 : 보유수량 - 매도수량 (평단가는 변화 없음)
	public List<AveragePriceInfo> getAveragePriceList(){
		averagePriceList.clear();
		
		BigDecimal avgPrice = BigDecimal.ZERO;
		BigDecimal holdingQuantity = BigDecimal.ZERO;
		
		// TQQQ, UPRO 1/13일 액면분할 적용 코드
		LocalDate splitDate = LocalDate.of(2022, 1, 13);
		// 1. 1월 13일 전에 시작했는지 체크
		boolean isCheckSplit = false;
		if((getSymbol().equals(EtfSymbol.TQQQ.name()) || getSymbol().equals(EtfSymbol.UPRO.name())) && 
			(getInfiniteState().equals(InfiniteState.ING) || getInfiniteState().equals(InfiniteState.STOP) || getInfiniteState().equals(InfiniteState.OUT)) &&
			getStartedDate().isBefore(splitDate))
			isCheckSplit = true;
		
		for(InfiniteHistory history : historyList) {
			
			if(history.getQuantity() == null || history.getQuantity() == 0) // 매매내역 수량이 0이면 skip
				continue;
			
			if(isCheckSplit && 
					(history.getTradeDate().equals(splitDate) || history.getTradeDate().isAfter(splitDate))) {
				// 2. 액면분할 2:1
				avgPrice = avgPrice.divide(new BigDecimal(2), 8, RoundingMode.HALF_EVEN);
				holdingQuantity = holdingQuantity.multiply(new BigDecimal(2));
				isCheckSplit = false;
			}
			
			if(history.getTradeType().equals(TradeType.BUY)) { // 매수
				// 보유 평단가 * 보유수량
				BigDecimal temp1 = avgPrice.multiply(holdingQuantity);
				// 신규매수단가 * 신규매수수량
				BigDecimal temp2 = history.getUnitPrice().multiply(new BigDecimal(history.getQuantity()));
				// 보유수량 + 신규매수수량
				holdingQuantity = holdingQuantity.add(new BigDecimal(history.getQuantity()));
				
				// 평단가
				if(holdingQuantity.compareTo(new BigDecimal("0.0")) == 0)
					avgPrice = (temp1.add(temp2)).divide(new BigDecimal("1.0"), 8, RoundingMode.HALF_EVEN);
				else
					avgPrice = (temp1.add(temp2)).divide(holdingQuantity, 8, RoundingMode.HALF_EVEN);
			} else if(history.getTradeType().equals(TradeType.SELL)) { // 매도
				holdingQuantity = holdingQuantity.subtract(new BigDecimal(history.getQuantity()));
			}
			
			// 일별 매매내역 추가
			if(!averagePriceList.isEmpty() && 
					averagePriceList.get(averagePriceList.size()-1).getTradeDate().equals(history.getTradeDate())) {
				// 매매일자가 같으면 마지막 평단가로 바꿔줌
				averagePriceList.get(averagePriceList.size()-1).setAveragePrice(avgPrice);
				averagePriceList.get(averagePriceList.size()-1).setTradeDate(history.getTradeDate());
				averagePriceList.get(averagePriceList.size()-1).setHoldingQuantity(holdingQuantity.intValue());
			} else {
				AveragePriceInfo averagePriceInfo = new AveragePriceInfo();
				averagePriceInfo.setAveragePrice(avgPrice);
				averagePriceInfo.setTradeDate(history.getTradeDate());
				averagePriceInfo.setHoldingQuantity(holdingQuantity.intValue());
				averagePriceList.add(averagePriceInfo);
			}
		}
		
		if(isCheckSplit) {
			// 액면분할 2:1
			avgPrice = avgPrice.divide(new BigDecimal(2), 8, RoundingMode.HALF_EVEN);
			holdingQuantity = holdingQuantity.multiply(new BigDecimal(2));
			averagePriceList.get(averagePriceList.size()-1).setAveragePrice(avgPrice);
			averagePriceList.get(averagePriceList.size()-1).setTradeDate(historyList.get(historyList.size()-1).getTradeDate());
			averagePriceList.get(averagePriceList.size()-1).setHoldingQuantity(holdingQuantity.intValue());
		}
		
		setHoldingQuantity(holdingQuantity.intValue());
		return averagePriceList;
	}
	
	// 매입금액
	// 평단가 * 보유수량
	public BigDecimal getBuyPrice() {
		if(getIsKskyj() && getKskyjBuyPrice() != null)
			return getKskyjBuyPrice();
		return getAveragePrice().multiply(new BigDecimal(getHoldingQuantity()));
	}
	
	// 손익금 
	// 매도완료일 때 : 전체 매도금액 - 전체 매수금액 - 수수료
	// 나머지 : 평가금액 - 매입금액 - 수수료
	public BigDecimal getIncome() {
		if(getInfiniteState().equals(InfiniteState.DONE) || getBuyPrice().compareTo(new BigDecimal("0.0")) == 0) {
			
			if(getTotalBuyPrice() == null)
				return BigDecimal.ZERO;
			if(getTotalSellPrice() == null)
				return BigDecimal.ZERO;
			
			// 수수료(소수점 2자리에서 버림) : (총매수금액 + 총매도금액) / 수수료율
			BigDecimal fees = getTotalBuyPrice().add(getTotalSellPrice()).multiply(getRealFeesPer()).setScale(2, RoundingMode.DOWN);
			return getTotalSellPrice().subtract(getTotalBuyPrice()).subtract(fees);
		} else {
			// 수수료(소수점 2자리에서 버림) : (매입금 + 평가금) / 수수료율
			BigDecimal fees = getBuyPrice().add(getEvalPrice()).multiply(getRealFeesPer()).setScale(2, RoundingMode.DOWN);
			return getEvalPrice().subtract(getBuyPrice()).subtract(fees);
		}
	}
	
	// 손익률
	// 매도완료일 때 : 손익금 / 전체매입금액 * 100
	// 나머지 : 손익금 / 매입금액 * 100
	public BigDecimal getIncomePer() {
		if(getInfiniteState().equals(InfiniteState.DONE) || getBuyPrice().compareTo(new BigDecimal("0.0")) == 0) {
			
			if(getTotalBuyPrice() == null || getTotalBuyPrice().compareTo(BigDecimal.ZERO) == 0)
				return BigDecimal.ZERO;
			
			return getIncome().divide(getTotalBuyPrice(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
		} else {
			return getIncome().divide(getBuyPrice(), 8, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
		}
	}
	
	// 진행률
	// 매도완료일 때 : 전체매입금 / 배정시드 * 100 ( 100%가 큰 경우 return 100)
	// 나머지 : 매입금액 / 배정시드 * 100
	public BigDecimal getProgressPer() {
		
		if(getSeed().compareTo(BigDecimal.ZERO) == 0)
			return new BigDecimal("100.0");
		
		if(getInfiniteState().equals(InfiniteState.DONE) || getBuyPrice().compareTo(new BigDecimal("0.0")) == 0) {
			
			if(getTotalBuyPrice() == null || getTotalBuyPrice().compareTo(BigDecimal.ZERO) == 0)
				return BigDecimal.ZERO;
			
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
	 * @return {@link #historyList}
	 */
	public List<InfiniteHistory> getHistoryList() {
		return historyList;
	}

	/**
	 * @param historyList {@link #historyList}
	 */
	public void setHistoryList(List<InfiniteHistory> historyList) {
		this.historyList = historyList;
	}
	
}
