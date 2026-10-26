package uk.gov.dwp.uc.pairtest.ruleengine.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolationType;

import java.util.List;

public final class MaximumTicketLimitRule implements TicketRule {
    private static final int MAX_TICKETS = 25;

    @Override
    public List<RuleViolation> evaluate(TicketPurchaseContext context) {
        if (context == null || context.tickets() == null) {
            return List.of();
        }

        long total = 0;
        for (TicketTypeRequest ticket : context.tickets()) {
            if (ticket != null && ticket.getNoOfTickets() > 0) {
                total += ticket.getNoOfTickets();
            }
        }

        if (total > MAX_TICKETS) {
            return List.of(new RuleViolation(
                    RuleViolationType.MAXIMUM_TICKET_LIMIT.getCode(), RuleViolationType.MAXIMUM_TICKET_LIMIT.getMessage()));
        }
        return List.of();
    }
}
