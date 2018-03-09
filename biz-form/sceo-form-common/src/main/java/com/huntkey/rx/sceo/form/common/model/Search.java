package com.huntkey.rx.sceo.form.common.model;

public class Search {
	
	private Condition[] conditions;
	
	public Search(){}
	
	public Search(Condition[] conditions) {
		this.conditions = conditions;
	}

	public Condition[] getConditions() {
		return conditions;
	}

	public void setConditions(Condition[] conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return "Search [conditions=" + conditions + "]";
	}
}
