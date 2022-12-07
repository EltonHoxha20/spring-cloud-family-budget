package al.codepie.microservices.aggregator.budget.service;

import al.codepie.microservices.api.aggregator.budget.BudgetAggregate;
import al.codepie.microservices.api.aggregator.budget.BudgetAggregateService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Log4j2
public class BudgetAggregateServiceImpl implements BudgetAggregateService {

  private final BudgetAggregateIntegration integration;

  @Autowired
  public BudgetAggregateServiceImpl(BudgetAggregateIntegration integration) {
    this.integration = integration;
  }

  @Override
  public BudgetAggregate getBudgetAggregateExpense(LocalDateTime from, LocalDateTime to, Long itemId) {
    try {

      log.debug("getBudgetAggregateExpense: from: {}, to: {}, itemId: {}", from, to, itemId);
      return integration.getBudgetAggregateExpense(from, to, itemId);
    } catch (RuntimeException re) {
      log.warn("getBudgetAggregateExpense failed", re);
      throw re;
    }
  }

}
