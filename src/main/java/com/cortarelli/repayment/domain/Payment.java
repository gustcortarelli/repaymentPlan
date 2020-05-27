package com.cortarelli.repayment.domain;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * This object represent one payment of repayment plan
 */
public final class Payment {

    private final float borrowerPaymentAmount;
    private final OffsetDateTime date;
    private final float initialOutstandingPrincipal;
    private final float interest;
    private final float principal;
    private final float remainingOutstandingPrincipal;

    public Payment(float borrowerPaymentAmount, OffsetDateTime date, float initialOutstandingPrincipal, float interest, float principal, float remainingOutstandingPrincipal) {
        this.borrowerPaymentAmount = borrowerPaymentAmount;
        this.date = date;
        this.initialOutstandingPrincipal = initialOutstandingPrincipal;
        this.interest = interest;
        this.principal = principal;
        this.remainingOutstandingPrincipal = remainingOutstandingPrincipal;
    }

    /**
     * Borrower Payment Amount (principal + interest)
     * @return
     */
    public float getBorrowerPaymentAmount() {
        return borrowerPaymentAmount;
    }

    /**
     * Payment date
     * @return
     */
    public OffsetDateTime getDate() {
        return date;
    }

    /**
     * Remaining value to be paid from the loan amount without interest
     * @return
     */
    public float getInitialOutstandingPrincipal() {
        return initialOutstandingPrincipal;
    }

    /**
     * Interest related on this payment
     * @return
     */
    public float getInterest() {
        return interest;
    }

    /**
     * Value without interest that will deduct the remaining loan amount
     * @return
     */
    public float getPrincipal() {
        return principal;
    }

    /**
     * Remaining value to be paid after this payment (initialOutstandingPrincipal - principal)
     * @return
     */
    public float getRemainingOutstandingPrincipal() {
        return remainingOutstandingPrincipal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment that = (Payment) o;
        return Objects.equals(borrowerPaymentAmount, that.borrowerPaymentAmount) &&
                Objects.equals(date, that.date) &&
                Objects.equals(initialOutstandingPrincipal, that.initialOutstandingPrincipal) &&
                Objects.equals(interest, that.interest) &&
                Objects.equals(principal, that.principal) &&
                Objects.equals(remainingOutstandingPrincipal, that.remainingOutstandingPrincipal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(borrowerPaymentAmount, date, initialOutstandingPrincipal, interest, principal, remainingOutstandingPrincipal);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "borrowerPaymentAmount=" + borrowerPaymentAmount +
                ", date=" + date +
                ", initialOutstandingPrincipal=" + initialOutstandingPrincipal +
                ", interest=" + interest +
                ", principal=" + principal +
                ", remainingOutstandingPrincipal=" + remainingOutstandingPrincipal +
                '}';
    }
}
