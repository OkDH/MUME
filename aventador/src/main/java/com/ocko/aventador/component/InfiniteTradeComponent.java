package com.ocko.aventador.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ocko.aventador.constant.ConcludeType;
import com.ocko.aventador.constant.InfiniteType;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.model.StockTradeInfo;

@Component
public class InfiniteTradeComponent {
	
	// 수수료 포함 10% 계산용
	private static String TEN_PER = "1.1017";
	// 수수료 포함 5% 계산용
	private static String FIVE_PER = "1.0517";

	/**
	 * 매수 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	public List<StockTradeInfo> getBuyInfo(InfiniteDetail infiniteDetail) {
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		if(infiniteDetail.getInfiniteType().equals(InfiniteType.V2_1)) {
			tradeInfoList = getBuyInfoV2(infiniteDetail);
		} else if(infiniteDetail.getInfiniteType().equals(InfiniteType.V2)) {
			tradeInfoList = getBuyInfoV2(infiniteDetail);
		} else if(infiniteDetail.getInfiniteType().equals(InfiniteType.V1)) {
			tradeInfoList = getBuyInfoV1(infiniteDetail);
		}
		
		return tradeInfoList;
	}
	
	/**
	 * 매도 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	public List<StockTradeInfo> getSellInfo(InfiniteDetail infiniteDetail) {
		if(infiniteDetail == null)
			return null;
		if(infiniteDetail.getStockDetail() == null)
			return null;
		
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		if(infiniteDetail.getInfiniteType().equals(InfiniteType.V2_1)) {
			tradeInfoList = getSellInfoV2_1(infiniteDetail);
		} else if(infiniteDetail.getInfiniteType().equals(InfiniteType.V2)) {
			tradeInfoList = getSellInfoV2(infiniteDetail);
		} else if(infiniteDetail.getInfiniteType().equals(InfiniteType.V1)) {
			tradeInfoList = getSellInfoV1(infiniteDetail);
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v2_1 매도 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getSellInfoV2_1(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		int quantity = (int) Math.round(infiniteDetail.getHoldingQuantity()/4.0);
		
		// 진행률 
		if(infiniteDetail.getProgressPer().floatValue() < 50) { // 전반전
			
			{
				// LOC 매도 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 매도 (+5%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(FIVE_PER));
				info.setPrice(price);
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 + 10%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price);
				info.setQuantity(infiniteDetail.getHoldingQuantity() - quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		} else { // 후반전
			
			{
				// LOC 매도 +0%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 매도 (+0%)");
				info.setPrice(infiniteDetail.getAveragePrice());
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+5%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(FIVE_PER));
				info.setPrice(price);
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 +10%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price);
				info.setQuantity(infiniteDetail.getHoldingQuantity() - quantity - quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v2 매도 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getSellInfoV2(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		// 진행률 
		if(infiniteDetail.getProgressPer().floatValue() < 50) { // 전반전
			{
				// 지정가 매도
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price);
				info.setQuantity(infiniteDetail.getHoldingQuantity());
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		} else { // 후반전
			
			int quantity = (int) Math.round(infiniteDetail.getHoldingQuantity()/2.0);
			
			{
				// 지정가 매도 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+5%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price);
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 +10%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price);
				info.setQuantity(infiniteDetail.getHoldingQuantity() - quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v2.1 매도 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getSellInfoV1(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		{
			// 지정가 매도
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("지정가 매도 (+10%)");
			BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
			info.setPrice(price);
			info.setQuantity(infiniteDetail.getHoldingQuantity());
			info.setConcludeType(ConcludeType.PENDING_ORDER);
			tradeInfoList.add(info);
		}
		return tradeInfoList;
	}
	
	/**
	 * v2 매수 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getBuyInfoV2(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		// 0.5회 매수 시드
		BigDecimal oneBuySeed = infiniteDetail.getSeed().divide(new BigDecimal(80), 2, RoundingMode.FLOOR);
		// 현재가 +15%
		BigDecimal nowUpPrice = infiniteDetail.getStockDetail().getPriceClose().multiply(new BigDecimal("1.15"));
		
		// 진행률 
		if(infiniteDetail.getProgressPer().floatValue() < 50) { // 전반전
			{
				// LOC 평단매수
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 평단매수");
				
				// 현재가 +15% 와 평단가와 비교해서 작은값
				BigDecimal price = infiniteDetail.getAveragePrice().min(nowUpPrice);
				info.setPrice(price);
				
				info.setQuantity(oneBuySeed.divide(price, 0, RoundingMode.FLOOR).intValue());
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
			{
				// LOC 큰수매수 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 큰수매수 (+5%)");
				
				// 현재가 +15% 와 평단가+5% 와 비교해서 작은값
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal("1.05"));
				info.setPrice(price.min(nowUpPrice));
				
				info.setQuantity(oneBuySeed.divide(price, 0, RoundingMode.FLOOR).intValue());
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
		} else { // 후반전
			{
				// LOC 평단매수
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 평단매수");
				
				// 현재가 +15% 와 평단가와 비교해서 작은값
				BigDecimal price = infiniteDetail.getAveragePrice().min(nowUpPrice);
				info.setPrice(price);
				
				info.setQuantity(oneBuySeed.multiply(new BigDecimal(2)).divide(price, 0, RoundingMode.FLOOR).intValue());
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v1 매수 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getBuyInfoV1(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		// 0.5회 매수 시드
		BigDecimal oneBuySeed = infiniteDetail.getSeed().divide(new BigDecimal(80), 2, RoundingMode.FLOOR);
		// 현재가 +15%
		BigDecimal nowUpPrice = infiniteDetail.getStockDetail().getPriceClose().multiply(new BigDecimal("1.15"));
				
		{
			// LOC 평단매수
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("LOC 평단매수");
			
			// 현재가 +15% 와 평단가와 비교해서 작은값
			BigDecimal price = infiniteDetail.getAveragePrice().min(nowUpPrice);
			info.setPrice(price);
			
			info.setQuantity(oneBuySeed.divide(price, 0, RoundingMode.FLOOR).intValue());
			info.setConcludeType(ConcludeType.LOC);
			tradeInfoList.add(info);
		}
		{
			// LOC 큰수매수 +10%
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("LOC 큰수매수 (+10%)");
			
			// 현재가 +15% 와 평단+10%와 비교해서 작은값
			BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal("1.1"));
			info.setPrice(price.min(nowUpPrice));
			
			info.setQuantity(oneBuySeed.divide(price, 0, RoundingMode.FLOOR).intValue());
			info.setConcludeType(ConcludeType.LOC);
			tradeInfoList.add(info);
		}
		
		return tradeInfoList;
	}
}
