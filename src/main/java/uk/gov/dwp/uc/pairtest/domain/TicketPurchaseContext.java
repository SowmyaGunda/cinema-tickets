package uk.gov.dwp.uc.pairtest.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record TicketPurchaseContext(Long accountId, List<TicketTypeRequest> tickets) {
    public TicketPurchaseContext {
        // Preserve null entries so validation rules can report malformed lines
        // instead of List.copyOf throwing before the engine runs.
        tickets = tickets == null ? null
                : Collections.unmodifiableList(new ArrayList<>(tickets));
    }
}
