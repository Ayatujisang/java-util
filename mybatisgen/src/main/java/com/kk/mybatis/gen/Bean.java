package com.kk.mybatis.gen;

public class Bean {
	private String column;  // user_id
	private String field;  // userId
	private String type;// 类型  Integer,Long等

	public Bean() {
	}


	public Bean(String column, String field, String type) {
		this.column = column;
		this.field = field;
		this.type = type;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Bean{" +
				"column='" + column + '\'' +
				", field='" + field + '\'' +
				", type='" + type + '\'' +
				'}';
	}
}
