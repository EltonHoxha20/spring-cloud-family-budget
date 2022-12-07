package al.codepie.microservices.core.budgetexpense;

import al.codepie.microservices.api.core.budgetexpense.BudgetExpense;
import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseRepository;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BudgetExpenseServiceImplTests {

	@Autowired
	private WebTestClient testClient;

	@Autowired
	private BudgetExpenseRepository repository;

	@BeforeEach
	void setupDb() {
		repository.deleteAll();
	}

	@Test
	void getBudgetExpenseById() {
		String budgetExpenseId ="abc";
		assertFalse(repository.findById(budgetExpenseId).isPresent());

		// test create budget type
		postAndVerifyBudgetExpense(BudgetExpense.builder().budgetSubItemId(1L)
				.expenseAmount(123.45).description("sample desc").serviceAddress("SA").build(), HttpStatus.OK);
		assertEquals(1, repository.count());

		budgetExpenseId = repository.findAll().get(0).getId();

		getAndVerifyBudgetExpense(budgetExpenseId, HttpStatus.OK)
				.jsonPath("$.id").isEqualTo(budgetExpenseId)
				.jsonPath("$.description").isEqualTo("sample desc")
				.jsonPath("$.expenseAmount").isEqualTo(123.45);

		// test update budget type
		putAndVerifyBudgetExpense(BudgetExpense.builder().id(budgetExpenseId).budgetSubItemId(1L)
				.expenseAmount(543.21).description("sample desc 2").serviceAddress("SA").build(), HttpStatus.OK);
		assertEquals(1, repository.count());

		getAndVerifyBudgetExpense(budgetExpenseId, HttpStatus.OK)
				.jsonPath("$.id").isEqualTo(budgetExpenseId)
				.jsonPath("$.description").isEqualTo("sample desc 2")
				.jsonPath("$.expenseAmount").isEqualTo(543.21);

	}


	@Test
	void deleteBudgetExpense() {
		postAndVerifyBudgetExpense(BudgetExpense.builder().budgetSubItemId(1L)
				.expenseAmount(543.21).description("sample desc").serviceAddress("SA").build(), OK)
				.jsonPath("$.expenseAmount").isEqualTo(543.21);
		assertEquals(1, repository.count());

		String budgetExpenseId = repository.findAll().get(0).getId();

		deleteAndVerifyBudgetExpense(budgetExpenseId, HttpStatus.OK);
		assertEquals(0, repository.count());

		// we can try many times since we've made delete method idempotent
		deleteAndVerifyBudgetExpense(budgetExpenseId, HttpStatus.OK);
	}

	@Test
	void getBudgetExpenseNotFound() {
		String budgetExpenseId = "123L";
		getAndVerifyBudgetExpense( budgetExpenseId, NOT_FOUND)
				.jsonPath("$.path").isEqualTo("/budget-expense/" + budgetExpenseId)
				.jsonPath("$.message").isEqualTo("No Budget Expense was found with Id :123L");
	}


	private WebTestClient.BodyContentSpec getAndVerifyBudgetExpense(String budgetExpenseId, HttpStatus expectedStatus) {
	return testClient.get()
				.uri("/budget-expense/"+  budgetExpenseId)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	private WebTestClient.BodyContentSpec postAndVerifyBudgetExpense(BudgetExpense budgetExpense, HttpStatus expectedStatus) {
		return testClient.post()
				.uri("budget-expense")
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(budgetExpense)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	private WebTestClient.BodyContentSpec putAndVerifyBudgetExpense(BudgetExpense budgetExpense, HttpStatus expectedStatus) {
		return testClient.put()
				.uri("budget-expense")
				.accept(MediaType.APPLICATION_JSON)
				.bodyValue(budgetExpense)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody();
	}

	private WebTestClient.BodyContentSpec deleteAndVerifyBudgetExpense(String budgetExpenseId, HttpStatus expectedStatus) {
		return testClient.delete()
				.uri("budget-expense/" + budgetExpenseId)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(expectedStatus)
				.expectBody();
	}
}
