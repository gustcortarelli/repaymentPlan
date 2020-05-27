package com.cortarelli.repayment.domain;

import com.cortarelli.repayment.AbstractTest;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;

public class PaymentTest extends AbstractTest {

    private static final float BORROWER_PAYMENT_AMOUNT = 219.36F;
    private static final OffsetDateTime DATE = OffsetDateTime.parse("2018-01-01T00:00:00Z");
    private static final float INITIAL_OUTSTANDING_PRINCIPAL = 5000.0F;
    private static final float INTEREST = 20.83F;
    private static final float PRINCIPAL = 198.53F;
    private static final float REMAINING_OUTSTANDING_PRINCIPAL = 4801.47F;

    @Test
    public void shouldHaveConstructorAndGetters() {
        Payment payment = new Payment(BORROWER_PAYMENT_AMOUNT, DATE, INITIAL_OUTSTANDING_PRINCIPAL,
                INTEREST, PRINCIPAL, REMAINING_OUTSTANDING_PRINCIPAL);
        assertThat(payment.getBorrowerPaymentAmount(), Is.is(BORROWER_PAYMENT_AMOUNT));
        assertThat(payment.getDate(), Is.is(DATE));
        assertThat(payment.getInitialOutstandingPrincipal(), Is.is(INITIAL_OUTSTANDING_PRINCIPAL));
        assertThat(payment.getInterest(), Is.is(INTEREST));
        assertThat(payment.getPrincipal(), Is.is(PRINCIPAL));
        assertThat(payment.getRemainingOutstandingPrincipal(), Is.is(REMAINING_OUTSTANDING_PRINCIPAL));
    }

    @Test
    public void shouldHaveEqualsMethod() {
        EqualsVerifier.forClass(Payment.class).verify();
    }

    @Test
    public void toStringShouldContainTheValuesOfTheClassFields() {
        assertThat(new Payment(BORROWER_PAYMENT_AMOUNT, DATE, INITIAL_OUTSTANDING_PRINCIPAL, INTEREST, PRINCIPAL,
                        REMAINING_OUTSTANDING_PRINCIPAL), hasToStringContainingInAnyOrder(BORROWER_PAYMENT_AMOUNT, DATE,
                INITIAL_OUTSTANDING_PRINCIPAL, INTEREST, PRINCIPAL, REMAINING_OUTSTANDING_PRINCIPAL));
    }

    @Test
    public void shouldDeserializeJsonToEntity() throws Exception {
        Payment payment = new Payment(BORROWER_PAYMENT_AMOUNT, DATE, INITIAL_OUTSTANDING_PRINCIPAL, INTEREST,
                PRINCIPAL, REMAINING_OUTSTANDING_PRINCIPAL);
        String jsonPaymentPlan = "{\"borrowerPaymentAmount\": " + payment.getBorrowerPaymentAmount() +
                ", \"date\": \"" + payment.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")) + "\"" +
                ", \"initialOutstandingPrincipal\": " + payment.getInitialOutstandingPrincipal() +
                ", \"interest\": " + payment.getInterest() +
                ", \"principal\": " + payment.getPrincipal() +
                ", \"remainingOutstandingPrincipal\": " + payment.getRemainingOutstandingPrincipal() + "}";

        JSONAssert.assertEquals(jsonPaymentPlan, mapper.writeValueAsString(payment), true);
    }

}
