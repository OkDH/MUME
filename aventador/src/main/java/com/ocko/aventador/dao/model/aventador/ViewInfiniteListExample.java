package com.ocko.aventador.dao.model.aventador;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class ViewInfiniteListExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
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
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public ViewInfiniteListExample() {
		oredCriteria = new ArrayList<>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
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
	 * This class was generated by MyBatis Generator. This class corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
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

		public Criteria andMemberIdIsNull() {
			addCriterion("member_id is null");
			return (Criteria) this;
		}

		public Criteria andMemberIdIsNotNull() {
			addCriterion("member_id is not null");
			return (Criteria) this;
		}

		public Criteria andMemberIdEqualTo(Integer value) {
			addCriterion("member_id =", value, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdNotEqualTo(Integer value) {
			addCriterion("member_id <>", value, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdGreaterThan(Integer value) {
			addCriterion("member_id >", value, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("member_id >=", value, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdLessThan(Integer value) {
			addCriterion("member_id <", value, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdLessThanOrEqualTo(Integer value) {
			addCriterion("member_id <=", value, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdIn(List<Integer> values) {
			addCriterion("member_id in", values, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdNotIn(List<Integer> values) {
			addCriterion("member_id not in", values, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdBetween(Integer value1, Integer value2) {
			addCriterion("member_id between", value1, value2, "memberId");
			return (Criteria) this;
		}

		public Criteria andMemberIdNotBetween(Integer value1, Integer value2) {
			addCriterion("member_id not between", value1, value2, "memberId");
			return (Criteria) this;
		}

		public Criteria andAccountIdIsNull() {
			addCriterion("account_id is null");
			return (Criteria) this;
		}

		public Criteria andAccountIdIsNotNull() {
			addCriterion("account_id is not null");
			return (Criteria) this;
		}

		public Criteria andAccountIdEqualTo(Integer value) {
			addCriterion("account_id =", value, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdNotEqualTo(Integer value) {
			addCriterion("account_id <>", value, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdGreaterThan(Integer value) {
			addCriterion("account_id >", value, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("account_id >=", value, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdLessThan(Integer value) {
			addCriterion("account_id <", value, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdLessThanOrEqualTo(Integer value) {
			addCriterion("account_id <=", value, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdIn(List<Integer> values) {
			addCriterion("account_id in", values, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdNotIn(List<Integer> values) {
			addCriterion("account_id not in", values, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdBetween(Integer value1, Integer value2) {
			addCriterion("account_id between", value1, value2, "accountId");
			return (Criteria) this;
		}

		public Criteria andAccountIdNotBetween(Integer value1, Integer value2) {
			addCriterion("account_id not between", value1, value2, "accountId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdIsNull() {
			addCriterion("infinite_id is null");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdIsNotNull() {
			addCriterion("infinite_id is not null");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdEqualTo(Integer value) {
			addCriterion("infinite_id =", value, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdNotEqualTo(Integer value) {
			addCriterion("infinite_id <>", value, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdGreaterThan(Integer value) {
			addCriterion("infinite_id >", value, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("infinite_id >=", value, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdLessThan(Integer value) {
			addCriterion("infinite_id <", value, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdLessThanOrEqualTo(Integer value) {
			addCriterion("infinite_id <=", value, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdIn(List<Integer> values) {
			addCriterion("infinite_id in", values, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdNotIn(List<Integer> values) {
			addCriterion("infinite_id not in", values, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdBetween(Integer value1, Integer value2) {
			addCriterion("infinite_id between", value1, value2, "infiniteId");
			return (Criteria) this;
		}

		public Criteria andInfiniteIdNotBetween(Integer value1, Integer value2) {
			addCriterion("infinite_id not between", value1, value2, "infiniteId");
			return (Criteria) this;
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

		public Criteria andSeedIsNull() {
			addCriterion("seed is null");
			return (Criteria) this;
		}

		public Criteria andSeedIsNotNull() {
			addCriterion("seed is not null");
			return (Criteria) this;
		}

		public Criteria andSeedEqualTo(BigDecimal value) {
			addCriterion("seed =", value, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedNotEqualTo(BigDecimal value) {
			addCriterion("seed <>", value, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedGreaterThan(BigDecimal value) {
			addCriterion("seed >", value, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("seed >=", value, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedLessThan(BigDecimal value) {
			addCriterion("seed <", value, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedLessThanOrEqualTo(BigDecimal value) {
			addCriterion("seed <=", value, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedIn(List<BigDecimal> values) {
			addCriterion("seed in", values, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedNotIn(List<BigDecimal> values) {
			addCriterion("seed not in", values, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("seed between", value1, value2, "seed");
			return (Criteria) this;
		}

		public Criteria andSeedNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("seed not between", value1, value2, "seed");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeIsNull() {
			addCriterion("infinite_type is null");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeIsNotNull() {
			addCriterion("infinite_type is not null");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeEqualTo(String value) {
			addCriterion("infinite_type =", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeNotEqualTo(String value) {
			addCriterion("infinite_type <>", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeGreaterThan(String value) {
			addCriterion("infinite_type >", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeGreaterThanOrEqualTo(String value) {
			addCriterion("infinite_type >=", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeLessThan(String value) {
			addCriterion("infinite_type <", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeLessThanOrEqualTo(String value) {
			addCriterion("infinite_type <=", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeLike(String value) {
			addCriterion("infinite_type like", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeNotLike(String value) {
			addCriterion("infinite_type not like", value, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeIn(List<String> values) {
			addCriterion("infinite_type in", values, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeNotIn(List<String> values) {
			addCriterion("infinite_type not in", values, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeBetween(String value1, String value2) {
			addCriterion("infinite_type between", value1, value2, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteTypeNotBetween(String value1, String value2) {
			addCriterion("infinite_type not between", value1, value2, "infiniteType");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateIsNull() {
			addCriterion("infinite_state is null");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateIsNotNull() {
			addCriterion("infinite_state is not null");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateEqualTo(String value) {
			addCriterion("infinite_state =", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateNotEqualTo(String value) {
			addCriterion("infinite_state <>", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateGreaterThan(String value) {
			addCriterion("infinite_state >", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateGreaterThanOrEqualTo(String value) {
			addCriterion("infinite_state >=", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateLessThan(String value) {
			addCriterion("infinite_state <", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateLessThanOrEqualTo(String value) {
			addCriterion("infinite_state <=", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateLike(String value) {
			addCriterion("infinite_state like", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateNotLike(String value) {
			addCriterion("infinite_state not like", value, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateIn(List<String> values) {
			addCriterion("infinite_state in", values, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateNotIn(List<String> values) {
			addCriterion("infinite_state not in", values, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateBetween(String value1, String value2) {
			addCriterion("infinite_state between", value1, value2, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andInfiniteStateNotBetween(String value1, String value2) {
			addCriterion("infinite_state not between", value1, value2, "infiniteState");
			return (Criteria) this;
		}

		public Criteria andStartedDateIsNull() {
			addCriterion("started_date is null");
			return (Criteria) this;
		}

		public Criteria andStartedDateIsNotNull() {
			addCriterion("started_date is not null");
			return (Criteria) this;
		}

		public Criteria andStartedDateEqualTo(LocalDate value) {
			addCriterion("started_date =", value, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateNotEqualTo(LocalDate value) {
			addCriterion("started_date <>", value, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateGreaterThan(LocalDate value) {
			addCriterion("started_date >", value, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateGreaterThanOrEqualTo(LocalDate value) {
			addCriterion("started_date >=", value, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateLessThan(LocalDate value) {
			addCriterion("started_date <", value, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateLessThanOrEqualTo(LocalDate value) {
			addCriterion("started_date <=", value, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateIn(List<LocalDate> values) {
			addCriterion("started_date in", values, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateNotIn(List<LocalDate> values) {
			addCriterion("started_date not in", values, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateBetween(LocalDate value1, LocalDate value2) {
			addCriterion("started_date between", value1, value2, "startedDate");
			return (Criteria) this;
		}

		public Criteria andStartedDateNotBetween(LocalDate value1, LocalDate value2) {
			addCriterion("started_date not between", value1, value2, "startedDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateIsNull() {
			addCriterion("registered_date is null");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateIsNotNull() {
			addCriterion("registered_date is not null");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateEqualTo(LocalDateTime value) {
			addCriterion("registered_date =", value, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateNotEqualTo(LocalDateTime value) {
			addCriterion("registered_date <>", value, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateGreaterThan(LocalDateTime value) {
			addCriterion("registered_date >", value, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateGreaterThanOrEqualTo(LocalDateTime value) {
			addCriterion("registered_date >=", value, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateLessThan(LocalDateTime value) {
			addCriterion("registered_date <", value, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateLessThanOrEqualTo(LocalDateTime value) {
			addCriterion("registered_date <=", value, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateIn(List<LocalDateTime> values) {
			addCriterion("registered_date in", values, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateNotIn(List<LocalDateTime> values) {
			addCriterion("registered_date not in", values, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateBetween(LocalDateTime value1, LocalDateTime value2) {
			addCriterion("registered_date between", value1, value2, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andRegisteredDateNotBetween(LocalDateTime value1, LocalDateTime value2) {
			addCriterion("registered_date not between", value1, value2, "registeredDate");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityIsNull() {
			addCriterion("holding_quantity is null");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityIsNotNull() {
			addCriterion("holding_quantity is not null");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityEqualTo(Integer value) {
			addCriterion("holding_quantity =", value, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityNotEqualTo(Integer value) {
			addCriterion("holding_quantity <>", value, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityGreaterThan(Integer value) {
			addCriterion("holding_quantity >", value, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityGreaterThanOrEqualTo(Integer value) {
			addCriterion("holding_quantity >=", value, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityLessThan(Integer value) {
			addCriterion("holding_quantity <", value, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityLessThanOrEqualTo(Integer value) {
			addCriterion("holding_quantity <=", value, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityIn(List<Integer> values) {
			addCriterion("holding_quantity in", values, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityNotIn(List<Integer> values) {
			addCriterion("holding_quantity not in", values, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityBetween(Integer value1, Integer value2) {
			addCriterion("holding_quantity between", value1, value2, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andHoldingQuantityNotBetween(Integer value1, Integer value2) {
			addCriterion("holding_quantity not between", value1, value2, "holdingQuantity");
			return (Criteria) this;
		}

		public Criteria andAveragePriceIsNull() {
			addCriterion("average_price is null");
			return (Criteria) this;
		}

		public Criteria andAveragePriceIsNotNull() {
			addCriterion("average_price is not null");
			return (Criteria) this;
		}

		public Criteria andAveragePriceEqualTo(BigDecimal value) {
			addCriterion("average_price =", value, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceNotEqualTo(BigDecimal value) {
			addCriterion("average_price <>", value, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceGreaterThan(BigDecimal value) {
			addCriterion("average_price >", value, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("average_price >=", value, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceLessThan(BigDecimal value) {
			addCriterion("average_price <", value, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("average_price <=", value, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceIn(List<BigDecimal> values) {
			addCriterion("average_price in", values, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceNotIn(List<BigDecimal> values) {
			addCriterion("average_price not in", values, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("average_price between", value1, value2, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andAveragePriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("average_price not between", value1, value2, "averagePrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceIsNull() {
			addCriterion("buy_price is null");
			return (Criteria) this;
		}

		public Criteria andBuyPriceIsNotNull() {
			addCriterion("buy_price is not null");
			return (Criteria) this;
		}

		public Criteria andBuyPriceEqualTo(BigDecimal value) {
			addCriterion("buy_price =", value, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceNotEqualTo(BigDecimal value) {
			addCriterion("buy_price <>", value, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceGreaterThan(BigDecimal value) {
			addCriterion("buy_price >", value, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("buy_price >=", value, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceLessThan(BigDecimal value) {
			addCriterion("buy_price <", value, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("buy_price <=", value, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceIn(List<BigDecimal> values) {
			addCriterion("buy_price in", values, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceNotIn(List<BigDecimal> values) {
			addCriterion("buy_price not in", values, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("buy_price between", value1, value2, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andBuyPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("buy_price not between", value1, value2, "buyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceIsNull() {
			addCriterion("total_buy_price is null");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceIsNotNull() {
			addCriterion("total_buy_price is not null");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceEqualTo(BigDecimal value) {
			addCriterion("total_buy_price =", value, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceNotEqualTo(BigDecimal value) {
			addCriterion("total_buy_price <>", value, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceGreaterThan(BigDecimal value) {
			addCriterion("total_buy_price >", value, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("total_buy_price >=", value, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceLessThan(BigDecimal value) {
			addCriterion("total_buy_price <", value, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("total_buy_price <=", value, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceIn(List<BigDecimal> values) {
			addCriterion("total_buy_price in", values, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceNotIn(List<BigDecimal> values) {
			addCriterion("total_buy_price not in", values, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("total_buy_price between", value1, value2, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalBuyPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("total_buy_price not between", value1, value2, "totalBuyPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceIsNull() {
			addCriterion("total_sell_price is null");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceIsNotNull() {
			addCriterion("total_sell_price is not null");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceEqualTo(BigDecimal value) {
			addCriterion("total_sell_price =", value, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceNotEqualTo(BigDecimal value) {
			addCriterion("total_sell_price <>", value, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceGreaterThan(BigDecimal value) {
			addCriterion("total_sell_price >", value, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceGreaterThanOrEqualTo(BigDecimal value) {
			addCriterion("total_sell_price >=", value, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceLessThan(BigDecimal value) {
			addCriterion("total_sell_price <", value, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceLessThanOrEqualTo(BigDecimal value) {
			addCriterion("total_sell_price <=", value, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceIn(List<BigDecimal> values) {
			addCriterion("total_sell_price in", values, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceNotIn(List<BigDecimal> values) {
			addCriterion("total_sell_price not in", values, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("total_sell_price between", value1, value2, "totalSellPrice");
			return (Criteria) this;
		}

		public Criteria andTotalSellPriceNotBetween(BigDecimal value1, BigDecimal value2) {
			addCriterion("total_sell_price not between", value1, value2, "totalSellPrice");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table aventador.view_infinite_list
	 * @mbg.generated  Wed Dec 15 00:16:53 KST 2021
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
     * This class corresponds to the database table aventador.view_infinite_list
     *
     * @mbg.generated do_not_delete_during_merge Sun Dec 05 23:57:12 KST 2021
     */
    public static class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }
    }
}