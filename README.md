# Cinema Tickets

## 1. Overview

The Cinema Tickets is a Java 21 application that validates ticket purchase requests against a set of configurable business rules before allowing a purchase to proceed.

The application separates business validation from service orchestration. This makes the rules easier to maintain, test, and extend without coupling them to an API endpoint or infrastructure concerns.

### Key features

- Centralized ticket purchase validation.
- Independent, reusable business rules.
- Collection of multiple rule violations in a single validation pass.
- Service-layer orchestration of purchase processing.
- Prevention of payment and reservation processing for invalid purchases.
- Unit tests and Cucumber feature tests.
- Maven-based build and test execution.

## 2. Technology stack

| Technology | Purpose |
|---|---|
| Java 21 | Application language |
| Maven | Dependency management and build |
| Spring Boot | Dependency injection and application configuration |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies |
| AssertJ | Readable test assertions |
| Cucumber | Business-readable feature tests |

## 3. Architecture

The main processing flow is:

```text
Ticket Purchase Request
          |
          v
    TicketService
          |
          v
    TicketRuleEngine
          |
          v
     Business Rules
          |
          v
   Rule Violations
          |
          v
  Validation Decision
       /       \
   Invalid     Valid
      |          |
 Return errors   Process purchase
                 |
           Payment / Reservation
```

The service invokes the rule engine before performing purchase-related operations. If validation fails, the service returns the applicable violations and does not proceed with payment or reservation.

## 4. Design principles Implemented

- **Single responsibility:** Each business rule validates one specific condition.
- **Separation of concerns:** The rule engine validates; the service orchestrates.
- **Fail safely:** Invalid requests must not trigger purchase operations.
- **Comprehensive feedback:** Report all applicable business-rule violations.
- **Testability:** Business logic can be tested without an API or external systems.
- **Extensibility:** New rules can be added with limited impact on existing rules.

## 5. TicketRuleEngine

### Responsibility

`TicketRuleEngine` evaluates a ticket purchase request against the configured business rules.

It coordinates the individual rules without owning the business logic of every rule. Each rule implements one specific validation responsibility.

### Key responsibilities

- Accept a ticket purchase request.
- Execute the configured business rules.
- Collect violations returned by the rules.
- Return the complete collection of violations.
- Continue evaluating applicable rules instead of stopping at the first failure.

### Example usage

```java
List<RuleViolation> violations =
        ticketRuleEngine.validate(purchaseRequest);

if (!violations.isEmpty()) {
    // Reject the purchase and return the violations.
}
```

The method name should match the actual project implementation.

### Business rules

| Rule                 | Description |
|----------------------|---|
| Valid account        | An account identifier must be present and valid. |
| Non-empty request    | A purchase must contain ticket lines. |
| Valid ticket type    | Ticket types and quantities must be valid. |
| Maximum ticket limit | The purchase must not exceed 25 tickets. |
| Adult required       | A purchase containing child tickets must satisfy the adult-ticket requirement. |

Each rule should be independently testable and return a structured `RuleViolation` when its condition is not satisfied.

### Extending the engine

To add a new business rule:

1. Create a class implementing the project's rule interface.
2. Implement the rule's validation logic.
3. Return a `RuleViolation` when validation fails.
4. Register the new rule with `TicketRuleEngine`.
5. Add unit tests and Cucumber feature scenarios.

This design keeps individual rules focused and reduces changes to existing validation logic.

## 6. TicketService and TicketServiceImpl

### Responsibility

The service layer coordinates the ticket purchase workflow. It connects business validation with downstream purchase operations.

The service is responsible for:

1. Receiving a purchase request.
2. Calling `TicketRuleEngine`.
3. Checking the returned violations.
4. Rejecting invalid requests and returning their violations.
5. Processing valid requests using the required payment and reservation services.
6. Returning the appropriate purchase result.

### Validation-first processing

The service follows this conceptual pattern:

```java
public PurchaseResult purchase(TicketPurchaseRequest request) {
    List<RuleViolation> violations =
            ticketRuleEngine.validate(request);

    if (!violations.isEmpty()) {
        return PurchaseResult.rejected(violations);
    }

    // Continue with payment and reservation.
    // Return the successful purchase result.
}
```

This is illustrative pseudocode. Adapt it to the actual result type and service method signatures.

### Important service guarantees

- **Invalid purchase:** Return validation failures without initiating payment or reservation.
- **Multiple failures:** Preserve all applicable rule violations.
- **Valid purchase:** Proceed only after validation succeeds.
- **Separation of concerns:** Keep validation logic in the rule engine and workflow coordination in the service.

### Error handling

Business-rule failures should be represented as structured violations rather than generic exceptions. Unexpected infrastructure failures, such as payment-provider errors, should be handled according to the application's error-handling strategy and should not be confused with validation failures.

Payment and reservation operations may require transactional or compensating behavior if one succeeds and the other fails. Validation alone does not guarantee transactional consistency.

## 7. RuleViolation

`RuleViolation` represents a business rule that a purchase request has failed.

Depending on the implementation, it may contain:

- A rule identifier or name.
- A human-readable error message.
- Optional details about the invalid field or value.

Structured violations allow callers and tests to identify failures without relying exclusively on error-message text.

## 8. Testing

### Unit tests

Unit tests are added to verify individual business rules independently, covering both valid and invalid inputs.

Some of the scenarios covered:

- Empty account identifier.
- Empty ticket request.
- Invalid ticket quantity.
- More than 25 tickets.
- Child tickets without a qualifying adult ticket.



## 8. Running the tests

Run all tests:

```bash
mvn test
```

Run the unit tests for a specific class:

```bash
mvn -Dtest=TicketRuleEngineTest test
```