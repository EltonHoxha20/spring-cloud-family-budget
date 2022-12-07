package al.codepie.microservices.core.budgetexpense.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BudgetExpenseRepository extends MongoRepository<BudgetExpenseEntity, String> {

  List<BudgetExpenseEntity> findAllByRegistrationDateBetweenAndBudgetSubItemIdIn(LocalDateTime from, LocalDateTime after, List<Long> ids);

}
