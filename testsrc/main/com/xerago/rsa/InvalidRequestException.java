package com.xerago.rsa;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.xerago.rsa.dto.request.Request;

public class InvalidRequestException extends RuntimeException {
	
	private final Set<ConstraintViolation<Request>> constraintViolationSet;
	
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(Set<ConstraintViolation<Request>> set) {
		this.constraintViolationSet = set;
	}
	
	/**
	 * @return the constraintViolationSet
	 */
	public Set<ConstraintViolation<Request>> getConstraintViolationSet() {
		return constraintViolationSet;
	}

}
