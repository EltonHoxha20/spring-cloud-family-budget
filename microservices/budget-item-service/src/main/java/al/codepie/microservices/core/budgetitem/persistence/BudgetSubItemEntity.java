package al.codepie.microservices.core.budgetitem.persistence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "budget_sub_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSubItemEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Version
  private Long version;

  private String name;

  // monthly
  private Double projExpAmount;

  @ManyToOne
  private BudgetItemEntity budgetItem;
}
