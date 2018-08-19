package com.iota.mdxdoclet.data;

public class Example {

	private String example;
	private String responseOk;
	private String responseError;
	private String generator;
	
	public Example(String example, String responseOk, String generator) {
		this(example, responseOk, generator, null);
	}
	
	public Example(String example, String responseOk, String responseError, String generator) {
		this.example = example;
		this.responseOk = responseOk;
		this.generator = generator;
		this.responseError = responseError;
	}

	public String getExample() {
		return example;
	}
	
	public String getGenerator() {
		return generator;
	}

	public String getResponseOk() {
		return responseOk;
	}
	
	public boolean hasResponseError(){
		return responseError != null;
	}

	public String getResponseError() {
		return responseError;
	}
	
	
}
