package com.cortarelli.repayment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Loan parameters that will be used to calculate the repayment plan
 */
public final class Loan {

    private final float loanAmount;
    private final float nominalRate;
    private final int duration;
    private final OffsetDateTime startDate;

    public Loan(
            @JsonProperty(value = "loanAmount", required = true) float loanAmount,
            @JsonProperty(value = "nominalRate", required = true) float nominalRate,
            @JsonProperty(value = "duration", required = true) int duration,
            @JsonProperty(value = "startDate", required = true) OffsetDateTime startDate) {
        this.loanAmount = loanAmount;
        this.nominalRate = nominalRate;
        this.duration = duration;
        this.startDate = startDate;
    }

    /**
     * The principal amount
     * @return
     */
    public float getLoanAmount() {
        return loanAmount;
    }

    /**
     * Annual interest rate
     * @return
     */
    public float getNominalRate() {
        return nominalRate;
    }

    /**
     * Number of installments in months
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Date of the first Disbursement/Payout
     * @return
     */
    public OffsetDateTime getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) {
            return false;
        }
        Loan loan = (Loan) o;
        return Objects.equals(loanAmount, loan.loanAmount) &&
                Objects.equals(nominalRate, loan.nominalRate) &&
                Objects.equals(duration, loan.duration) &&
                Objects.equals(startDate, loan.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanAmount, nominalRate, duration, startDate);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanAmount=" + loanAmount +
                ", interestRate=" + nominalRate +
                ", duration=" + duration +
                ", startDate=" + startDate +
                '}';
    }
}
