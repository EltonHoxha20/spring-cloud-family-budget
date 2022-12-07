package al.codepie.microservices.core.budgetitem.service;

import al.codepie.microservices.api.core.budgetitem.BudgetItem;
import al.codepie.microservices.core.budgetitem.persistence.BudgetItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetItemMapper {

  @Mappings({
      @Mapping(target = "serviceAddress", ignore = true),
      @Mapping(target = "totalAmount", ignore = true),
      @Mapping(target = "budgetSubItemIds", ignore = true),
//      @Mapping(target = "budgetTypeId", source = "budgetType.id"),
      @Mapping(target = "projectedExpenseAmount", source = "projExpAmount")

  })
  BudgetItem entityToApi(BudgetItemEntity entity);

  @Mappings({
      @Mapping(target = "version", ignore = true),
 //     @Mapping(target = "budgetType.id", source = "budgetTypeId"),
      @Mapping(target = "projExpAmount", source = "projectedExpenseAmount")
  })
  BudgetItemEntity apiToEntity(BudgetItem api);

  List<BudgetItem> entityListToApiList(List<BudgetItemEntity> entity);

  List<BudgetItemEntity> apiListToEntityList(List<BudgetItem> api);
}
