package al.codepie.microservices.aggregator.budget.service;

import al.codepie.microservices.api.aggregator.budget.BudgetAggregate;
import al.codepie.microservices.api.core.budgetexpense.BudgetExpense;
import al.codepie.microservices.api.core.budgetexpense.BudgetExpenseRequest;
import al.codepie.microservices.api.core.budgetitem.BudgetItem;
import al.codepie.microservices.api.core.budgetitem.BudgetSubItem;
import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.util.http.ErrorUtil;
import al.codepie.microservices.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Component
@Log4j2
public class BudgetAggregateIntegration {

  private final RestTemplate restTemplate;
  private final ErrorUtil errorUtil;

  private final ServiceUtil serviceUtil;

  private final String budgetTypeServiceUrl;
  private final String budgetItemServiceUrl;
  private final String budgetExpenseServiceUrl;

  @Autowired
  public BudgetAggregateIntegration(RestTemplate restTemplate,
                                    ErrorUtil errorUtil,
                                    ServiceUtil serviceUtil,
                                    @Value("${app.budget-type-service.host}") String budgetTypeServiceUrl,
                                    @Value("${app.budget-type-service.port}") int budgetTypeServicePort,
                                    @Value("${app.budget-item-service.host}") String budgetItemServiceUrl,
                                    @Value("${app.budget-item-service.port}") int budgetItemServicePort,
                                    @Value("${app.budget-expense-service.host}") String budgetExpenseServiceUrl,
                                    @Value("${app.budget-expense-service.port}") int budgetExpenseServicePort) {
    this.restTemplate = restTemplate;
    this.errorUtil = errorUtil;
    this.serviceUtil = serviceUtil;
    this.budgetTypeServiceUrl = "http://" + budgetTypeServiceUrl + ":" + budgetTypeServicePort + "/budget-type";
    this.budgetItemServiceUrl = "http://" + budgetItemServiceUrl + ":" + budgetItemServicePort + "/budget-item";
    this.budgetExpenseServiceUrl = "http://" + budgetExpenseServiceUrl + ":" + budgetExpenseServicePort + "/budget-expense";


  }


  public BudgetAggregate getBudgetAggregateExpense(LocalDateTime from, LocalDateTime to, Long itemId) {
    BudgetItem budgetItem = getBudgetItem(itemId);
    BudgetType budgetType = getBudgetType(budgetItem.getBudgetTypeId());
    List<BudgetSubItem> budgetSubItems = getBudgetSubItems(budgetItem.getId());
    List<BudgetExpense> budgetExpenseList = getBudgetExpenses(from, to, budgetSubItems);
    return aggregateExpenses(budgetItem, budgetType, budgetExpenseList, from, to);
  }

  private BudgetAggregate aggregateExpenses(BudgetItem budgetItem, BudgetType budgetType, List<BudgetExpense> budgetExpenses, LocalDateTime from, LocalDateTime to) {
    Double actualExpense = budgetExpenses.stream().mapToDouble(BudgetExpense::getExpenseAmount).sum();
    return BudgetAggregate.builder()
        .itemId(budgetItem.getId())
        .itemName(budgetItem.getName())
        .fromDate(from)
        .toDate(to)
        .totalIncome(budgetType.getTotalIncome())
        .projectedExpense(budgetItem.getProjectedExpenseAmount())
        .actualExpense(actualExpense)
        .difference(serviceUtil.formatDouble(budgetType.getTotalIncome() - actualExpense))
        .serviceAddress(serviceUtil.getServiceAddress())
        .build();
  }

  public BudgetItem getBudgetItem(Long budgetItemId) {
    try {
      String itemUrl = this.budgetItemServiceUrl + "/" + budgetItemId;
      log.debug("URL to get budgetItem {}", itemUrl);
      return this.restTemplate.getForObject(itemUrl, BudgetItem.class);
    } catch (HttpClientErrorException ex) {
      throw errorUtil.handleHttpClientException(ex);
    }
  }

  public List<BudgetSubItem> getBudgetSubItems(Long budgetItemId) {
    try {
      String subItemsUrl = this.budgetItemServiceUrl + "/sub-items-by-item/" + budgetItemId;
      log.debug("URL to get budgetSubItems {}", subItemsUrl);
      return this.restTemplate.exchange(subItemsUrl, HttpMethod.GET, null,
          new ParameterizedTypeReference<List<BudgetSubItem>>() {
          }).getBody();
    } catch (HttpClientErrorException ex) {
      throw errorUtil.handleHttpClientException(ex);
    }
  }

  public List<BudgetExpense> getBudgetExpenses(LocalDateTime from, LocalDateTime to, List<BudgetSubItem> budgetSubItems) {
    try {
      LocalDateTime fromExpense;
      LocalDateTime toExpense;
      if (from == null) {
        fromExpense = LocalDateTime.now().minusDays(30);
      } else {
        fromExpense = from.minusDays(1);
      }
      if (to == null) {
        toExpense = LocalDateTime.now().plusDays(1);
      } else {
        toExpense = to.plusDays(1);
      }
      String expenseUrl = this.budgetExpenseServiceUrl + "/aggregate";
      log.debug("URL to get budgetExpense {}", expenseUrl);
      return this.restTemplate.exchange(expenseUrl, HttpMethod.POST, new HttpEntity<>(BudgetExpenseRequest.builder()
              .from(fromExpense).to(toExpense).subItemIds(budgetSubItems.stream().map(BudgetSubItem::getId).collect(Collectors.toList())).build()),
          new ParameterizedTypeReference<List<BudgetExpense>>() {
          }).getBody();

    } catch (HttpClientErrorException ex) {
      throw errorUtil.handleHttpClientException(ex);
    }
  }

  public BudgetType getBudgetType(Long budgetTypeId) {
    try {
      String budgetTypeUrl = this.budgetTypeServiceUrl + "/" + budgetTypeId;
      log.debug("URL to get budgetType {}", budgetTypeUrl);
      return this.restTemplate.getForObject(budgetTypeUrl, BudgetType.class);
    } catch (HttpClientErrorException ex) {
      throw errorUtil.handleHttpClientException(ex);
    }
  }


}
