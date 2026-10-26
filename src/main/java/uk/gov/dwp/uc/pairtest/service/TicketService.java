package uk.gov.dwp.uc.pairtest.service;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

public interface TicketService {
    void purchaseTickets(Long accountId, TicketTypeRequest... requests);
}
