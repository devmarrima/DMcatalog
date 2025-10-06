package com.devmarrima.DMcatalog.resources.exceptions;

public class FieldMessage {
	
	private String FieldName;
	private String message;
	
	public FieldMessage() {

}

	public FieldMessage(String fieldName, String message) {
		FieldName = fieldName;
		this.message = message;
	}

	public String getFieldName() {
		return FieldName;
	}

	public void setFieldName(String fieldName) {
		FieldName = fieldName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
