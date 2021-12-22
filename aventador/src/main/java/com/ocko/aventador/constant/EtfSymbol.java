package com.ocko.aventador.constant;

public enum EtfSymbol {
	TQQQ(EtfSector.S1, 60),
	TECL(EtfSector.S1, 60),
	SOXL(EtfSector.S1, 65),
	BULZ(EtfSector.S1, 65),
	
	WEBL(EtfSector.S2, 60),
	UPRO(EtfSector.S2, 55),
	HIBL(EtfSector.S2, 55),
	FNGU(EtfSector.S2, 55),
	UDOW(EtfSector.S2, 50),
	
	WANT(EtfSector.S3, 55),
	TNA(EtfSector.S3, 50),
	NAIL(EtfSector.S3, 50),
	RETL(EtfSector.S3, 50),
	MIDU(EtfSector.S3, 45),
	DRN(EtfSector.S3, 40),
	
	DUSL(EtfSector.S4, 40),
	TPOR(EtfSector.S4, 40),
	DFEN(EtfSector.S4, 40),
	UTSL(EtfSector.S4, 35),
	
	FAS(EtfSector.S5, 45),
	DPST(EtfSector.S5, 35),
	BNKU(EtfSector.S5, 35),
	
	LABU(EtfSector.S6, 45),
	CURE(EtfSector.S6, 45),
	PILL(EtfSector.S6, 45);
	
	private final EtfSector sector;
	private int defaultRsi;
	
	EtfSymbol(EtfSector sector, int rsi) {
		this.sector = sector;
		this.defaultRsi = rsi;
	}
	
	public EtfSector getSector() {
		return sector;
	}
	public int getDefaultRsi() {
		return defaultRsi;
	}
}
