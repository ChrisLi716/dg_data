package com.chris.dg_data;

public class Main {

	public static void main(String[] args) {
		String key = "string";
		if (key.contains("str")) {
			System.out.println("ture");
		}
		else {
			System.out.println("false");
		}


		System.out.println(String.format("my name is {0}, age {1}", "chris", 33));
	}
}
