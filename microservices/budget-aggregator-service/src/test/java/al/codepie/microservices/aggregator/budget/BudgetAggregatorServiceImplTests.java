package al.codepie.microservices.aggregator.budget;

import al.codepie.microservices.aggregator.budget.service.BudgetAggregateIntegration;
import al.codepie.microservices.aggregator.budget.service.BudgetAggregateServiceImpl;
import al.codepie.microservices.api.aggregator.budget.BudgetAggregate;
import al.codepie.microservices.api.core.budgetexpense.BudgetExpense;
import al.codepie.microservices.api.core.budgetitem.BudgetItem;
import al.codepie.microservices.api.core.budgetitem.BudgetSubItem;
import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import al.codepie.microservices.api.exceptions.InvalidInputException;
import al.codepie.microservices.api.exceptions.NotFoundException;
import al.codepie.microservices.util.http.ServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

//@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class BudgetAggregatorServiceImplTests {

  //@Test
  void contextLoads() {
  }

  private static final long BUDGET_ITEM_ID_OK = 1L;
  private static final long BUDGET_ITEM_ID_NOT_FOUND = 2L;
  private static final long BUDGET_ITEM_ID_INVALID = 3L;

  private static final long BUDGET_TYPE_ID_OK = 4L;

  private static final long BUDGET_SUB_ITEM_ID_OK = 5L;

  private static final String BUDGET_EXPENSE_ID_OK = "OK";

  @InjectMocks
  //@Autowired
  private BudgetAggregateServiceImpl service;

  @Mock
  //@MockBean
  private BudgetAggregateIntegration integration;

  @Mock
  //@MockBean
  private ServiceUtil serviceUtil;

  @BeforeEach
  void setUp() {
//    when(integration.getBudgetItem(BUDGET_ITEM_ID_OK))
//        .thenReturn(BudgetItem.builder().id(BUDGET_ITEM_ID_OK).totalAmount(200.1).name("Name Item")
//            .projectedExpenseAmount(100.1).serviceAddress("mock-sa").budgetTypeId(BUDGET_TYPE_ID_OK).build());
//
//    when(integration.getBudgetType(BUDGET_TYPE_ID_OK))
//        .thenReturn(BudgetType.builder().id(BUDGET_TYPE_ID_OK).totalIncome(200.1)
//            .type(BudgetTypeEnum.MONTHLY).serviceAddress("mock-sa").build());
//
//    when(integration.getBudgetSubItems(BUDGET_ITEM_ID_OK))
//        .thenReturn(Collections.singletonList(BudgetSubItem.builder()
//            .id(BUDGET_SUB_ITEM_ID_OK).budgetItemId(BUDGET_ITEM_ID_OK).name("Name Sub Item").serviceAddress("mock-sa").build()));
//
//    when(integration.getBudgetExpenses(ZonedDateTime.now().minusDays(2),
//        ZonedDateTime.now().plusDays(2), List.of(BudgetSubItem.builder().id(BUDGET_SUB_ITEM_ID_OK).build())))
//        .thenReturn(Collections.singletonList(BudgetExpense.builder()
//            .id(BUDGET_EXPENSE_ID_OK).budgetSubItemId(BUDGET_SUB_ITEM_ID_OK).description("Desc")
//            .registrationDate(ZonedDateTime.now()).serviceAddress("mock-sa").expenseAmount(10.00).build()));
//
//    when(integration.getBudgetItem(BUDGET_ITEM_ID_NOT_FOUND))
//        .thenThrow(new NotFoundException("NOT FOUND: " + BUDGET_ITEM_ID_NOT_FOUND));
//
//    when(integration.getBudgetItem(BUDGET_ITEM_ID_INVALID))
//        .thenThrow(new InvalidInputException("INVALID: " + BUDGET_ITEM_ID_INVALID));

//    when(serviceUtil.getServiceAddress()).thenReturn("mock-sa");


  }

  @Test
  void testBudgetAggregate() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime plusTwo = now.plusDays(2);
    LocalDateTime minusTwo = now.minusDays(2);
    BudgetAggregate aggregate = BudgetAggregate.builder().itemId(BUDGET_ITEM_ID_OK)
        .itemName("Name Item")
        .projectedExpense(111.11).actualExpense(10.00)
        .difference(190.1)
        .fromDate(minusTwo)
        .toDate(plusTwo)
        .totalIncome(200.1)
        .serviceAddress("mock-sa")
        .build();

    doReturn(aggregate).when(integration).getBudgetAggregateExpense(minusTwo,
        plusTwo, BUDGET_ITEM_ID_OK);

    assertEquals(aggregate,
        service.getBudgetAggregateExpense(minusTwo, plusTwo, BUDGET_ITEM_ID_OK));
  }


}
