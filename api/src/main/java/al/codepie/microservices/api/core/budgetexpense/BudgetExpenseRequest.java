package al.codepie.microservices.api.core.budgetexpense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetExpenseRequest {
  private LocalDateTime from;
  private LocalDateTime to;
  private List<Long> subItemIds;
}
