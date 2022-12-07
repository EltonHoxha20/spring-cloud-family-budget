package al.codepie.microservices.core.budgettype;

import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeEntity;
import al.codepie.microservices.core.budgettype.service.BudgetTypeMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapperTests {

  private BudgetTypeMapper mapper = Mappers.getMapper(BudgetTypeMapper.class);

  @Test
  void mapperTests() {

    assertNotNull(mapper);

    BudgetType api = BudgetType.builder().id(1L).type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).serviceAddress("SA").build();
    BudgetTypeEntity entity = mapper.apiToEntity(api);

    assertEquals(api.getId(), entity.getId());
    assertEquals(api.getTotalIncome(), entity.getTotalIncome());
    assertEquals(api.getType(), entity.getType());

    BudgetType api2 = mapper.entityToApi(entity);

    assertEquals(api.getId(), api2.getId());
    assertEquals(api.getType(), api2.getType());
    assertEquals(api.getTotalIncome(), api2.getTotalIncome());
    assertNull(api2.getServiceAddress());
  }

  @Test
  void mapperListTests() {

    assertNotNull(mapper);

    BudgetType api = BudgetType.builder().id(1L).type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).serviceAddress("SA").build();
    List<BudgetType> apiList = Collections.singletonList(api);

    List<BudgetTypeEntity> entityList = mapper.apiListToEntityList(apiList);
    assertEquals(apiList.size(), entityList.size());

    BudgetTypeEntity entity = entityList.get(0);

    assertEquals(api.getId(), entity.getId());
    assertEquals(api.getType(), entity.getType());
    assertEquals(api.getTotalIncome(), entity.getTotalIncome());

    List<BudgetType> api2List = mapper.entityListToApiList(entityList);
    assertEquals(apiList.size(), api2List.size());

    BudgetType api2 = api2List.get(0);

    assertEquals(api.getId(), api2.getId());
    assertEquals(api.getType(), api2.getType());
    assertEquals(api.getTotalIncome(), api2.getTotalIncome());
    assertNull(api2.getServiceAddress());
  }

}
