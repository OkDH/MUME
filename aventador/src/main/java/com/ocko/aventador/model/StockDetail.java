package com.ocko.aventador.model;

import java.time.LocalDateTime;

import com.ocko.aventador.constant.EtfSector;
import com.ocko.aventador.dao.model.aventador.StockHistory;

public class StockDetail extends StockHistory {

	// 섹터
	private EtfSector sector;
	// 진입 권장 RSI
	private Integer baseRsi;
	// 마지막 거래 시간
	private LocalDateTime lastTradeTime;
	
	
	/**
	 * RSI GAP
	 */
	public Float getGapRsi() {
		if(getRsi() != null && baseRsi != null)
			return getRsi() - baseRsi;
		return null;
	}
	
	public EtfSector getSector() {
		return sector;
	}
	public void setSector(EtfSector sector) {
		this.sector = sector;
	}
	public Integer getBaseRsi() {
		return baseRsi;
	}
	public void setBaseRsi(Integer baseRsi) {
		this.baseRsi = baseRsi;
	}
	public LocalDateTime getLastTradeTime() {
		return lastTradeTime;
	}

	public void setLastTradeTime(LocalDateTime lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}
	
}
