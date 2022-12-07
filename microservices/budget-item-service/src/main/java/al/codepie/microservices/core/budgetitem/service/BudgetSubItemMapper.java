package al.codepie.microservices.core.budgetitem.service;

import al.codepie.microservices.api.core.budgetitem.BudgetSubItem;
import al.codepie.microservices.core.budgetitem.persistence.BudgetSubItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetSubItemMapper {

  @Mappings({
      @Mapping(target = "serviceAddress", ignore = true),
      @Mapping(target = "budgetItemName", ignore = true),
      @Mapping(target = "budgetTypeId", ignore = true),
      @Mapping(target = "budgetItemId", source = "budgetItem.id"),
      @Mapping(target = "projectedExpenseAmount", source = "projExpAmount")

  })
  BudgetSubItem entityToApi(BudgetSubItemEntity entity);

  @Mappings({
      @Mapping(target = "version", ignore = true),
      @Mapping(target = "budgetItem.id", source = "budgetItemId"),
      @Mapping(target = "projExpAmount", source = "projectedExpenseAmount")
  })
  BudgetSubItemEntity apiToEntity(BudgetSubItem api);

  List<BudgetSubItem> entityListToApiList(List<BudgetSubItemEntity> entity);

  List<BudgetSubItemEntity> apiListToEntityList(List<BudgetSubItem> api);
}
