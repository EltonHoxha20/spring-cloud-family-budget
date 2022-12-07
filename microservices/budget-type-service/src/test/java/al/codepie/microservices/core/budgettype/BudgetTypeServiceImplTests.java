package al.codepie.microservices.core.budgettype;

import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BudgetTypeServiceImplTests extends MySqlTestBase{

  @Autowired
  private WebTestClient testClient;

  @Autowired
  private BudgetTypeRepository repository;

  @BeforeEach
  void setupDb() {
    repository.deleteAll();
  }

  @Test
  void getBudgetTypeById() {

    // test create budget type
    postAndVerifyBudgetType(BudgetType.builder().type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).serviceAddress("SA").build(), HttpStatus.OK);
    // count is 4 because of the child records
    assertEquals(4, repository.count());
    Long budgetTypeId = repository.findAll().iterator().next().getId();

    getAndVerifyBudgetType(budgetTypeId, HttpStatus.OK)
        .jsonPath("$.type").isEqualTo(BudgetTypeEnum.MONTHLY.name())
        .jsonPath("$.totalIncome").isEqualTo(123.45);

    // test update budget type
    putAndVerifyBudgetType(BudgetType.builder().id(budgetTypeId).type(BudgetTypeEnum.MONTHLY).totalIncome(54.321).serviceAddress("SA").build(), HttpStatus.OK);
    // count is 4 because of the child records
    assertEquals(4, repository.count());

    getAndVerifyBudgetType(budgetTypeId, HttpStatus.OK)
        .jsonPath("$.id").isEqualTo(budgetTypeId)
        .jsonPath("$.type").isEqualTo(BudgetTypeEnum.MONTHLY.name())
        .jsonPath("$.totalIncome").isEqualTo(54.321);
  }

  // no reason dealing with duplicate error since we only have auto incremented id
//  @Test
//  void duplicateError() {
//
//    assertEquals(0, repository.count());
//
//    postAndVerifyBudgetType(BudgetType.builder().type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).serviceAddress("SA").build(), HttpStatus.OK)
//        .jsonPath("$.totalIncome").isEqualTo(123.45);
//    // count is 4 because of the child records
//    assertEquals(4, repository.count());
//
//    postAndVerifyBudgetType(BudgetType.builder().type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).serviceAddress("SA").build(), HttpStatus.OK)
//        .jsonPath("$.path").isEqualTo("/budget-type")
//        .jsonPath("$.message").isEqualTo("Duplicate key, monthlyBudget Id: 1");
//    // count is 4 because of the child records
//    assertEquals(4, repository.count());
//  }

  @Test
  void deleteBudgetType() {

    postAndVerifyBudgetType(BudgetType.builder().type(BudgetTypeEnum.MONTHLY).totalIncome(123.45).serviceAddress("SA").build(), HttpStatus.OK)
        .jsonPath("$.totalIncome").isEqualTo(123.45);
    // count is 4 because of the child records
    assertEquals(4, repository.count());
    Long budgetTypeId = repository.findAll().iterator().next().getId();

    deleteAndVerifyBudgetType(budgetTypeId, HttpStatus.OK);
    assertEquals(0, repository.count());

    // we can try many times since we've made delete method idempotent
    deleteAndVerifyBudgetType(budgetTypeId, HttpStatus.OK);
  }

//  @Test
//  void getBudgetTypeMissingParameter() {
//    getAndVerifyBudgetType("", BAD_REQUEST)
//        .jsonPath("$.path").isEqualTo("/budget-type")
//        .jsonPath("$.message").isEqualTo("Required int parameter 'budgetTypeId' is not present");
//  }

  @Test
  void getBudgetTypeInvalidParameter() {
    getAndVerifyBudgetType("/no-number", BAD_REQUEST)
        .jsonPath("$.path").isEqualTo("/budget-type/no-number")
        .jsonPath("$.message").isEqualTo("Type mismatch.");
  }

  @Test
  void getBudgetTypeNotFound() {
    long budgetTypeId = 123L;
    getAndVerifyBudgetType("/" + budgetTypeId, NOT_FOUND)
        .jsonPath("$.path").isEqualTo("/budget-type/123")
        .jsonPath("$.message").isEqualTo("No Budget Type was found with Id :123");
  }


  private WebTestClient.BodyContentSpec getAndVerifyBudgetType(Long budgetTypeId, HttpStatus expectedStatus) {
    return getAndVerifyBudgetType("/" + budgetTypeId, expectedStatus);
  }

  private WebTestClient.BodyContentSpec getAndVerifyBudgetType(String budgetTypePath, HttpStatus expectedStatus) {
    return testClient.get()
        .uri("/budget-type" + budgetTypePath)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();
  }

  private WebTestClient.BodyContentSpec postAndVerifyBudgetType(BudgetType budgetType, HttpStatus expectedStatus) {
    return testClient.post()
        .uri("budget-type")
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(budgetType)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();
  }

  private WebTestClient.BodyContentSpec putAndVerifyBudgetType(BudgetType budgetType, HttpStatus expectedStatus) {
    return testClient.put()
        .uri("budget-type")
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(budgetType)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody();
  }

  private WebTestClient.BodyContentSpec deleteAndVerifyBudgetType(Long budgetTypeId, HttpStatus expectedStatus) {
    return testClient.delete()
        .uri("budget-type/" + budgetTypeId)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(expectedStatus)
        .expectBody();
  }

}
