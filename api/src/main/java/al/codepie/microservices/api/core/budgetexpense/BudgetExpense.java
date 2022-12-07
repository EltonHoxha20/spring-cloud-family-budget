package al.codepie.microservices.api.core.budgetexpense;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetExpense {
  private String id;
  private Long budgetSubItemId;
  private Double expenseAmount;
  private String description;
  private LocalDateTime registrationDate;
  private String serviceAddress;
}
