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
	
	private LocalDate date;
	
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal open;
	
	private BigDecimal prevClose;
	
	private Long volume;
	
	// 전일 대비 변화
	private BigDecimal chg;
	// 전일 대비 변화율
	private double chgp;
	
	private double rsi;
	
	/**
	 * 전일 대비 변화 계산
	 * @return
	 */
	public BigDecimal getChg() {
		if(close != null && prevClose != null) {
			chg = close.subtract(prevClose);
			return chg;
		}
		return null;
	}
	
	/**
	 * 전일 대비 변화율 계산
	 * @return
	 */
	public double getChgp() {
		getChg();
		if(chg != null || prevClose != null) {
			chgp = chg.divide(prevClose, 5, BigDecimal.ROUND_HALF_UP).movePointRight(2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return chgp;
		}
		return 0;
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
	 * @return {@link #date}
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date {@link #date}
	 */
	public void setDate(LocalDate date) {
		this.date = date;
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
	public double getRsi() {
		return rsi;
	}

	/**
	 * @param rsi {@link #rsi}
	 */
	public void setRsi(double rsi) {
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
	
}
