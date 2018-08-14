package com.iota.mdxdoclet;

public enum ApiCalls {
	NODE_INFO("getNodeInfo"),
	GET_NEIGHBORS("getNeighbors"),
	ADD_NEIGHBORS("addNeighbors"),
	REMOVE_NEIGHBORS("removeNeighbors"),
	GET_TIPS("getTips"),
	FIND_TRANSACTIONS("findTransactions"),
	GET_TRYTES("getTrytes"),
	GET_INCLUSION_STATE("getInclusionStates"),
	GET_BALANCE("getBalances"),
	GET_TRANSACTION_APPROVE("getTransactionsToApprove"),
	ATTACH_TANGLE("attachToTangle"),
	INTERRUPT_ATTACH("interruptAttachingToTangle"),
	BROADCAST_TRANSACTION("broadcastTransactions"),
	STORE_TRANSACTION("storeTransactions"),
	WERE_ADDRESSES_SPENT_FROM("wereAddressesSpentFrom");
	
	private String command;
	
	ApiCalls(String command){
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	
	public String getCommandStatement() {
		return command + "Statement";
	}
	
	public static boolean isApiCall(String function) {
		for (ApiCalls api : ApiCalls.values()) {
			if (function.equals(api.getCommandStatement())) {
				return true;
			}
		}
		return false;
	}
}