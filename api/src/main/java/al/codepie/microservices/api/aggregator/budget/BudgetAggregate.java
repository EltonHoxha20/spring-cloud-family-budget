package al.codepie.microservices.api.aggregator.budget;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAggregate {

  private String itemName;
  private Double totalIncome;
  private Double projectedExpense;
  private Double actualExpense;
  private Double difference;
  private Long itemId;
  private LocalDateTime fromDate;
  private LocalDateTime toDate;
  private String serviceAddress;

}
