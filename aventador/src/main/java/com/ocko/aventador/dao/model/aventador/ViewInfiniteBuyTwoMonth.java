package com.ocko.aventador.dao.model.aventador;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ViewInfiniteBuyTwoMonth {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column aventador.view_infinite_buy_two_month.member_id
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    private Integer memberId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column aventador.view_infinite_buy_two_month.account_id
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    private Integer accountId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column aventador.view_infinite_buy_two_month.trade_date
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    private LocalDate tradeDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column aventador.view_infinite_buy_two_month.buy_price
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    private BigDecimal buyPrice;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column aventador.view_infinite_buy_two_month.member_id
     *
     * @return the value of aventador.view_infinite_buy_two_month.member_id
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column aventador.view_infinite_buy_two_month.member_id
     *
     * @param memberId the value for aventador.view_infinite_buy_two_month.member_id
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column aventador.view_infinite_buy_two_month.account_id
     *
     * @return the value of aventador.view_infinite_buy_two_month.account_id
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column aventador.view_infinite_buy_two_month.account_id
     *
     * @param accountId the value for aventador.view_infinite_buy_two_month.account_id
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column aventador.view_infinite_buy_two_month.trade_date
     *
     * @return the value of aventador.view_infinite_buy_two_month.trade_date
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public LocalDate getTradeDate() {
        return tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column aventador.view_infinite_buy_two_month.trade_date
     *
     * @param tradeDate the value for aventador.view_infinite_buy_two_month.trade_date
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column aventador.view_infinite_buy_two_month.buy_price
     *
     * @return the value of aventador.view_infinite_buy_two_month.buy_price
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column aventador.view_infinite_buy_two_month.buy_price
     *
     * @param buyPrice the value for aventador.view_infinite_buy_two_month.buy_price
     *
     * @mbg.generated Thu Dec 16 18:26:46 KST 2021
     */
    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }
}