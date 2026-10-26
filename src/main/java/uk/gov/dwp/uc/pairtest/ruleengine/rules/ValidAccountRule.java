package uk.gov.dwp.uc.pairtest.ruleengine.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolationType;

import java.util.List;

public final class ValidAccountRule implements TicketRule {

    @Override
    public List<RuleViolation> evaluate(TicketPurchaseContext context) {
        if (context == null || context.accountId() == null || context.accountId() <= 0) {
            return List.of(new RuleViolation( RuleViolationType.INVALID_ACCOUNT.getCode(), RuleViolationType.INVALID_ACCOUNT.getMessage()));
        }
        return List.of();
    }
}
