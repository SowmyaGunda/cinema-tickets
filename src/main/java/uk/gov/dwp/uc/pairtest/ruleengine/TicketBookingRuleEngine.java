package uk.gov.dwp.uc.pairtest.ruleengine;

import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.TicketRule;

import java.util.ArrayList;
import java.util.List;

public final class TicketBookingRuleEngine {
    private final List<TicketRule> rules;

    public TicketBookingRuleEngine(List<TicketRule> rules) {
        if (rules == null) {
            throw new IllegalArgumentException("Rules must not be null");
        }
        this.rules = List.copyOf(rules);
    }

    public List<RuleViolation> evaluate(TicketPurchaseContext context) {
        List<RuleViolation> failures = new ArrayList<>();
        for (TicketRule rule : rules) {
                failures.addAll(rule.evaluate(context));
        }
        return List.copyOf(failures);
    }
}
