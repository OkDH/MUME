package com.ocko.aventador.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ocko.aventador.constant.ConcludeType;
import com.ocko.aventador.constant.InfiniteVersion;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.model.StockTradeInfo;

@Component
public class InfiniteTradeComponent {
	
	// 수수료 포함 10% 계산용
	private static String TEN_PER = "1.1017";
	// 수수료 포함 5% 계산용
	private static String FIVE_PER = "1.0517";
	// 수수료 포함 0% 계산용
	private static String ZERO_PER = "1.0017";

	/**
	 * 매수 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	public List<StockTradeInfo> getBuyInfo(InfiniteDetail infiniteDetail) {
		if(infiniteDetail == null)
			return null;
		if(infiniteDetail.getStockDetail() == null)
			return null;
		
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		if(infiniteDetail.getInfiniteVersion() != null) {
			if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V2_1)) {
				tradeInfoList = getBuyInfoV2(infiniteDetail);
			} else if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V2_1_SH)) {
				tradeInfoList = getBuyInfoV2_SH(infiniteDetail);
			} else if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V2)) {
				tradeInfoList = getBuyInfoV2(infiniteDetail);
			} else if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V1)) {
				tradeInfoList = getBuyInfoV1(infiniteDetail);
			}
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
		
		if(infiniteDetail.getInfiniteVersion() != null) {
			if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V2_1)) {
				tradeInfoList = getSellInfoV2_1(infiniteDetail);
			} else if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V2_1_SH)) {
				tradeInfoList = getSellInfoV2_1_SH(infiniteDetail);
			} else if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V2)) {
				tradeInfoList = getSellInfoV2(infiniteDetail);
			} else if(infiniteDetail.getInfiniteVersion().equals(InfiniteVersion.V1)) {
				tradeInfoList = getSellInfoV1(infiniteDetail);
			}
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
		
		int quantity = (int) Math.floor(infiniteDetail.getHoldingQuantity()/4.0);
		
		// 진행률 
		if(infiniteDetail.getProgressPer().floatValue() < 50) { // 전반전
			
			{
				// LOC 매도 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 매도 (+5%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(FIVE_PER));
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 + 10%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				info.setQuantity(infiniteDetail.getHoldingQuantity() - quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		} else { // 후반전
			
			{
				// LOC 매도 +0%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 매도 (+0%)");
				info.setPrice(infiniteDetail.getAveragePrice().multiply(new BigDecimal(ZERO_PER)));
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+5%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(FIVE_PER));
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 +10%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				info.setQuantity(infiniteDetail.getHoldingQuantity() - quantity - quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v2_1 후반전 고정 매도 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getSellInfoV2_1_SH(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		int quantity = (int) Math.floor(infiniteDetail.getHoldingQuantity()/4.0);
		
		// 후반전
		{
			// LOC 매도 +0%
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("LOC 매도 (+0%)");
			info.setPrice(infiniteDetail.getAveragePrice().multiply(new BigDecimal(ZERO_PER)));
			info.setQuantity(quantity);
			info.setConcludeType(ConcludeType.LOC);
			tradeInfoList.add(info);
		}
		{
			// 지정가 매도 +5%
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("지정가 매도 (+5%)");
			BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(FIVE_PER));
			info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
			info.setQuantity(quantity);
			info.setConcludeType(ConcludeType.PENDING_ORDER);
			tradeInfoList.add(info);
		}
		{
			// 지정가 매도 +10%
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("지정가 매도 (+10%)");
			BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
			info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
			info.setQuantity(infiniteDetail.getHoldingQuantity() - quantity - quantity);
			info.setConcludeType(ConcludeType.PENDING_ORDER);
			tradeInfoList.add(info);
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
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				info.setQuantity(infiniteDetail.getHoldingQuantity());
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
		} else { // 후반전
			
			int quantity = (int) Math.floor(infiniteDetail.getHoldingQuantity()/2.0);
			
			{
				// 지정가 매도 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+5%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(FIVE_PER));
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				info.setQuantity(quantity);
				info.setConcludeType(ConcludeType.PENDING_ORDER);
				tradeInfoList.add(info);
			}
			{
				// 지정가 매도 +10%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("지정가 매도 (+10%)");
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal(TEN_PER));
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
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
			info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
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
		// 1회 매수 량
		Integer oneBuyQuantity = infiniteDetail.getOneBuyQuantity();
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
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				
				// 수량
				int quantity = (int) Math.round(oneBuyQuantity/2.0);
				info.setQuantity(quantity);
				oneBuyQuantity -= quantity;
				
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
			{
				// LOC 큰수매수 +5%
				StockTradeInfo info = new StockTradeInfo();
				info.setTradeName("LOC 큰수매수 (+5%)");
				
				// 현재가 +15% 와 평단가+5% 와 비교해서 작은값
				BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal("1.05"));
				info.setPrice(price.min(nowUpPrice).setScale(2, RoundingMode.HALF_UP));
				
				info.setQuantity(oneBuyQuantity);
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
				info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
				
				info.setQuantity(oneBuyQuantity);
				info.setConcludeType(ConcludeType.LOC);
				tradeInfoList.add(info);
			}
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v2 후반전 고정 매수 정보 가져오기
	 * @param infiniteDetail
	 * @return
	 */
	private List<StockTradeInfo> getBuyInfoV2_SH(InfiniteDetail infiniteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		// 1회 매수 량
		Integer oneBuyQuantity = infiniteDetail.getOneBuyQuantity();
		// 현재가 +15%
		BigDecimal nowUpPrice = infiniteDetail.getStockDetail().getPriceClose().multiply(new BigDecimal("1.15"));
		
		// 후반전
		{
			// LOC 평단매수
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("LOC 평단매수");
			
			// 현재가 +15% 와 평단가와 비교해서 작은값
			BigDecimal price = infiniteDetail.getAveragePrice().min(nowUpPrice);
			info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
			
			info.setQuantity(oneBuyQuantity);
			info.setConcludeType(ConcludeType.LOC);
			tradeInfoList.add(info);
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
		
		// 1회 매수량
		Integer oneBuyQuantity = infiniteDetail.getOneBuyQuantity();
		// 현재가 +15%
		BigDecimal nowUpPrice = infiniteDetail.getStockDetail().getPriceClose().multiply(new BigDecimal("1.15"));
				
		{
			// LOC 평단매수
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("LOC 평단매수");
			
			// 현재가 +15% 와 평단가와 비교해서 작은값
			BigDecimal price = infiniteDetail.getAveragePrice().min(nowUpPrice);
			info.setPrice(price.setScale(2, RoundingMode.HALF_UP));
			
			// 수량
			int quantity = (int) Math.round(oneBuyQuantity/2.0);
			info.setQuantity(quantity);
			oneBuyQuantity -= quantity;
			
			info.setConcludeType(ConcludeType.LOC);
			tradeInfoList.add(info);
		}
		{
			// LOC 큰수매수 +10%
			StockTradeInfo info = new StockTradeInfo();
			info.setTradeName("LOC 큰수매수 (+10%)");
			
			// 현재가 +15% 와 평단+10%와 비교해서 작은값
			BigDecimal price = infiniteDetail.getAveragePrice().multiply(new BigDecimal("1.1"));
			info.setPrice(price.min(nowUpPrice).setScale(2, RoundingMode.HALF_UP));
			
			info.setQuantity(oneBuyQuantity);
			info.setConcludeType(ConcludeType.LOC);
			tradeInfoList.add(info);
		}
		
		return tradeInfoList;
	}
}
