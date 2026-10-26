package uk.gov.dwp.uc.pairtest.ruleengine;

public enum RuleViolationType {
    ADULT_REQUIRED(
            "ADULT_REQUIRED",
            "At least one adult ticket is required when purchasing tickets."
    ),

    INVALID_ACCOUNT(
            "INVALID_ACCOUNT",
            "Account ID must be greater than zero."
    ),

    INVALID_TICKET_QUANTITY(
            "INVALID_TICKET_QUANTITY",
            "Ticket quantity must be greater than zero."
    ),
    MAXIMUM_TICKET_LIMIT(
            "MAXIMUM_TICKET_LIMIT",
            "Maximum 25 tickets can be purchased in single transaction"
    ),
    NON_EMPTY_REQUEST(
            "NON_EMPTY_REQUEST",
            "At least one ticket request is required."
    ),
    NULL_TICKET_TYPE(
            "NULL_TICKET_TYPE",
            "Ticket Type can not be null"
    );

    private final String code;
    private final String message;

    RuleViolationType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
