package al.codepie.microservices.core.budgetitem.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetSubItemRepository extends PagingAndSortingRepository<BudgetSubItemEntity, Long> {

  List<BudgetSubItemEntity> findAllByBudgetItemId(Long id);
}
