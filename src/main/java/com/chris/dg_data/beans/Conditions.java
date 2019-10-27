package com.chris.dg_data.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @ToString @AllArgsConstructor public class Conditions {

	private String filePath;

	private int beginDay;

	private int endDay;

	public Conditions(int beginDay, int endDay) {
		this.beginDay = beginDay;
		this.endDay = endDay;
	}
}
