package com.cortarelli.repayment.controller;

import com.cortarelli.repayment.AbstractTest;
import com.cortarelli.repayment.domain.Loan;
import com.cortarelli.repayment.domain.Payment;
import com.cortarelli.repayment.exception.InvalidLoanParameterException;
import com.cortarelli.repayment.exception.NullLoanParameterException;
import com.cortarelli.repayment.service.CalculationRepaymentPlanService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RepaymentPlanControllerTest extends AbstractTest {

    @Mock
    private CalculationRepaymentPlanService calculationRepaymentPlanService;
    private RepaymentPlanController repaymentPlanController;
    private MockMvc mockMvc;

    private final static String GENERATE_PLAN = "generate-plan";

    private final static float LOAN_AMOUNT = 5000F;
    private final static float INTEREST_RATE = 5F;
    private final static int DURATION = 1;
    private final static OffsetDateTime START_DATE = OffsetDateTime.parse("2018-01-01T00:00:01Z");

    private static final float BORROWER_PAYMENT_AMOUNT = 5020.83F;
    private static final OffsetDateTime DATE = OffsetDateTime.parse("2018-01-01T00:00:00Z");
    private static final float INITIAL_OUTSTANDING_PRINCIPAL = 5000.0F;
    private static final float INTEREST = 20.83F;
    private static final float PRINCIPAL = 5000F;
    private static final float REMAINING_OUTSTANDING_PRINCIPAL = 0F;

    private List<Payment> paymentPlan;


    @Before
    public void init() {
        repaymentPlanController = new RepaymentPlanController(calculationRepaymentPlanService);
        paymentPlan = Arrays.asList(new Payment(BORROWER_PAYMENT_AMOUNT, DATE, INITIAL_OUTSTANDING_PRINCIPAL,
                INTEREST, PRINCIPAL, REMAINING_OUTSTANDING_PRINCIPAL));

        mockMvc = MockMvcBuilders.standaloneSetup(repaymentPlanController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();
    }

    @Test
    public void testCalculateRepaymentPlan() throws Exception {
        Loan loan = new Loan(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE);

        Mockito.when(calculationRepaymentPlanService.calculateRepaymentPlan(loan)).thenReturn(paymentPlan);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + GENERATE_PLAN).content(toJson(loan)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers
                                .content()
                                .json(toJson(paymentPlan))
                );

        Mockito.verify(calculationRepaymentPlanService).calculateRepaymentPlan(loan);
    }

    @Test
    public void testNullLoanParameterExceptionHandler() throws Exception {
        Loan loan = new Loan(LOAN_AMOUNT, INTEREST_RATE, DURATION, START_DATE);

        Mockito.doThrow(new NullLoanParameterException("loan amount")).when(calculationRepaymentPlanService).calculateRepaymentPlan(loan);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + GENERATE_PLAN).content(toJson(loan)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(calculationRepaymentPlanService).calculateRepaymentPlan(loan);
    }

    @Test
    public void testInvalidLoanParameterExceptionHandler() throws Exception {
        Loan loan = new Loan(0F, INTEREST_RATE, DURATION, START_DATE);

        Mockito.doThrow(new InvalidLoanParameterException("loan amount", 0F)).when(calculationRepaymentPlanService).calculateRepaymentPlan(loan);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + GENERATE_PLAN).content(toJson(loan)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(calculationRepaymentPlanService).calculateRepaymentPlan(loan);
    }

    @Test
    public void testRunExceptionHandler() throws Exception {
        Loan loan = new Loan(5000F, INTEREST_RATE, DURATION, START_DATE);

        Mockito.doThrow(new RuntimeException("ERROR")).when(calculationRepaymentPlanService).calculateRepaymentPlan(loan);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + GENERATE_PLAN).content(toJson(loan)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());

        Mockito.verify(calculationRepaymentPlanService).calculateRepaymentPlan(loan);
    }

}
