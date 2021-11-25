/**
 * 
 */
package com.ocko.aventador.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocko.aventador.constant.EtfSector;

/**
 * @author ok
 *
 */
public class StockDetail {
	
	private String symbol;
	
	private EtfSector sector;
	
	private LocalDate stockDate;
	
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal open;
	
	private BigDecimal prevClose;
	
	private Long volume;
	
	// 전일비
	private BigDecimal chg;
	// 변화율
	private BigDecimal chgp;
	// 주가 RSI
	private Double rsi;
	// 진입 권장 RSI
	private Integer baseRsi;
	
//	public Double getChgp() {
//		if(chg != null && prevClose != null)
//			return chg.divide(prevClose, 5, BigDecimal.ROUND_HALF_UP).movePointRight(2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		return null;
//	}
	
	/**
	 * RSI GAP
	 */
	public Double getGapRsi() {
		if(rsi != null && baseRsi != null)
			return rsi - baseRsi;
		return null;
	}

	/**
	 * @return {@link #symbol}
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol {@link #symbol}
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return {@link #stockDate}
	 */
	public LocalDate getStockDate() {
		return stockDate;
	}

	/**
	 * @param stockDate {@link #stockDate}
	 */
	public void setStockDate(LocalDate stockDate) {
		this.stockDate = stockDate;
	}

	/**
	 * @return {@link #high}
	 */
	public BigDecimal getHigh() {
		return high;
	}

	/**
	 * @param high {@link #high}
	 */
	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	/**
	 * @return {@link #low}
	 */
	public BigDecimal getLow() {
		return low;
	}

	/**
	 * @param low {@link #low}
	 */
	public void setLow(BigDecimal low) {
		this.low = low;
	}

	/**
	 * @return {@link #close}
	 */
	public BigDecimal getClose() {
		return close;
	}

	/**
	 * @param close {@link #close}
	 */
	public void setClose(BigDecimal close) {
		this.close = close;
	}

	/**
	 * @return {@link #open}
	 */
	public BigDecimal getOpen() {
		return open;
	}

	/**
	 * @param open {@link #open}
	 */
	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * @return {@link #rsi}
	 */
	public Double getRsi() {
		return rsi;
	}

	/**
	 * @param rsi {@link #rsi}
	 */
	public void setRsi(Double rsi) {
		this.rsi = rsi;
	}

	/**
	 * @return {@link #prevClose}
	 */
	public BigDecimal getPrevClose() {
		return prevClose;
	}

	/**
	 * @param prevClose {@link #prevClose}
	 */
	public void setPrevClose(BigDecimal prevClose) {
		this.prevClose = prevClose;
	}

	public EtfSector getSector() {
		return sector;
	}

	public void setSector(EtfSector sector) {
		this.sector = sector;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	/**
	 * @return {@link #baseRsi}
	 */
	public Integer getBaseRsi() {
		return baseRsi;
	}

	/**
	 * @param baseRsi {@link #baseRsi}
	 */
	public void setBaseRsi(Integer baseRsi) {
		this.baseRsi = baseRsi;
	}

	public BigDecimal getChg() {
		return chg;
	}

	public void setChg(BigDecimal chg) {
		this.chg = chg;
	}

	public BigDecimal getChgp() {
		return chgp;
	}

	public void setChgp(BigDecimal chgp) {
		this.chgp = chgp;
	}
	
}
