package al.codepie.microservices.core.budgetexpense.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Document(collection = "budget_expense")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetExpenseEntity {
  @Id
  private String id;

  @Version
  private Long version;

  private Long budgetSubItemId;

  private LocalDateTime registrationDate;

  private Double expenseAmount;

  private String description;
}
