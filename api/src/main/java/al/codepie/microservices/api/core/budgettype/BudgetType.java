package al.codepie.microservices.api.core.budgettype;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetType {

  private Long id;
  private BudgetTypeEnum type;
  private Double totalIncome;
  private String serviceAddress;

}
