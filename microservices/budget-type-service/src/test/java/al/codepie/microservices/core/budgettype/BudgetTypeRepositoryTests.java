package al.codepie.microservices.core.budgettype;

import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeEntity;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

//This annotation starts up a SQL database when the test starts.
@DataJpaTest
//By default, Spring Boot configures the tests to roll back updates to the SQL database to minimize
// the risk of negative side effects on other tests. In our case, this behavior will cause some of the tests to fail.
// Therefore, automatic rollback is disabled with the class level annotation @Transactional(propagation = NOT_SUPPORTED).
@Transactional(propagation = NOT_SUPPORTED)
//The @DataMongoTest and @DataJpaTest annotations are designed to start an embedded database by default.
// Since we want to use a containerized database, we have to disable this feature.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BudgetTypeRepositoryTests extends MySqlTestBase {

  @Autowired
  private BudgetTypeRepository repository;

  private BudgetTypeEntity savedEntity;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();

    BudgetTypeEntity entity = BudgetTypeEntity.builder().id(1L).type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).build();
    savedEntity = repository.save(entity);

    assertEqualsBudgetType(savedEntity, entity);

  }

  @Test
  void create() {
    BudgetTypeEntity newEntity = BudgetTypeEntity.builder().id(2L).type(BudgetTypeEnum.MONTHLY).totalIncome(543.21).build();
    repository.save(newEntity);

    BudgetTypeEntity foundEntity = repository.findById(newEntity.getId()).get();

    assertEqualsBudgetType(newEntity, foundEntity);
    assertEquals(2, repository.count());
  }

  @Test
  void update() {
    savedEntity.setTotalIncome(111.11);
    repository.save(savedEntity);

    BudgetTypeEntity foundEntity = repository.findById(savedEntity.getId()).get();

    assertEquals(1, foundEntity.getVersion());
    assertEquals(savedEntity.getTotalIncome(), foundEntity.getTotalIncome());

  }

  @Test
  void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  void findBudgetTypeById() {
    BudgetTypeEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEqualsBudgetType(savedEntity, foundEntity);
  }

  // it doesn't make sense to have duplicate error check since we only have primary key and not a compound of pk or indexes
//  @Test
//  void duplicateError() {
//    assertThrows(DataIntegrityViolationException.class, () -> {
//      BudgetTypeEntity entity = BudgetTypeEntity.builder().id(1L).type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).build();
//      repository.save(entity);
//    });
//  }

  @Test
  void optimisticLockError(){
    // Store the saved entity in two separate entity objects
    BudgetTypeEntity entity1 = repository.findById(savedEntity.getId()).get();
    BudgetTypeEntity entity2 = repository.findById(savedEntity.getId()).get();

    // Update the entity using the first entity object
    entity1.setTotalIncome(222.22);
    repository.save(entity1);

    // Update the entity using the second entity object.
    // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
    assertThrows(OptimisticLockingFailureException.class,()->{
      entity2.setTotalIncome(333.33);
      repository.save(entity2);
    });

    // Get the updated entity from the database and verify its new state
    BudgetTypeEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, updatedEntity.getVersion());
    assertEquals(222.22, updatedEntity.getTotalIncome());
  }

  @Test
  void paging() {

    repository.deleteAll();

    List<BudgetTypeEntity> newBudgetTypes = rangeClosed(2, 11)
        .mapToObj(i -> BudgetTypeEntity.builder().id((long) i).type(BudgetTypeEnum.MONTHLY).totalIncome((double) (i*2)).build())
        .collect(Collectors.toList());
    repository.saveAll(newBudgetTypes);

    Pageable nextPage = PageRequest.of(0, 4, ASC, "id");
    nextPage = testNextPage(nextPage, "[2, 3, 4, 5]", true);
    nextPage = testNextPage(nextPage, "[6, 7, 8, 9]", true);
    nextPage = testNextPage(nextPage, "[10, 11]", false);
  }

  private Pageable testNextPage(Pageable nextPage, String expectedBudgetTypeIds, boolean expectsNextPage) {
    Page<BudgetTypeEntity> budgetTypePage = repository.findAll(nextPage);
    assertEquals(expectedBudgetTypeIds, budgetTypePage.getContent().stream().map(BudgetTypeEntity::getId).toList().toString());
    assertEquals(expectsNextPage, budgetTypePage.hasNext());
    return budgetTypePage.nextPageable();
  }

  private void assertEqualsBudgetType(BudgetTypeEntity expectedEntity, BudgetTypeEntity actualEntity) {

    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getType(), actualEntity.getType());
    assertEquals(expectedEntity.getTotalIncome(), actualEntity.getTotalIncome());

  }

}
