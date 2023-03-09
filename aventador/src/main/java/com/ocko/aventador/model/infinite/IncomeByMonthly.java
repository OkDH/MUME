/**
 * 
 */
package com.ocko.aventador.model.infinite;

import java.math.BigDecimal;

/**
 * @author ok
 * 종목별 실현손익
 */
public class IncomeByMonthly {

	private Integer memberId;
	private Integer accountId;
	private String monthly;
	private BigDecimal buyPrice;
	private BigDecimal sellPrice;
	private BigDecimal income;
	private BigDecimal fees;
	private Integer sellCount;
	/**
	 * @return {@link #memberId}
	 */
	public Integer getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId {@link #memberId}
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return {@link #accountId}
	 */
	public Integer getAccountId() {
		return accountId;
	}
	/**
	 * @param accountId {@link #accountId}
	 */
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	/**
	 * @return {@link #buyPrice}
	 */
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}
	/**
	 * @param buyPrice {@link #buyPrice}
	 */
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}
	/**
	 * @return {@link #sellPrice}
	 */
	public BigDecimal getSellPrice() {
		return sellPrice;
	}
	/**
	 * @param sellPrice {@link #sellPrice}
	 */
	public void setSellPrice(BigDecimal sellPrice) {
		this.sellPrice = sellPrice;
	}
	/**
	 * @return {@link #income}
	 */
	public BigDecimal getIncome() {
		return income;
	}
	/**
	 * @param income {@link #income}
	 */
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	/**
	 * @return {@link #fees}
	 */
	public BigDecimal getFees() {
		return fees;
	}
	/**
	 * @param fees {@link #fees}
	 */
	public void setFees(BigDecimal fees) {
		this.fees = fees;
	}
	/**
	 * @return {@link #sellCount}
	 */
	public Integer getSellCount() {
		return sellCount;
	}
	/**
	 * @param sellCount {@link #sellCount}
	 */
	public void setSellCount(Integer sellCount) {
		this.sellCount = sellCount;
	}
	/**
	 * @return {@link #monthly}
	 */
	public String getMonthly() {
		return monthly;
	}
	/**
	 * @param monthly {@link #monthly}
	 */
	public void setMonthly(String monthly) {
		this.monthly = monthly;
	}
	
}
