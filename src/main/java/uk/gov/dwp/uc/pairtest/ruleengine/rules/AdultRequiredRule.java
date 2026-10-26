package uk.gov.dwp.uc.pairtest.ruleengine.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolationType;

import java.util.List;

public final class AdultRequiredRule implements TicketRule {

    @Override
    public List<RuleViolation> evaluate(TicketPurchaseContext context) {
        if (context == null || context.tickets() == null || context.tickets().isEmpty()) {
            return List.of(); // Missing requests are reported by NON_EMPTY_REQUEST.
        }

        boolean hasAdult = context.tickets().stream()
                .filter(ticket -> ticket != null && ticket.getNoOfTickets() > 0)
                .anyMatch(ticket -> ticket.getTicketType() == TicketTypeRequest.Type.ADULT);

        if (!hasAdult) {
            return List.of(new RuleViolation(
                    RuleViolationType.ADULT_REQUIRED.getCode(), RuleViolationType.ADULT_REQUIRED.getMessage()));
        }
        return List.of();
    }
}
