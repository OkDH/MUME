package com.ocko.aventador.constant;

public enum MarketIndex {
	DJI("^DJI"), // 다우산업
	IXIC("^IXIC"), // 나스닥
	GSPC("^GSPC"), // S&P500
	SOX("^SOX"), // 필라델피아 반도체
	
	ESF("ES=F"), // 다운산업 선물
	NQF("NQ=F"), // 나스닥 선물
	YMF("YM=F"), // S&P 500 선물
	RTYF("RTY=F"), // 러셀 2000 선물
	
	KRWX("KRW=X"), // 환율
	CLF("CL=F"), // 원가
	GCF("GC=F"), // 금
	SIF("SI=F"), // 은
	
	TNX("^TNX"), // 미국 국채 10년물 금리
	VIX("^VIX"), // 변동성지수
	KS11("^KS11"), // 코스피
	KQ11("^KQ11"), // 코스닥
	
	CNSS("000001.SS"), // 상해종합주가지수
	N225("^N225"), // 니케이 225
	STOXX("^STOXX50E"), // 유로스톡스50
	BTCKRW("BTC-KRW"); // 비트코인
	
	private final String symbol;
	
	MarketIndex(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
