package uk.gov.dwp.uc.pairtest.service;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.PurchaseTotals;
import uk.gov.dwp.uc.pairtest.domain.TicketPurchaseContext;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.TicketBookingRuleEngine;
import java.util.List;
import java.util.Arrays;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketBookingRuleEngine ruleEngine;
    private final TicketPaymentService paymentService;
    private final SeatReservationService reservationService;

    public TicketServiceImpl(
            TicketBookingRuleEngine ruleEngine,
            TicketPaymentService paymentService,
            SeatReservationService reservationService) {
        this.ruleEngine = ruleEngine;
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    private List<RuleViolation> validate(Long accountId, List<TicketTypeRequest> requests) {
        return ruleEngine.evaluate(new TicketPurchaseContext(accountId, requests));
    }

    private PurchaseTotals calculateTotals(List<TicketTypeRequest> requests) {
        int tickets = 0;
        int amount = 0;
        int seats = 0;

        for (TicketTypeRequest request : requests) {
            int quantity = request.getNoOfTickets();
            tickets = Math.addExact(tickets, quantity);
            switch (request.getTicketType()) {
                case ADULT -> {
                    amount = (int) Math.addExact(amount, Math.multiplyExact(25L, quantity));
                    seats = Math.addExact(seats, quantity);
                }
                case CHILD -> {
                    amount = (int) Math.addExact(amount, Math.multiplyExact(15L, quantity));
                    seats = Math.addExact(seats, quantity);
                }
                case INFANT -> { /* £0 and no seat */ }
            }
        }
        return new PurchaseTotals(tickets, amount, seats);
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... requests) {
        List<TicketTypeRequest> lines = requests == null ? null : Arrays.asList(requests.clone());
        List<RuleViolation> failures = validate(accountId, lines);
        if (!failures.isEmpty()) {
            throw new InvalidPurchaseException(failures);
        }

        PurchaseTotals totals = calculateTotals(lines);

        paymentService.makePayment(accountId, totals.paymentPounds());
        reservationService.reserveSeat(accountId, totals.seats());
    }
}
