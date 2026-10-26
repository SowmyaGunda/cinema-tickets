package uk.gov.dwp.uc.pairtest.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.ruleengine.RuleViolation;
import uk.gov.dwp.uc.pairtest.ruleengine.TicketBookingRuleEngine;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketPaymentService paymentService;

    @Mock
    private SeatReservationService reservationService;

    @Mock
    private TicketBookingRuleEngine ticketBookingRuleEngine;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private Long accountId;

    @BeforeEach
    void setUp() {
        accountId = 123L;
    }

    @Test
    void purchaseTickets_shouldMakePaymentAndReserveSeats_whenPurchaseIsValid() {

        when(ticketBookingRuleEngine.evaluate(any())).thenReturn(List.of());
        // Act
        ticketService.purchaseTickets(accountId, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2),
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD,1),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 1));

        // Assert
        verify(paymentService).makePayment(eq(accountId), eq(65));
        verify(reservationService).reserveSeat(eq(accountId), eq(3));
    }

    @Test
    void purchaseTickets_shouldThrowException_whenValidationFailsWithAdultRequiredRule() {
        // Arrange
        TicketTypeRequest request =
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        when(ticketBookingRuleEngine.evaluate(any())).thenReturn(List.of(new RuleViolation("ADULT_REQUIRED",
                "At least one adult ticket is required when purchasing tickets.")));

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(accountId, request)
        );

        assertEquals(1, exception.getViolations().size());

        assertEquals(
                "ADULT_REQUIRED",
                exception.getViolations().get(0).ruleCode()
        );

        assertEquals(
                "At least one adult ticket is required when purchasing tickets.",
                exception.getViolations().get(0).message()
        );

        verifyNoInteractions(paymentService, reservationService);
    }

    @Test
    void purchaseTickets_shouldThrowException_whenValidationFailsWithValidAccountIdRule() {
        // Arrange
        TicketTypeRequest request =
                new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        when(ticketBookingRuleEngine.evaluate(any())).thenReturn(List.of(new RuleViolation("VALID_ACCOUNT",
                "Account ID must be greater than zero.")));

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(0L, request)
        );

        assertEquals(1, exception.getViolations().size());

        assertEquals(
                "VALID_ACCOUNT",
                exception.getViolations().get(0).ruleCode()
        );

        assertEquals(
                "Account ID must be greater than zero.",
                exception.getViolations().get(0).message()
        );

        verifyNoInteractions(paymentService, reservationService);
    }


    @Test
    void purchaseTickets_shouldNotMakePayment_whenTicketIsInValid() {
        // Arrange
        TicketTypeRequest request =
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -1);
        when(ticketBookingRuleEngine.evaluate(any())).thenReturn(List.of(new RuleViolation("VALID_TICKET_LINES",
                "Quantity at index 0 must be greater than zero")));

        // Act & Assert
        InvalidPurchaseException exception = assertThrows(
                InvalidPurchaseException.class,
                () -> ticketService.purchaseTickets(accountId, request)
        );

        assertEquals(1, exception.getViolations().size());

        assertEquals(
                "VALID_TICKET_LINES",
                exception.getViolations().get(0).ruleCode()
        );

        assertEquals(
                "Quantity at index 0 must be greater than zero",
                exception.getViolations().get(0).message()
        );

        verify(paymentService, never()).makePayment(anyLong(), anyInt());
        verify(reservationService, never()).reserveSeat(anyLong(), anyInt());
    }

    @Test
    void purchaseTickets_shouldPassCorrectAccountIdToDependencies() {
        // Arrange
        Long expectedAccountId = 456L;
        TicketTypeRequest request =
                new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);

        // Act
        ticketService.purchaseTickets(expectedAccountId, request);

        // Assert
        verify(paymentService).makePayment(eq(expectedAccountId), anyInt());
        verify(reservationService).reserveSeat(eq(expectedAccountId), anyInt());
    }
}