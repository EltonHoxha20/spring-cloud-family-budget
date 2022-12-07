package al.codepie.microservices.core.budgetexpense;

import al.codepie.microservices.api.core.budgetexpense.BudgetExpense;
import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseEntity;
import al.codepie.microservices.core.budgetexpense.service.BudgetExpenseMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTests {
  private BudgetExpenseMapper mapper = Mappers.getMapper(BudgetExpenseMapper.class);

  @Test
  void mapperTests() {

    assertNotNull(mapper);

    BudgetExpense api = BudgetExpense.builder().id("1A").budgetSubItemId(1L).expenseAmount(123.45).registrationDate(LocalDateTime.now()).description("Desc").serviceAddress("SA").build();
    BudgetExpenseEntity entity = mapper.apiToEntity(api);

    assertEquals(api.getId(), entity.getId());
    assertEquals(api.getBudgetSubItemId(), entity.getBudgetSubItemId());
    assertEquals(api.getExpenseAmount(), entity.getExpenseAmount());
    assertEquals(api.getDescription(), entity.getDescription());
    assertEquals(api.getRegistrationDate(), entity.getRegistrationDate());

    BudgetExpense api2 = mapper.entityToApi(entity);

    assertEquals(api.getId(), api2.getId());
    assertEquals(api.getBudgetSubItemId(), api2.getBudgetSubItemId());
    assertEquals(api.getExpenseAmount(), api2.getExpenseAmount());
    assertEquals(api.getDescription(), api2.getDescription());
    assertEquals(api.getRegistrationDate(), api2.getRegistrationDate());
    assertNull(api2.getServiceAddress());
  }

  @Test
  void mapperListTests() {

    assertNotNull(mapper);

    BudgetExpense api = BudgetExpense.builder().id("1A").budgetSubItemId(1L).expenseAmount(123.45).registrationDate(LocalDateTime.now()).description("Desc").serviceAddress("SA").build();
    List<BudgetExpense> apiList = Collections.singletonList(api);

    List<BudgetExpenseEntity> entityList = mapper.apiListToEntityList(apiList);
    assertEquals(apiList.size(), entityList.size());

    BudgetExpenseEntity entity = entityList.get(0);

    assertEquals(api.getId(), entity.getId());
    assertEquals(api.getBudgetSubItemId(), entity.getBudgetSubItemId());
    assertEquals(api.getExpenseAmount(), entity.getExpenseAmount());
    assertEquals(api.getDescription(), entity.getDescription());
    assertEquals(api.getRegistrationDate(), entity.getRegistrationDate());

    List<BudgetExpense> api2List = mapper.entityListToApiList(entityList);
    assertEquals(apiList.size(), api2List.size());

    BudgetExpense api2 = api2List.get(0);

    assertEquals(api.getId(), api2.getId());
    assertEquals(api.getBudgetSubItemId(), api2.getBudgetSubItemId());
    assertEquals(api.getExpenseAmount(), api2.getExpenseAmount());
    assertEquals(api.getDescription(), api2.getDescription());
    assertEquals(api.getRegistrationDate(), api2.getRegistrationDate());
    assertNull(api2.getServiceAddress());
  }

}
