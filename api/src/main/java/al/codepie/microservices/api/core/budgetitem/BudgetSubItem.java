package al.codepie.microservices.api.core.budgetitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSubItem {
  private Long id;
  private String name;
  private Long budgetItemId;
  //TODO: encapsulate BudgetItem in a new object
  private String budgetItemName;
  private Long budgetTypeId;
  private Double projectedExpenseAmount;
  private String serviceAddress;
}
