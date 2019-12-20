package com.chris.dg_data.common;

public enum FileType {
	CSV("csv"),
	EXCEL("excel");

	private String extention;

	FileType(String extention) {
		this.extention = extention;
	}

	public String getValue() {
		return this.extention;
	}
}
