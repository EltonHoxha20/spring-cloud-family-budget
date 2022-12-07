package al.codepie.microservices.core.budgetitem;

import al.codepie.microservices.api.core.budgetitem.BudgetItem;
import al.codepie.microservices.api.core.budgetitem.BudgetSubItem;
import al.codepie.microservices.core.budgetitem.persistence.BudgetItemEntity;
import al.codepie.microservices.core.budgetitem.persistence.BudgetSubItemEntity;
import al.codepie.microservices.core.budgetitem.service.BudgetItemMapper;
import al.codepie.microservices.core.budgetitem.service.BudgetSubItemMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTests {

  private BudgetItemMapper budgetItemMapper = Mappers.getMapper(BudgetItemMapper.class);
  private BudgetSubItemMapper budgetSubItemMapper = Mappers.getMapper(BudgetSubItemMapper.class);

  @Test
  void mapperTests() {

    assertNotNull(budgetItemMapper);
    assertNotNull(budgetSubItemMapper);

    //BudgetItem
    BudgetItem budgetItemApi = BudgetItem.builder().id(1L).name("name").budgetTypeId(2L).projectedExpenseAmount(123.45).totalAmount(12.34).serviceAddress("SA").build();
    BudgetItemEntity entity = budgetItemMapper.apiToEntity(budgetItemApi);

    assertEquals(budgetItemApi.getId(), entity.getId());
    assertEquals(budgetItemApi.getBudgetTypeId(), entity.getBudgetTypeId());
    assertEquals(budgetItemApi.getName(), entity.getName());
    assertEquals(budgetItemApi.getProjectedExpenseAmount(), entity.getProjExpAmount());

    BudgetItem budgetItemApi2 = budgetItemMapper.entityToApi(entity);

    assertEquals(budgetItemApi.getId(), budgetItemApi2.getId());
    assertEquals(budgetItemApi.getBudgetTypeId(), budgetItemApi2.getBudgetTypeId());
    assertEquals(budgetItemApi.getName(), budgetItemApi2.getName());
    assertEquals(budgetItemApi.getProjectedExpenseAmount(), budgetItemApi2.getProjectedExpenseAmount());

    assertNull(budgetItemApi2.getServiceAddress());

    //BudgetSubItem
    BudgetSubItem budgetSubItemApi = BudgetSubItem.builder().id(1L).name("name").budgetItemId(2L).projectedExpenseAmount(123.45).budgetItemName("name").serviceAddress("SA").build();
    BudgetSubItemEntity budgetSubItemEntity = budgetSubItemMapper.apiToEntity(budgetSubItemApi);

    assertEquals(budgetSubItemApi.getId(), budgetSubItemEntity.getId());
    assertEquals(budgetSubItemApi.getBudgetItemId(), budgetSubItemEntity.getBudgetItem().getId());
    assertEquals(budgetSubItemApi.getName(), budgetSubItemEntity.getName());
    assertEquals(budgetSubItemApi.getProjectedExpenseAmount(), budgetSubItemEntity.getProjExpAmount());

    BudgetSubItem budgetSubItemApi2 = budgetSubItemMapper.entityToApi(budgetSubItemEntity);

    assertEquals(budgetSubItemApi.getId(), budgetSubItemApi2.getId());
    assertEquals(budgetSubItemApi.getBudgetItemId(), budgetSubItemApi2.getBudgetItemId());
    assertEquals(budgetSubItemApi.getName(), budgetSubItemApi2.getName());
    assertEquals(budgetSubItemApi.getProjectedExpenseAmount(), budgetSubItemApi2.getProjectedExpenseAmount());

    assertNull(budgetSubItemApi2.getServiceAddress());
  }

  @Test
  void mapperListTests() {

    assertNotNull(budgetItemMapper);
    assertNotNull(budgetSubItemMapper);

    //BudgetItem
    BudgetItem budgetItemApi = BudgetItem.builder().id(1L).name("name").budgetTypeId(2L).projectedExpenseAmount(123.45).totalAmount(12.34).serviceAddress("SA").build();
    List<BudgetItem> budgetItemApiList = Collections.singletonList(budgetItemApi);

    List<BudgetItemEntity> entityList = budgetItemMapper.apiListToEntityList(budgetItemApiList);
    assertEquals(budgetItemApiList.size(), entityList.size());

    BudgetItemEntity entity = entityList.get(0);

    assertEquals(budgetItemApi.getId(), entity.getId());
    assertEquals(budgetItemApi.getBudgetTypeId(), entity.getBudgetTypeId());
    assertEquals(budgetItemApi.getName(), entity.getName());
    assertEquals(budgetItemApi.getProjectedExpenseAmount(), entity.getProjExpAmount());

    List<BudgetItem> budgetItemApi2List = budgetItemMapper.entityListToApiList(entityList);
    assertEquals(budgetItemApiList.size(), budgetItemApi2List.size());

    BudgetItem budgetItemApi2 = budgetItemApi2List.get(0);

    assertEquals(budgetItemApi.getId(), budgetItemApi2.getId());
    assertEquals(budgetItemApi.getBudgetTypeId(), budgetItemApi2.getBudgetTypeId());
    assertEquals(budgetItemApi.getName(), budgetItemApi2.getName());
    assertEquals(budgetItemApi.getProjectedExpenseAmount(), budgetItemApi2.getProjectedExpenseAmount());
    assertNull(budgetItemApi2.getServiceAddress());

    //BudgetSubItem
    BudgetSubItem budgetSubItemApi = BudgetSubItem.builder().id(1L).name("name").budgetItemId(2L).projectedExpenseAmount(123.45).budgetItemName("name").serviceAddress("SA").build();
    List<BudgetSubItem> budgetSubItemApiList = Collections.singletonList(budgetSubItemApi);

    List<BudgetSubItemEntity> budgetSubItemEntityList = budgetSubItemMapper.apiListToEntityList(budgetSubItemApiList);
    assertEquals(budgetSubItemApiList.size(), budgetSubItemEntityList.size());

    BudgetSubItemEntity budgetSubItemEntity = budgetSubItemEntityList.get(0);

    assertEquals(budgetSubItemApi.getId(), budgetSubItemEntity.getId());
    assertEquals(budgetSubItemApi.getBudgetItemId(), budgetSubItemEntity.getBudgetItem().getId());
    assertEquals(budgetSubItemApi.getName(), budgetSubItemEntity.getName());
    assertEquals(budgetSubItemApi.getProjectedExpenseAmount(), budgetSubItemEntity.getProjExpAmount());

    List<BudgetSubItem> budgetSubItemApi2List = budgetSubItemMapper.entityListToApiList(budgetSubItemEntityList);
    assertEquals(budgetSubItemApiList.size(), budgetSubItemApi2List.size());

    BudgetSubItem budgetSubItemApi2 = budgetSubItemApi2List.get(0);

    assertEquals(budgetSubItemApi.getId(), budgetSubItemApi2.getId());
    assertEquals(budgetSubItemApi.getBudgetItemId(), budgetSubItemApi2.getBudgetItemId());
    assertEquals(budgetSubItemApi.getName(), budgetSubItemApi2.getName());
    assertEquals(budgetSubItemApi.getProjectedExpenseAmount(), budgetSubItemApi2.getProjectedExpenseAmount());
    assertNull(budgetSubItemApi2.getServiceAddress());
  }

}
