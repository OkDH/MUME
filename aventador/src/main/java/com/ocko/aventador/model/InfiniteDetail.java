package com.ocko.aventador.model;

import java.math.BigDecimal;

import com.ocko.aventador.dao.model.aventador.ViewInfiniteList;

public class InfiniteDetail extends ViewInfiniteList {

	// 매입금액
	public BigDecimal getBuyPrice() {
		return getAveragePrice().multiply(new BigDecimal(getHoldingQuantity()));
	}
}
