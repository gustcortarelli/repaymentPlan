package com.cortarelli.repayment.service;

import com.cortarelli.repayment.domain.Loan;
import com.cortarelli.repayment.domain.Payment;
import com.cortarelli.repayment.exception.InvalidLoanParameterException;
import com.cortarelli.repayment.exception.NullLoanParameterException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.OffsetDateTime;
import java.util.List;

public class CalculationRepaymentPlanServiceTest {

    private CalculationRepaymentPlanService calculationService;

    private static final float  LOAN_AMOUNT = 5000;
    private static final float  NOMINAL_RATE = 5F;
    private static final int    DURATION = 24;
    private static final float  ANNUITY_PAYMENT = 219.36F;

    private static final float  OUTSTANDING_PRINCIPAL = 5000;
    private static final float  INTEREST_VALUE = 20.83F;
    private static final float  PRINCIPAL_VALUE = 198.53F;

    private static final float  OUTSTANDING_PRINCIPAL_2 = 4801.47F;
    private static final float  INTEREST_VALUE_2 = 20.01F;
    private static final float  PRINCIPAL_VALUE_2 = 199.35F;

    private static final float  OUTSTANDING_PRINCIPAL_24 = 218.37F;
    private static final float  INTEREST_VALUE_24 = 0.91F;
    private static final float  PRINCIPAL_VALUE_24 = 218.37F;
    private static final float  ANNUITY_PAYMENT_24 = 219.28F;

    private static final OffsetDateTime START_DATE = (OffsetDateTime.now());
    private static final OffsetDateTime START_DATE_2 = START_DATE.plusMonths(1);
    private static final OffsetDateTime START_DATE_3 = START_DATE.plusMonths(2);
    private static final OffsetDateTime START_DATE_24 = START_DATE.plusMonths(23);

    private static final Loan LOAN = new Loan(LOAN_AMOUNT, NOMINAL_RATE, DURATION, START_DATE);

    private static final Payment PAYMENT_1 = new Payment(ANNUITY_PAYMENT, START_DATE,
            LOAN_AMOUNT, INTEREST_VALUE, PRINCIPAL_VALUE, OUTSTANDING_PRINCIPAL - PRINCIPAL_VALUE);
    private static final Payment PAYMENT_2 = new Payment(ANNUITY_PAYMENT, START_DATE_2,
            OUTSTANDING_PRINCIPAL_2, INTEREST_VALUE_2, PRINCIPAL_VALUE_2, OUTSTANDING_PRINCIPAL_2 - PRINCIPAL_VALUE_2);
    private static final Payment PAYMENT_24 = new Payment(ANNUITY_PAYMENT_24, START_DATE_24,
            PRINCIPAL_VALUE_24, INTEREST_VALUE_24, PRINCIPAL_VALUE_24, 0F);

    @Before
    public void init() {
        calculationService = new CalculationRepaymentPlanService();
    }


    @Test
    public void checkAnnuityPayment() {
        Assert.assertEquals(ANNUITY_PAYMENT, calculationService.annuityCalculation(DURATION, NOMINAL_RATE, OUTSTANDING_PRINCIPAL), 0);
    }

    @Test
    public void checkInterestCalculation() {
        Assert.assertEquals(INTEREST_VALUE, calculationService.calculateInterest(NOMINAL_RATE, OUTSTANDING_PRINCIPAL), 0);
        Assert.assertEquals(INTEREST_VALUE_2, calculationService.calculateInterest(NOMINAL_RATE, OUTSTANDING_PRINCIPAL_2), 0);
        Assert.assertEquals(INTEREST_VALUE_24, calculationService.calculateInterest(NOMINAL_RATE, OUTSTANDING_PRINCIPAL_24), 0);
    }

    @Test
    public void checkPrincipalCalculation() {
        Assert.assertEquals(PRINCIPAL_VALUE, calculationService.calculatePrincipalValue(ANNUITY_PAYMENT, INTEREST_VALUE, OUTSTANDING_PRINCIPAL), 0);
        Assert.assertEquals(PRINCIPAL_VALUE_2, calculationService.calculatePrincipalValue(ANNUITY_PAYMENT, INTEREST_VALUE_2, OUTSTANDING_PRINCIPAL_2), 0);
        Assert.assertEquals(PRINCIPAL_VALUE_24, calculationService.calculatePrincipalValue(ANNUITY_PAYMENT, INTEREST_VALUE_24, OUTSTANDING_PRINCIPAL_24), 0);
    }

    @Test
    public void checkBorrowerPaymentAmount() {
        Assert.assertEquals(ANNUITY_PAYMENT, calculationService.calculateBorrowerPaymentAmount(PRINCIPAL_VALUE, INTEREST_VALUE), 0);
        Assert.assertEquals(ANNUITY_PAYMENT_24, calculationService.calculateBorrowerPaymentAmount(PRINCIPAL_VALUE_24, INTEREST_VALUE_24), 0);
    }

    @Test
    public void checkCalculatePaymentDate() {
        Assert.assertEquals(START_DATE, calculationService.calculatePaymentDate(START_DATE, 0));
        Assert.assertEquals(START_DATE_3, calculationService.calculatePaymentDate(START_DATE, 2));
        Assert.assertEquals(START_DATE_24, calculationService.calculatePaymentDate(START_DATE, 23));
    }

    @Test
    public void checkCalculateRepaymentPlan() throws Exception {
        List<Payment> paymentPlan = calculationService.calculateRepaymentPlan(LOAN);
        Assert.assertEquals(LOAN.getDuration(), paymentPlan.size());
        Assert.assertEquals(PAYMENT_1, paymentPlan.get(0));
        Assert.assertEquals(PAYMENT_2, paymentPlan.get(1));
        Assert.assertEquals(PAYMENT_24, paymentPlan.get(23));
    }

    @Test
    public void validLoanParameters() {
        Assertions.assertDoesNotThrow(() -> { calculationService.validateLoanParameters(LOAN); });
    }

    @Test
    public void invalidLoanParameterLoanAmount() {
        Loan loanZero = new Loan(0F, NOMINAL_RATE, DURATION, START_DATE);
        Assertions.assertThrows(InvalidLoanParameterException.class, () -> { calculationService.validateLoanParameters(loanZero); });

        Loan loanNegative = new Loan(-1F, NOMINAL_RATE, DURATION, START_DATE);
        Assertions.assertThrows(InvalidLoanParameterException.class,() -> { calculationService.validateLoanParameters(loanNegative); });
    }

    @Test
    public void invalidLoanParameterNominalRate() {
        Loan loanZero = new Loan(LOAN_AMOUNT, 0F, DURATION, START_DATE);
        Assertions.assertThrows(InvalidLoanParameterException.class, () -> { calculationService.validateLoanParameters(loanZero); });

        Loan loanNegative = new Loan(LOAN_AMOUNT, -1F, DURATION, OffsetDateTime.now().plusMonths(1));
        Assertions.assertThrows(InvalidLoanParameterException.class,() -> { calculationService.validateLoanParameters(loanNegative); });
    }

    @Test
    public void invalidLoanParameterDuration() {
        Loan loanZero = new Loan(LOAN_AMOUNT, NOMINAL_RATE, 0, START_DATE);
        Assertions.assertThrows(InvalidLoanParameterException.class, () -> { calculationService.validateLoanParameters(loanZero); });

        Loan loanNegative = new Loan(LOAN_AMOUNT, NOMINAL_RATE, -1, START_DATE);
        Assertions.assertThrows(InvalidLoanParameterException.class,() -> { calculationService.validateLoanParameters(loanNegative); });
    }

    @Test
    public void invalidLoanParameterStartDate() {
        Loan loanNullValue = new Loan(LOAN_AMOUNT, NOMINAL_RATE, DURATION, null);
        Assertions.assertThrows(NullLoanParameterException.class,() -> { calculationService.validateLoanParameters(loanNullValue); });
    }

}
