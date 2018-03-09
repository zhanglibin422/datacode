package com.huntkey.rx.sceo.form.common.model;

public class Condition {
	
	private String attr;
	
	private String operator;
	
	private String value;

	public Condition(String attr, String operator, String value) {
		super();
		this.attr = attr;
		this.operator = operator;
		this.value = value;
	}

	public String getAttr() {
		return attr;
	}

	public void setAttr(String attr) {
		this.attr = attr;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Condition [attr=" + attr + ", operator=" + operator + ", value=" + value + "]";
	}
}
