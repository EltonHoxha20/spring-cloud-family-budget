package al.codepie.microservices.core.budgetitem.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetItemRepository extends PagingAndSortingRepository<BudgetItemEntity, Long> {
}
