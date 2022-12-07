package al.codepie.microservices.api.core.budgetitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItem {

  private Long id;
  private String name;
  private Long budgetTypeId;
  private Double projectedExpenseAmount;
  private Double totalAmount;
  private String serviceAddress;
  private List<Long> budgetSubItemIds;


}
