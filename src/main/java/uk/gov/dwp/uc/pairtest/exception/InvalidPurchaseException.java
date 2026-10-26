package uk.gov.dwp.uc.pairtest.exception;

import java.util.List;

public class InvalidPurchaseException extends RuntimeException {
    private final List<uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation> violations;

    public InvalidPurchaseException(List<uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation> violations) {
        super("Ticket purchase failed validation.");
        this.violations = List.copyOf(violations);
    }

    public List<uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation> getViolations() {
        return violations;
    }

}
