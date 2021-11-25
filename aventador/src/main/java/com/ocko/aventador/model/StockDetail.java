package com.ocko.aventador.model;

import com.ocko.aventador.constant.EtfSector;
import com.ocko.aventador.dao.model.aventador.StockHistory;

public class StockDetail extends StockHistory {

	private EtfSector sector;
	// 진입 권장 RSI
	private Integer baseRsi;
	
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
	
}
