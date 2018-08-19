package com.iota.mdxdoclet.example;

import java.util.List;

import com.iota.mdxdoclet.ApiCall;
import com.sun.javadoc.MethodDoc;

public interface Export {
	
	/**
	 * Generates an example based on parameters from the Statement in API
	 * @param command The Statement Method
	 * @param api The Related API command
	 * @return
	 */
	String generateExample(MethodDoc command, ApiCall api);

	
	/**
	 * Generates basic response data based on MethodDoc return value
	 * @param command The command the response is from (Statement in API)
	 * @param api The API command
	 * @return
	 */
	String generateResponse(MethodDoc command, ApiCall api);
	
	/**
	 * Generates response data based on a list of methods
	 * @param command The command the response is from (Statement in API)
	 * @param api The API command
	 * @param methods The list of methods (public getters with javadoc)
	 * @return
	 */
	String generateResponse(MethodDoc command, ApiCall api, List<MethodDoc> methods);
	
	/**
	 * Generates an example error
	 * @return
	 */
	String generateError();
	
	/**
	 * The name of this generator (cURL, python, NodeJS)
	 * @return
	 */
	String getName();

	
}
