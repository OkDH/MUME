package com.ocko.aventador.constant;

public enum MarketIndex {
	DJI("^DJI"),
	IXIC("^IXIC"),
	GSPC("^GSPC"),
	SOX("^SOX"),
	
	ESF("ES=F"),
	NQF("NQ=F"),
	YMF("YM=F"),
	RTYF("RTY=F"),
	
	KRWX("KRW=X"),
	CLF("CL=F"),
	GCF("GC=F"),
	SIF("SI=F"),
	
	TNX("^TNX"), 
	VIX("^VIX"), 
	KS11("^KS11"),
	KQ11("^KQ11"),
	
	CNSS("000001.SS"),
	N225("^N225"),
	GDAXI("^GDAXI"),
	BTCKRW("BTC-KRW");
	
	private final String symbol;
	
	MarketIndex(String symbol){
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
}
