package com.huntkey.rx.sceo.form.common.model;

public class QueryParam {
	private String edmName;
	
	private Search search;
	
	public String getEdmName() {
		return edmName;
	}

	public void setEdmName(String edmName) {
		this.edmName = edmName;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "QueryParam [edmName=" + edmName + ", search=" + search + "]";
	}
	
}
