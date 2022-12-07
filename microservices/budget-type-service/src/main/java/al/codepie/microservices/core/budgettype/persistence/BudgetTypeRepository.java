package al.codepie.microservices.core.budgettype.persistence;

import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetTypeRepository extends PagingAndSortingRepository<BudgetTypeEntity, Long> {

  @Transactional(readOnly = true)
  List<BudgetTypeEntity> findByType(String type);

  @Transactional(readOnly = true)
  Optional<BudgetTypeEntity> findById(Long id);

  @Transactional(readOnly = true)
  Optional<BudgetTypeEntity> findByIdAndType(Long id, BudgetTypeEnum type);

  @Transactional(readOnly = true)
  Optional<BudgetTypeEntity> findByIdAndParentIsNull(Long id);

  @Transactional(readOnly = true)
  Optional<BudgetTypeEntity> findByIdAndParentIsNullAndType(Long id, BudgetTypeEnum type);

  void deleteAllByParentId(Long id);

}
