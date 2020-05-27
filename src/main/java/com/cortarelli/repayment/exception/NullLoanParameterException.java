package com.cortarelli.repayment.exception;

public class NullLoanParameterException extends Exception {

    public NullLoanParameterException(String parameterName) {
        super(String.format("The parameter %s is invalid, the assigned value cannot be not null.", parameterName));
    }

}
