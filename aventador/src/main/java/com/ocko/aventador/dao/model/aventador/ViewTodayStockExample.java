package com.ocko.aventador.dao.model.aventador;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewTodayStockExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	protected List<Criteria> oredCriteria;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table {0}
	 * @mbggenerated
	 */
	private Integer limit;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table {0}
	 * @mbggenerated
	 */
	private Integer offset;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public ViewTodayStockExample() {
		oredCriteria = new ArrayList<>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table {0}
	 * @mbggenerated
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table {0}
	 * @mbggenerated
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table {0}
	 * @mbggenerated
	 */
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table {0}
	 * @mbggenerated
	 */
	public Integer getOffset() {
		return offset;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andSymbolIsNull() {
			addCriterion("symbol is null");
			return (Criteria) this;
		}

		public Criteria andSymbolIsNotNull() {
			addCriterion("symbol is not null");
			return (Criteria) this;
		}

		public Criteria andSymbolEqualTo(String value) {
			addCriterion("symbol =", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolNotEqualTo(String value) {
			addCriterion("symbol <>", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolGreaterThan(String value) {
			addCriterion("symbol >", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolGreaterThanOrEqualTo(String value) {
			addCriterion("symbol >=", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolLessThan(String value) {
			addCriterion("symbol <", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolLessThanOrEqualTo(String value) {
			addCriterion("symbol <=", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolLike(String value) {
			addCriterion("symbol like", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolNotLike(String value) {
			addCriterion("symbol not like", value, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolIn(List<String> values) {
			addCriterion("symbol in", values, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolNotIn(List<String> values) {
			addCriterion("symbol not in", values, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolBetween(String value1, String value2) {
			addCriterion("symbol between", value1, value2, "symbol");
			return (Criteria) this;
		}

		public Criteria andSymbolNotBetween(String value1, String value2) {
			addCriterion("symbol not between", value1, value2, "symbol");
			return (Criteria) this;
		}

		public Criteria andStockDateIsNull() {
			addCriterion("stock_date is null");
			return (Criteria) this;
		}

		public Criteria andStockDateIsNotNull() {
			addCriterion("stock_date is not null");
			return (Criteria) this;
		}

		public Criteria andStockDateEqualTo(LocalDate value) {
			addCriterion("stock_date =", value, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateNotEqualTo(LocalDate value) {
			addCriterion("stock_date <>", value, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateGreaterThan(LocalDate value) {
			addCriterion("stock_date >", value, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateGreaterThanOrEqualTo(LocalDate value) {
			addCriterion("stock_date >=", value, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateLessThan(LocalDate value) {
			addCriterion("stock_date <", value, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateLessThanOrEqualTo(LocalDate value) {
			addCriterion("stock_date <=", value, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateIn(List<LocalDate> values) {
			addCriterion("stock_date in", values, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateNotIn(List<LocalDate> values) {
			addCriterion("stock_date not in", values, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateBetween(LocalDate value1, LocalDate value2) {
			addCriterion("stock_date between", value1, value2, "stockDate");
			return (Criteria) this;
		}

		public Criteria andStockDateNotBetween(LocalDate value1, LocalDate value2) {
			addCriterion("stock_date not between", value1, value2, "stockDate");
			return (Criteria) this;
		}

		public Criteria andPriceHighIsNull() {
			addCriterion("price_high is null");
			return (Criteria) this;
		}

		public Criteria andPriceHighIsNotNull() {
			addCriterion("price_high is not null");
			return (Criteria) this;
		}

		public Criteria andPriceHighEqualTo(Float value) {
			addCriterion("price_high =", value, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighNotEqualTo(Float value) {
			addCriterion("price_high <>", value, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighGreaterThan(Float value) {
			addCriterion("price_high >", value, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighGreaterThanOrEqualTo(Float value) {
			addCriterion("price_high >=", value, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighLessThan(Float value) {
			addCriterion("price_high <", value, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighLessThanOrEqualTo(Float value) {
			addCriterion("price_high <=", value, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighIn(List<Float> values) {
			addCriterion("price_high in", values, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighNotIn(List<Float> values) {
			addCriterion("price_high not in", values, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighBetween(Float value1, Float value2) {
			addCriterion("price_high between", value1, value2, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceHighNotBetween(Float value1, Float value2) {
			addCriterion("price_high not between", value1, value2, "priceHigh");
			return (Criteria) this;
		}

		public Criteria andPriceLowIsNull() {
			addCriterion("price_low is null");
			return (Criteria) this;
		}

		public Criteria andPriceLowIsNotNull() {
			addCriterion("price_low is not null");
			return (Criteria) this;
		}

		public Criteria andPriceLowEqualTo(Float value) {
			addCriterion("price_low =", value, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowNotEqualTo(Float value) {
			addCriterion("price_low <>", value, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowGreaterThan(Float value) {
			addCriterion("price_low >", value, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowGreaterThanOrEqualTo(Float value) {
			addCriterion("price_low >=", value, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowLessThan(Float value) {
			addCriterion("price_low <", value, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowLessThanOrEqualTo(Float value) {
			addCriterion("price_low <=", value, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowIn(List<Float> values) {
			addCriterion("price_low in", values, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowNotIn(List<Float> values) {
			addCriterion("price_low not in", values, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowBetween(Float value1, Float value2) {
			addCriterion("price_low between", value1, value2, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceLowNotBetween(Float value1, Float value2) {
			addCriterion("price_low not between", value1, value2, "priceLow");
			return (Criteria) this;
		}

		public Criteria andPriceCloseIsNull() {
			addCriterion("price_close is null");
			return (Criteria) this;
		}

		public Criteria andPriceCloseIsNotNull() {
			addCriterion("price_close is not null");
			return (Criteria) this;
		}

		public Criteria andPriceCloseEqualTo(Float value) {
			addCriterion("price_close =", value, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseNotEqualTo(Float value) {
			addCriterion("price_close <>", value, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseGreaterThan(Float value) {
			addCriterion("price_close >", value, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseGreaterThanOrEqualTo(Float value) {
			addCriterion("price_close >=", value, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseLessThan(Float value) {
			addCriterion("price_close <", value, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseLessThanOrEqualTo(Float value) {
			addCriterion("price_close <=", value, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseIn(List<Float> values) {
			addCriterion("price_close in", values, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseNotIn(List<Float> values) {
			addCriterion("price_close not in", values, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseBetween(Float value1, Float value2) {
			addCriterion("price_close between", value1, value2, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceCloseNotBetween(Float value1, Float value2) {
			addCriterion("price_close not between", value1, value2, "priceClose");
			return (Criteria) this;
		}

		public Criteria andPriceOpenIsNull() {
			addCriterion("price_open is null");
			return (Criteria) this;
		}

		public Criteria andPriceOpenIsNotNull() {
			addCriterion("price_open is not null");
			return (Criteria) this;
		}

		public Criteria andPriceOpenEqualTo(Float value) {
			addCriterion("price_open =", value, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenNotEqualTo(Float value) {
			addCriterion("price_open <>", value, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenGreaterThan(Float value) {
			addCriterion("price_open >", value, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenGreaterThanOrEqualTo(Float value) {
			addCriterion("price_open >=", value, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenLessThan(Float value) {
			addCriterion("price_open <", value, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenLessThanOrEqualTo(Float value) {
			addCriterion("price_open <=", value, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenIn(List<Float> values) {
			addCriterion("price_open in", values, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenNotIn(List<Float> values) {
			addCriterion("price_open not in", values, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenBetween(Float value1, Float value2) {
			addCriterion("price_open between", value1, value2, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPriceOpenNotBetween(Float value1, Float value2) {
			addCriterion("price_open not between", value1, value2, "priceOpen");
			return (Criteria) this;
		}

		public Criteria andPrevCloseIsNull() {
			addCriterion("prev_close is null");
			return (Criteria) this;
		}

		public Criteria andPrevCloseIsNotNull() {
			addCriterion("prev_close is not null");
			return (Criteria) this;
		}

		public Criteria andPrevCloseEqualTo(Float value) {
			addCriterion("prev_close =", value, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseNotEqualTo(Float value) {
			addCriterion("prev_close <>", value, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseGreaterThan(Float value) {
			addCriterion("prev_close >", value, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseGreaterThanOrEqualTo(Float value) {
			addCriterion("prev_close >=", value, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseLessThan(Float value) {
			addCriterion("prev_close <", value, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseLessThanOrEqualTo(Float value) {
			addCriterion("prev_close <=", value, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseIn(List<Float> values) {
			addCriterion("prev_close in", values, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseNotIn(List<Float> values) {
			addCriterion("prev_close not in", values, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseBetween(Float value1, Float value2) {
			addCriterion("prev_close between", value1, value2, "prevClose");
			return (Criteria) this;
		}

		public Criteria andPrevCloseNotBetween(Float value1, Float value2) {
			addCriterion("prev_close not between", value1, value2, "prevClose");
			return (Criteria) this;
		}

		public Criteria andChgIsNull() {
			addCriterion("chg is null");
			return (Criteria) this;
		}

		public Criteria andChgIsNotNull() {
			addCriterion("chg is not null");
			return (Criteria) this;
		}

		public Criteria andChgEqualTo(Float value) {
			addCriterion("chg =", value, "chg");
			return (Criteria) this;
		}

		public Criteria andChgNotEqualTo(Float value) {
			addCriterion("chg <>", value, "chg");
			return (Criteria) this;
		}

		public Criteria andChgGreaterThan(Float value) {
			addCriterion("chg >", value, "chg");
			return (Criteria) this;
		}

		public Criteria andChgGreaterThanOrEqualTo(Float value) {
			addCriterion("chg >=", value, "chg");
			return (Criteria) this;
		}

		public Criteria andChgLessThan(Float value) {
			addCriterion("chg <", value, "chg");
			return (Criteria) this;
		}

		public Criteria andChgLessThanOrEqualTo(Float value) {
			addCriterion("chg <=", value, "chg");
			return (Criteria) this;
		}

		public Criteria andChgIn(List<Float> values) {
			addCriterion("chg in", values, "chg");
			return (Criteria) this;
		}

		public Criteria andChgNotIn(List<Float> values) {
			addCriterion("chg not in", values, "chg");
			return (Criteria) this;
		}

		public Criteria andChgBetween(Float value1, Float value2) {
			addCriterion("chg between", value1, value2, "chg");
			return (Criteria) this;
		}

		public Criteria andChgNotBetween(Float value1, Float value2) {
			addCriterion("chg not between", value1, value2, "chg");
			return (Criteria) this;
		}

		public Criteria andChgpIsNull() {
			addCriterion("chgp is null");
			return (Criteria) this;
		}

		public Criteria andChgpIsNotNull() {
			addCriterion("chgp is not null");
			return (Criteria) this;
		}

		public Criteria andChgpEqualTo(Float value) {
			addCriterion("chgp =", value, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpNotEqualTo(Float value) {
			addCriterion("chgp <>", value, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpGreaterThan(Float value) {
			addCriterion("chgp >", value, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpGreaterThanOrEqualTo(Float value) {
			addCriterion("chgp >=", value, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpLessThan(Float value) {
			addCriterion("chgp <", value, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpLessThanOrEqualTo(Float value) {
			addCriterion("chgp <=", value, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpIn(List<Float> values) {
			addCriterion("chgp in", values, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpNotIn(List<Float> values) {
			addCriterion("chgp not in", values, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpBetween(Float value1, Float value2) {
			addCriterion("chgp between", value1, value2, "chgp");
			return (Criteria) this;
		}

		public Criteria andChgpNotBetween(Float value1, Float value2) {
			addCriterion("chgp not between", value1, value2, "chgp");
			return (Criteria) this;
		}

		public Criteria andVolumeIsNull() {
			addCriterion("volume is null");
			return (Criteria) this;
		}

		public Criteria andVolumeIsNotNull() {
			addCriterion("volume is not null");
			return (Criteria) this;
		}

		public Criteria andVolumeEqualTo(Long value) {
			addCriterion("volume =", value, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeNotEqualTo(Long value) {
			addCriterion("volume <>", value, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeGreaterThan(Long value) {
			addCriterion("volume >", value, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeGreaterThanOrEqualTo(Long value) {
			addCriterion("volume >=", value, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeLessThan(Long value) {
			addCriterion("volume <", value, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeLessThanOrEqualTo(Long value) {
			addCriterion("volume <=", value, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeIn(List<Long> values) {
			addCriterion("volume in", values, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeNotIn(List<Long> values) {
			addCriterion("volume not in", values, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeBetween(Long value1, Long value2) {
			addCriterion("volume between", value1, value2, "volume");
			return (Criteria) this;
		}

		public Criteria andVolumeNotBetween(Long value1, Long value2) {
			addCriterion("volume not between", value1, value2, "volume");
			return (Criteria) this;
		}

		public Criteria andUpAvgIsNull() {
			addCriterion("up_avg is null");
			return (Criteria) this;
		}

		public Criteria andUpAvgIsNotNull() {
			addCriterion("up_avg is not null");
			return (Criteria) this;
		}

		public Criteria andUpAvgEqualTo(Float value) {
			addCriterion("up_avg =", value, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgNotEqualTo(Float value) {
			addCriterion("up_avg <>", value, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgGreaterThan(Float value) {
			addCriterion("up_avg >", value, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgGreaterThanOrEqualTo(Float value) {
			addCriterion("up_avg >=", value, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgLessThan(Float value) {
			addCriterion("up_avg <", value, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgLessThanOrEqualTo(Float value) {
			addCriterion("up_avg <=", value, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgIn(List<Float> values) {
			addCriterion("up_avg in", values, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgNotIn(List<Float> values) {
			addCriterion("up_avg not in", values, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgBetween(Float value1, Float value2) {
			addCriterion("up_avg between", value1, value2, "upAvg");
			return (Criteria) this;
		}

		public Criteria andUpAvgNotBetween(Float value1, Float value2) {
			addCriterion("up_avg not between", value1, value2, "upAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgIsNull() {
			addCriterion("dw_avg is null");
			return (Criteria) this;
		}

		public Criteria andDwAvgIsNotNull() {
			addCriterion("dw_avg is not null");
			return (Criteria) this;
		}

		public Criteria andDwAvgEqualTo(Float value) {
			addCriterion("dw_avg =", value, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgNotEqualTo(Float value) {
			addCriterion("dw_avg <>", value, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgGreaterThan(Float value) {
			addCriterion("dw_avg >", value, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgGreaterThanOrEqualTo(Float value) {
			addCriterion("dw_avg >=", value, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgLessThan(Float value) {
			addCriterion("dw_avg <", value, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgLessThanOrEqualTo(Float value) {
			addCriterion("dw_avg <=", value, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgIn(List<Float> values) {
			addCriterion("dw_avg in", values, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgNotIn(List<Float> values) {
			addCriterion("dw_avg not in", values, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgBetween(Float value1, Float value2) {
			addCriterion("dw_avg between", value1, value2, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andDwAvgNotBetween(Float value1, Float value2) {
			addCriterion("dw_avg not between", value1, value2, "dwAvg");
			return (Criteria) this;
		}

		public Criteria andRsiIsNull() {
			addCriterion("rsi is null");
			return (Criteria) this;
		}

		public Criteria andRsiIsNotNull() {
			addCriterion("rsi is not null");
			return (Criteria) this;
		}

		public Criteria andRsiEqualTo(Float value) {
			addCriterion("rsi =", value, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiNotEqualTo(Float value) {
			addCriterion("rsi <>", value, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiGreaterThan(Float value) {
			addCriterion("rsi >", value, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiGreaterThanOrEqualTo(Float value) {
			addCriterion("rsi >=", value, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiLessThan(Float value) {
			addCriterion("rsi <", value, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiLessThanOrEqualTo(Float value) {
			addCriterion("rsi <=", value, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiIn(List<Float> values) {
			addCriterion("rsi in", values, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiNotIn(List<Float> values) {
			addCriterion("rsi not in", values, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiBetween(Float value1, Float value2) {
			addCriterion("rsi between", value1, value2, "rsi");
			return (Criteria) this;
		}

		public Criteria andRsiNotBetween(Float value1, Float value2) {
			addCriterion("rsi not between", value1, value2, "rsi");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table aventador.view_today_stock
	 * @mbg.generated  Thu Nov 25 22:33:06 KST 2021
	 */
	public static class Criterion {
		private String condition;
		private Object value;
		private Object secondValue;
		private boolean noValue;
		private boolean singleValue;
		private boolean betweenValue;
		private boolean listValue;
		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table aventador.view_today_stock
     *
     * @mbg.generated do_not_delete_during_merge Thu Nov 25 16:17:55 KST 2021
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }
}