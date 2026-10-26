package uk.gov.dwp.uc.pairtest.ruleengine.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolationType;

import java.util.List;

public final class NonEmptyRequestRule implements TicketRule {

    @Override
    public List<RuleViolation> evaluate(TicketPurchaseContext context) {
        if (context == null || context.tickets() == null || context.tickets().isEmpty()) {
            return List.of(new RuleViolation(RuleViolationType.NON_EMPTY_REQUEST.getCode(), RuleViolationType.NON_EMPTY_REQUEST.getMessage()));
        }
        return List.of();
    }
}
