package al.codepie.microservices.core.budgetexpense;

import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseEntity;
import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
public class BudgetExpenseRepositoryTests {

  @Autowired
  private BudgetExpenseRepository repository;

  private BudgetExpenseEntity savedEntity;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();

    BudgetExpenseEntity entity = BudgetExpenseEntity.builder().id("abc")
        .budgetSubItemId(1L).expenseAmount(123.45).description("sample desc").build();
    savedEntity = repository.save(entity);

    assertEqualsBudgetExpense(savedEntity, entity);

  }

  @Test
  void create() {
    BudgetExpenseEntity newEntity =BudgetExpenseEntity.builder().id("cba")
        .budgetSubItemId(1L).expenseAmount(543.21).description("sample desc 2").build();
    repository.save(newEntity);

    BudgetExpenseEntity foundEntity = repository.findById(newEntity.getId()).get();

    assertEqualsBudgetExpense(newEntity, foundEntity);
    assertEquals(2, repository.count());
  }

  @Test
  void update() {
    savedEntity.setExpenseAmount(111.11);
    repository.save(savedEntity);

    BudgetExpenseEntity foundEntity = repository.findById(savedEntity.getId()).get();

    assertEquals(1, foundEntity.getVersion());
    assertEquals(savedEntity.getExpenseAmount(), foundEntity.getExpenseAmount());

  }

  @Test
  void delete() {
    repository.delete(savedEntity);
    assertFalse(repository.existsById(savedEntity.getId()));
  }

  @Test
  void findBudgetTypeById() {
    BudgetExpenseEntity foundEntity = repository.findById(savedEntity.getId()).get();
    assertEqualsBudgetExpense(savedEntity, foundEntity);
  }

  // it doesn't make sense to have duplicate error check since we only have primary key and not a compound of pk or indexes
//  @Test
//  void duplicateError() {
//    assertThrows(DataIntegrityViolationException.class, () -> {
//      BudgetExpenseEntity entity = BudgetExpenseEntity.builder().id("abc")
//        .budgetSubItemId(1L).expenseAmount(123.45).description("sample desc").build();
//      repository.save(entity);
//    });
//  }

  @Test
  void optimisticLockError(){
    // Store the saved entity in two separate entity objects
    BudgetExpenseEntity entity1 = repository.findById(savedEntity.getId()).get();
    BudgetExpenseEntity entity2 = repository.findById(savedEntity.getId()).get();

    // Update the entity using the first entity object
    entity1.setExpenseAmount(222.22);
    repository.save(entity1);

    // Update the entity using the second entity object.
    // This should fail since the second entity now holds an old version number, i.e. an Optimistic Lock Error
    assertThrows(OptimisticLockingFailureException.class,()->{
      entity2.setExpenseAmount(333.33);
      repository.save(entity2);
    });

    // Get the updated entity from the database and verify its new state
    BudgetExpenseEntity updatedEntity = repository.findById(savedEntity.getId()).get();
    assertEquals(1, updatedEntity.getVersion());
    assertEquals(222.22, updatedEntity.getExpenseAmount());
  }

  private void assertEqualsBudgetExpense(BudgetExpenseEntity expectedEntity, BudgetExpenseEntity actualEntity) {
    assertEquals(expectedEntity.getId(), actualEntity.getId());
    assertEquals(expectedEntity.getVersion(), actualEntity.getVersion());
    assertEquals(expectedEntity.getExpenseAmount(), actualEntity.getExpenseAmount());
    assertEquals(expectedEntity.getBudgetSubItemId(), actualEntity.getBudgetSubItemId());
    assertEquals(expectedEntity.getDescription(), actualEntity.getDescription());
    assertEquals(expectedEntity.getRegistrationDate(), actualEntity.getRegistrationDate());
  }


}
