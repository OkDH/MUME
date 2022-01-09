package com.ocko.aventador.constant;

public enum EtfSymbol {
	TQQQ(EtfSector.S1, 60, false),
	TECL(EtfSector.S1, 60, false),
	SOXL(EtfSector.S1, 65, false),
	BULZ(EtfSector.S1, 65, false),
	
	WEBL(EtfSector.S2, 60, true),
	UPRO(EtfSector.S2, 55, false),
	HIBL(EtfSector.S2, 55, true),
	FNGU(EtfSector.S2, 55, false),
	UDOW(EtfSector.S2, 50, false),
	
	WANT(EtfSector.S3, 55, true),
	TNA(EtfSector.S3, 50, false),
	NAIL(EtfSector.S3, 50, true),
	RETL(EtfSector.S3, 50, true),
	MIDU(EtfSector.S3, 45, true),
	DRN(EtfSector.S3, 40, true),
	
	DUSL(EtfSector.S4, 40, true),
	TPOR(EtfSector.S4, 40, true),
	DFEN(EtfSector.S4, 40, false),
	UTSL(EtfSector.S4, 35, true),
	
	FAS(EtfSector.S5, 45, false),
	DPST(EtfSector.S5, 35, false),
	BNKU(EtfSector.S5, 35, true),
	
	LABU(EtfSector.S6, 45, false),
	CURE(EtfSector.S6, 45, true),
	PILL(EtfSector.S6, 45, true);
	
	private final EtfSector sector;
	private int defaultRsi;
	private final boolean isWarn;
	
	EtfSymbol(EtfSector sector, int rsi, boolean isWarn) {
		this.sector = sector;
		this.defaultRsi = rsi;
		this.isWarn = isWarn;
	}
	
	public EtfSector getSector() {
		return sector;
	}
	public int getDefaultRsi() {
		return defaultRsi;
	}
	public boolean isWran() {
		return isWarn;
	}
}
