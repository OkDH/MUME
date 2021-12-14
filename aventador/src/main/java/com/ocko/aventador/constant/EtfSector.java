package com.ocko.aventador.constant;

public enum EtfSector {
	
	S1("스페셜"),
	S2("기술/우량"),
	S3("중소형/소비재"),
	S4("기간산업"),
	S5("금융"),
	S6("보건");
	
	private String sectorName;
	
	EtfSector(String sectorName) {
		this.sectorName = sectorName;
	}
	
	public String getSectorName() {
		return sectorName;
	}

}
