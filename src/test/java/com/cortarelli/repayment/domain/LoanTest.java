package com.cortarelli.repayment.domain;

import com.cortarelli.repayment.AbstractTest;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;

public class LoanTest extends AbstractTest {

    private final static float LOAN_AMOUNT = 5000F;
    private final static float INTEREST_RATE = 5F;
    private final static int DURATION = 24;
    private final static OffsetDateTime START_DATE = OffsetDateTime.parse("2018-01-01T00:00:01Z");

    @Test
    public void shouldHaveConstructorAndGetters() {
        Loan loan = new Loan(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE);
        assertThat(loan.getLoanAmount(), Is.is(LOAN_AMOUNT));
        assertThat(loan.getNominalRate(), Is.is(INTEREST_RATE));
        assertThat(loan.getDuration(), Is.is(DURATION));
        assertThat(loan.getStartDate(), Is.is(START_DATE));
    }

    @Test
    public void shouldHaveEqualsMethod() {
        EqualsVerifier.forClass(Loan.class).verify();
    }

    @Test
    public void toStringShouldContainTheValuesOfTheClassFields() {
        assertThat(new Loan(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE), hasToStringContainingInAnyOrder(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE));
    }

    @Test
    public void shouldDeserializeJsonToEntity() throws Exception {
        Loan loan = new Loan(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE);
        String jsonLoan = "{\"loanAmount\": " + loan.getLoanAmount() + ", \"nominalRate\": " + loan.getNominalRate() +
                ", \"duration\": " + loan.getDuration() + ", \"startDate\": \"" + loan.getStartDate() + "\"}";

        assertThat(mapper.readValue(jsonLoan, Loan.class), Is.is(loan));
    }

    @Test
    public void verifyNullParametersParseJsonToEntity() throws Exception {
        Loan loan = new Loan(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE);
        String jsonMissingLoanAmount = "{\"nominalRate\": " + loan.getNominalRate() +
                ", \"duration\": " + loan.getDuration() + ", \"startDate\": \"" + loan.getStartDate() + "\"}";
        Assertions.assertThrows(MismatchedInputException.class, () -> { mapper.readValue(jsonMissingLoanAmount, Loan.class); });

        String jsonMissingNominalRate = "{\"loanAmount\": " + loan.getLoanAmount() +
                ", \"duration\": " + loan.getDuration() + ", \"startDate\": \"" + loan.getStartDate() + "\"}";
        Assertions.assertThrows(MismatchedInputException.class, () -> { mapper.readValue(jsonMissingNominalRate, Loan.class); });

        String jsonMissingDuration = "{\"loanAmount\": " + loan.getLoanAmount() + ", \"nominalRate\": " + loan.getNominalRate() +
                ", \"startDate\": \"" + loan.getStartDate() + "\"}";
        Assertions.assertThrows(MismatchedInputException.class, () -> { mapper.readValue(jsonMissingDuration, Loan.class); });

        String jsonMissingStartDate = "{\"loanAmount\": " + loan.getLoanAmount() + ", \"nominalRate\": " + loan.getNominalRate() +
                ", \"duration\": " + loan.getDuration() + "}";
        Assertions.assertThrows(MismatchedInputException.class, () -> { mapper.readValue(jsonMissingStartDate, Loan.class); });

    }

}
