package uk.gov.dwp.uc.pairtest.configuration;

import uk.gov.dwp.uc.pairtest.ruleengine.rules.AdultRequiredRule;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.MaximumTicketLimitRule;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.NonEmptyRequestRule;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.TicketRule;
import uk.gov.dwp.uc.pairtest.ruleengine.TicketBookingRuleEngine;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.ValidAccountRule;
import uk.gov.dwp.uc.pairtest.ruleengine.rules.ValidTicketTypeRule;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketRuleEngineConfiguration {
    @Bean
    TicketBookingRuleEngine ticketRuleEngine() {
        List<TicketRule> rules = List.of(
                new ValidAccountRule(),
                new NonEmptyRequestRule(),
                new ValidTicketTypeRule(),
                new MaximumTicketLimitRule(),
                new AdultRequiredRule()
        );
        return new TicketBookingRuleEngine(rules);
    }
}
