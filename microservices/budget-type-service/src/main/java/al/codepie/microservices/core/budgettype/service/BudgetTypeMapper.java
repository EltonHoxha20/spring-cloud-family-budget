package al.codepie.microservices.core.budgettype.service;

import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetTypeMapper {

  @Mappings({
      @Mapping(target = "serviceAddress", ignore = true)
  })
  BudgetType entityToApi(BudgetTypeEntity entity);

  @Mappings({
      @Mapping(target = "version", ignore = true),
      @Mapping(target = "parent", ignore = true),
      @Mapping(target = "children", ignore = true),
  })
  BudgetTypeEntity apiToEntity(BudgetType api);

  List<BudgetType> entityListToApiList(List<BudgetTypeEntity> entity);

  List<BudgetTypeEntity> apiListToEntityList(List<BudgetType> api);

}
