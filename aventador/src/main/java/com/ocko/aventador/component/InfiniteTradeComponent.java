package com.ocko.aventador.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ocko.aventador.constant.InfiniteType;
import com.ocko.aventador.model.InfiniteDetail;
import com.ocko.aventador.model.StockTradeInfo;

@Component
public class InfiniteTradeComponent {

	/**
	 * 매도 정보 가져오기
	 * @param infinteDetail
	 * @return
	 */
	public List<StockTradeInfo> getBuyInfo(InfiniteDetail infinteDetail) {
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		
		if(infinteDetail.getInfiniteType().equals(InfiniteType.V2_1)) {
			tradeInfoList = getBuyInfoV2_1(infinteDetail);
		} else if(infinteDetail.getInfiniteType().equals(InfiniteType.V2)) {
			
		} else if(infinteDetail.getInfiniteType().equals(InfiniteType.V1)) {
			
		}
		
		return tradeInfoList;
	}
	
	/**
	 * v2.1 매도 정보 가져오기
	 * @param infinteDetail
	 * @return
	 */
	private List<StockTradeInfo> getBuyInfoV2_1(InfiniteDetail infinteDetail){
		List<StockTradeInfo> tradeInfoList = new ArrayList<StockTradeInfo>();
		return tradeInfoList;
	}
}
