package com.cg.mypaymentapp.exception;

@SuppressWarnings("serial")
public class InvalidInputException extends RuntimeException {

	public InvalidInputException(String msg) {
		super(msg);
	}
}
