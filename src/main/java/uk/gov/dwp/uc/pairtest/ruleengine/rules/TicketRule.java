package uk.gov.dwp.uc.pairtest.ruleengine.rules;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;

import java.util.List;

public interface TicketRule {
    /**
     * Return an empty list when the rule passes; otherwise return all violations
     * this rule can identify for the supplied context.
     */
    List<RuleViolation> evaluate(TicketPurchaseContext context);
}
