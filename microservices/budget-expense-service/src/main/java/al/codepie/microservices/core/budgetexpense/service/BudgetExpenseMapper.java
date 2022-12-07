package al.codepie.microservices.core.budgetexpense.service;

import al.codepie.microservices.api.core.budgetexpense.BudgetExpense;
import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BudgetExpenseMapper {
  @Mappings({
      @Mapping(target = "serviceAddress", ignore = true),
  })
  BudgetExpense entityToApi(BudgetExpenseEntity entity);

  @Mappings({
      @Mapping(target = "version", ignore = true),
  })
  BudgetExpenseEntity apiToEntity(BudgetExpense api);

  List<BudgetExpense> entityListToApiList(List<BudgetExpenseEntity> entity);

  List<BudgetExpenseEntity> apiListToEntityList(List<BudgetExpense> api);
}
