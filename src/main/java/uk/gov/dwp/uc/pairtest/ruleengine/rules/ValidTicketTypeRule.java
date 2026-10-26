package uk.gov.dwp.uc.pairtest.ruleengine.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolationType;

import java.util.ArrayList;
import java.util.List;

public final class ValidTicketTypeRule implements TicketRule {

    @Override
    public List<RuleViolation> evaluate(TicketPurchaseContext context) {
        List<RuleViolation> failures = new ArrayList<>();
        if (context == null || context.tickets() == null) {
            return failures; // Missing collection is reported by NON_EMPTY_REQUEST.
        }

        for (int i = 0; i < context.tickets().size(); i++) {
            TicketTypeRequest ticket = context.tickets().get(i);
            if (ticket.getTicketType() == null) {
                failures.add(new RuleViolation(RuleViolationType.NULL_TICKET_TYPE.getCode(), RuleViolationType.NULL_TICKET_TYPE.getMessage()));
            }
            if (ticket.getNoOfTickets() < 0) {
                failures.add(new RuleViolation(RuleViolationType.INVALID_TICKET_QUANTITY.getCode(), RuleViolationType.INVALID_TICKET_QUANTITY.getMessage()));
            }
        }
        return List.copyOf(failures);
    }
}
