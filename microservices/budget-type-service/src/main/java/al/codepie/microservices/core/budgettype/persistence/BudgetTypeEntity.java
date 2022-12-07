package al.codepie.microservices.core.budgettype.persistence;

import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Set;

@Entity
@Table(name = "budget_type")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Where(clause = "parent IS NULL")
public class BudgetTypeEntity {

  @Id
  @GeneratedValue
  private Long id;

  @Version
  private Long version;

  private Double totalIncome;

  @Enumerated(EnumType.STRING)
  private BudgetTypeEnum type;

  @ManyToOne
  private BudgetTypeEntity parent;

  @OneToMany(mappedBy = "parent")
  private Set<BudgetTypeEntity> children;


}
