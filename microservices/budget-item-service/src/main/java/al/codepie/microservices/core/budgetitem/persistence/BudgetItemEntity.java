package al.codepie.microservices.core.budgetitem.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "budget_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetItemEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Version
  private Long version;

  private String name;

  // monthly
  private Double projExpAmount;

  private Long budgetTypeId;

}
