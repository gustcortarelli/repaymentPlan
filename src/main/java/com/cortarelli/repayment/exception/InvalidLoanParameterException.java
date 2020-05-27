package com.cortarelli.repayment.exception;

public class InvalidLoanParameterException extends Exception {

    public InvalidLoanParameterException(String parameterName, int value) {
        super(String.format("The parameter %s is invalid, the assigned value should be greater than zero. Value: %s.", parameterName, value));
    }

    public InvalidLoanParameterException(String parameterName, float value) {
        super(String.format("The parameter %s is invalid, the assigned value should be greater than zero. Value: %s.", parameterName, value));
    }

}