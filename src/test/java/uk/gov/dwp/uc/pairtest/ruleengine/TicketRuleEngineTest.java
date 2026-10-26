package uk.gov.dwp.uc.pairtest.ruleengine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.*;

class TicketRuleEngineTest {
    private final TicketBookingRuleEngine engine = new TicketBookingRuleEngine(List.of(
            new ValidAccountRule(),
            new NonEmptyRequestRule(),
            new ValidTicketTypeRule(),
            new MaximumTicketLimitRule(),
            new AdultRequiredRule()));

    @Test
    void returnsAllFailuresInsteadOfStoppingAtFirst() {
        TicketPurchaseContext context = new TicketPurchaseContext(
                0L,
                List.of(
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 0),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 30)));

        List<RuleViolation> failures = engine.evaluate(context);

        assertTrue(failures.stream().anyMatch(v -> v.ruleCode().equals(RuleViolationType.INVALID_ACCOUNT.getCode())));
        assertTrue(failures.stream().anyMatch(v -> v.ruleCode().equals(RuleViolationType.MAXIMUM_TICKET_LIMIT.getCode())));
        assertTrue(failures.stream().anyMatch(v -> v.ruleCode().equals(RuleViolationType.ADULT_REQUIRED.getCode())));
    }

    @Test
    void validRequestPassesAllRules() {
        TicketPurchaseContext context = new TicketPurchaseContext(
                1L,
                List.of(
                        new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                        new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 3),
                        new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1)));

        assertTrue(engine.evaluate(context).isEmpty());
    }
}
