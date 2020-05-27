package com.cortarelli.repayment.controller;

import com.cortarelli.repayment.domain.Loan;
import com.cortarelli.repayment.domain.Payment;
import com.cortarelli.repayment.exception.InvalidLoanParameterException;
import com.cortarelli.repayment.exception.NullLoanParameterException;
import com.cortarelli.repayment.service.CalculationRepaymentPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class RepaymentPlanController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private CalculationRepaymentPlanService calculationRepaymentPlanService;

    public RepaymentPlanController(CalculationRepaymentPlanService calculationRepaymentPlanService){
        this.calculationRepaymentPlanService = calculationRepaymentPlanService;
    }

    @PostMapping("/generate-plan")
    public List<Payment> calculateRepaymentPlan(@RequestBody Loan loan) throws InvalidLoanParameterException, NullLoanParameterException {
        return calculationRepaymentPlanService.calculateRepaymentPlan(loan);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handlerException(Exception e) {
        logger.info(e.getMessage(), e);
    }

    @ExceptionHandler(NullLoanParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerNullLoanParameterException(NullLoanParameterException e) {
        logger.info(e.getMessage(), e);
        return e.getMessage();
    }

    @ExceptionHandler(InvalidLoanParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlerInvalidLoanParameterException(InvalidLoanParameterException e) {
        logger.info(e.getMessage(), e);
        return e.getMessage();
    }

}
