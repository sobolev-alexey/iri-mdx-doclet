package com.iota.mdxdoclet.data;

public class ReturnParam {

	private String text;
	private String name;

	public ReturnParam(String name, String text) {
		this.name = name;
		this.text = text;
	}
	
	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
}
