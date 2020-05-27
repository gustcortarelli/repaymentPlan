package com.cortarelli.repayment.service;

import com.cortarelli.repayment.exception.InvalidLoanParameterException;
import com.cortarelli.repayment.exception.NullLoanParameterException;
import com.cortarelli.repayment.utils.Utils;
import com.cortarelli.repayment.domain.Loan;
import com.cortarelli.repayment.domain.Payment;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculationRepaymentPlanService {

    private static final int DAYS_IN_MONTH = 30;
    private static final int DAYS_IN_YEAR = 360;

    /**
     * Regarding the loan parameters, calculate the annuity payment
     * @param duration
     *          number of installments in months
     * @param nominalRate
     *          annual interest rate
     * @param loanAmount
     *          principal amount
     * @return
     */
    public float annuityCalculation(int duration, float nominalRate, float loanAmount) {
        // convert to monthly rate
        double monthlyRate = (nominalRate / 100) / 12;
        double annuityPayment = (monthlyRate * loanAmount / (1 - Math.pow(1 + monthlyRate, duration * -1)));
        return Utils.round(annuityPayment);
    }

    /**
     * Calculate interest value
     * @param nominalRate
     * @param initialOutstandingPrincipal
     * @return interest
     */
    public float calculateInterest(float nominalRate, float initialOutstandingPrincipal) {
        return Utils.round(((nominalRate / 100) * DAYS_IN_MONTH * initialOutstandingPrincipal) / DAYS_IN_YEAR);
    }

    /**
     * calculate principal value. When principal amount result exceeds the outstandingPrincipal value,
     * take outstandingPrincipal value instead.
     *
     * @param annuity
     * @param interest
     * @param outstandingPrincipal
     * @return principal
     */
    public float calculatePrincipalValue(float annuity, float interest, float outstandingPrincipal) {
        float principal = annuity - interest;
        if (principal > outstandingPrincipal) {
            return outstandingPrincipal;
        }
        return Utils.round(principal);
    }

    /**
     * Calculate the borrower payment amount (Annuity)
     * @param principal
     * @param interest
     * @return Annuity (Borrower Payment Amount)
     */
    public float calculateBorrowerPaymentAmount(float principal, float interest) {
        return Utils.round(principal + interest);
    }

    /**
     * Calculate the payment day according to the initial date and the payment number
     * @param initialDate
     *          day of first payment
     * @param paymentNumber
     *          the number of payment (starts on zero)
     * @return
     */
    public OffsetDateTime calculatePaymentDate(OffsetDateTime initialDate, int paymentNumber) {
        return initialDate.plusMonths(paymentNumber);
    }

    /**
     * Check if all {@link Loan} parameters are valid
     *
     * @param loan
     * @throws InvalidLoanParameterException
     */
    public void validateLoanParameters(Loan loan) throws InvalidLoanParameterException, NullLoanParameterException {
        if (loan.getDuration() <= 0) {
            throw new InvalidLoanParameterException("duration", loan.getDuration());
        }
        if (loan.getLoanAmount() <= 0.0) {
            throw new InvalidLoanParameterException("loanAmount", loan.getLoanAmount());
        }
        if(loan.getNominalRate() <= 0.0) {
            throw new InvalidLoanParameterException("nominalRate", loan.getLoanAmount());
        }
        if (loan.getStartDate() == null) {
            throw new NullLoanParameterException("startDate");
        }
    }

    /**
     * Calculate payment according to the loan parameters
     * @param loan
     * @return List of {@link Payment}
     */
    public List<Payment> calculateRepaymentPlan(Loan loan) throws InvalidLoanParameterException, NullLoanParameterException {

        validateLoanParameters(loan);

        float annuity = annuityCalculation(loan.getDuration(), loan.getNominalRate(), loan.getLoanAmount());
        List<Payment> paymentPlan = new ArrayList<>();
        float remainingOutstandingPrincipal = loan.getLoanAmount();

        for (int i = 0; i < loan.getDuration(); i++) {

            float interest = calculateInterest(loan.getNominalRate(), remainingOutstandingPrincipal);
            float principal = calculatePrincipalValue(annuity, interest, remainingOutstandingPrincipal);
            float borrowerPaymentAmount = calculateBorrowerPaymentAmount(principal, interest);
            float initialOutstandingPrincipal = remainingOutstandingPrincipal;
            remainingOutstandingPrincipal = Utils.round(remainingOutstandingPrincipal - principal);
            OffsetDateTime paymentDate = calculatePaymentDate(loan.getStartDate(), i);

            paymentPlan.add(new Payment(
                    borrowerPaymentAmount,
                    paymentDate,
                    initialOutstandingPrincipal,
                    interest,
                    principal,
                    remainingOutstandingPrincipal
            ));
        }

        return paymentPlan;

    }

}
