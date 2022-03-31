package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ocko.aventador.constant.EtfSector;
import com.ocko.aventador.dao.model.aventador.StockHistory;

public class StockDetail extends StockHistory {

	// 섹터
	private EtfSector sector;
	// 섹터 이름
	private String sectorName;
	// 진입 권장 RSI
	private Integer baseRsi;
	// 거래유의 종목 여부
	private Boolean isWarn;
	// 마지막 거래 시간
	private LocalDateTime lastTradeTime;
	
	/**
	 * RSI GAP
	 */
	public BigDecimal getGapRsi() {
		if(getRsi() != null && baseRsi != null)
			return getRsi().subtract(new BigDecimal(baseRsi));
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
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public void setLastTradeTime(LocalDateTime lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}
	public Boolean getIsWarn() {
		return isWarn;
	}
	public void setIsWarn(Boolean isWarn) {
		this.isWarn = isWarn;
	}
	
}
