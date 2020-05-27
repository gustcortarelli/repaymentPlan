# Loan Calculation API
This API is useful to calculate the repayment plan, regarding the loan parameters assigned
by the user. 

## How to start the application?

This application will be ran by default on 8090 port, it can be changed on application.yml
file, that is on "src/main/resources". 

After assure that the application port configuration, run the following command on 
project root folder:  

```
mvnw clean install
```

This command will build the artifact. After build process finish, execute the command
below to run the application:

```
java -jar target/repayment-0.0.1-SNAPSHOT.jar
``` 

If you see a message like this:

> Started RepaymentApplication in X seconds

you will are able to perform the requests to the application. 

## Using API - Calculating a Repayment Plan

In order to calculate a payment plan, it is necessary to perform a POST request
to the method http://&lt;projecturl&gt;:&lt;port&gt;/generate-plan (e.g. 
http://localhost:8090/generate-plan) with the following body content structure:

```
{
    "loanAmount": 5000,
    "nominalRate": 5.0,
    "duration": 24,
    "startDate": "2018-01-01T00:00:01Z"
}
```
#### Parameter description

| Parameter | Type | Description |
|---|:---:|---|
| loanAmount | Float | *The principal amount. |
| nominalRate | Float | *Annual interest rate |
| duration | Integer | *Number of installments in months |
| startDate | Date | Date of the first Disbursement/Payout |

All values should be assigned, and cannot be null.

_\* The numeric values should be greater than zero._

#### Output 

If the loan parameters are consistent, this operation will return a list with
the repayment plan as below:

```
{
    [
        {
            "borrowerPaymentAmount": "219.36",
            "date": "2018-01-01T00:00:00Z",
            "initialOutstandingPrincipal": "5000.00",
            "interest": "20.83",
            "principal": "198.53",
            "remainingOutstandingPrincipal": "4801.47",
        },
        {
            "borrowerPaymentAmount": "219.36",
            "date": "2018-02-01T00:00:00Z",
            "initialOutstandingPrincipal": "4801.47",
            "interest": "20.01",
            "principal": "199.35",
            "remainingOutstandingPrincipal": "4602.12",
        },
    ...
        {
            "borrowerPaymentAmount": "219.28",
            "date": "2019-12-01T00:00:00Z",
            "initialOutstandingPrincipal": "218.37",
            "interest": "0.91",
            "principal": "218.37",
            "remainingOutstandingPrincipal": "0",
        }
    ]
}
```

#### Parameters description

| Parameter | Type | Description |
|---|:---:|---|
| borrowerPaymentAmount | float | Borrower Payment Amount (principal + interest) |
| date | Date | Payment date |
| initialOutstandingPrincipal | Integer | Remaining value to be paid from the loan amount without interest |
| interest | float | Interest related on this payment |
| principal | float | Value without interest that will deduct the remaining loan amount |
| remainingOutstandingPrincipal | float | Remaining value to be paid after this payment (initialOutstandingPrincipal - principal) |

