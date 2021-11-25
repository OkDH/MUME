package com.ocko.aventador.constant;

public enum EtfSymbol {
	TQQQ(EtfSector.S1),
	TECL(EtfSector.S1),
	SOXL(EtfSector.S1),
	BULZ(EtfSector.S1),
	
	WEBL(EtfSector.S2),
	UPRO(EtfSector.S2),
	HIBL(EtfSector.S2),
	FNGU(EtfSector.S2),
	UDOW(EtfSector.S2),
	
	WANT(EtfSector.S3),
	TNA(EtfSector.S3),
	NAIL(EtfSector.S3),
	RETL(EtfSector.S3),
	MIDU(EtfSector.S3),
	DRN(EtfSector.S3),
	
	DUSL(EtfSector.S4),
	TPOR(EtfSector.S4),
	DFEN(EtfSector.S4),
	UTSL(EtfSector.S4),
	
	FAS(EtfSector.S5),
	DPST(EtfSector.S5),
	BNKU(EtfSector.S5),
	
	LABU(EtfSector.S6),
	CURE(EtfSector.S6),
	PILL(EtfSector.S6);
	
	private final EtfSector sector;
	
	EtfSymbol(EtfSector sector) {
		this.sector = sector;
	}
	
	public EtfSector sector() {
		return sector;
	}
}
