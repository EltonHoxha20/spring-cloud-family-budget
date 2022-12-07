package al.codepie.microservices.api.core.budgetexpense;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@Tag(name = "BudgetExpense", description = "REST API for budget expense information.")
public interface BudgetExpenseService {

  /** Sample usage, see below.
   *
   * curl -X POST $HOST:$PORT/budget-expense \
   *   -H "Content-Type: application/json" --data \
   *   '{"budgetSubItemId":123, "registrationDate":"12-02-2022T12:34:45+02:00","expenseAmount":123.4, "description":"bought groceries eggs etc"}'
   *
   * @param body A JSON representation of the new BudgetExpense
   * @return A JSON representation of the newly created BudgetExpense
   */
  @Operation(
      summary = "${api.budget-expense.create-budget-expense.description}",
      description = "${api.budget-expense.create-budget-expense.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PostMapping(
      value = "/budget-expense",
      consumes = "application/json",
      produces = "application/json")
  BudgetExpense createBudgetExpense(@RequestBody BudgetExpense body);

  /** Sample usage, see below.
   *
   * curl -X PUT $HOST:$PORT/budget-expense \
   *   -H "Content-Type: application/json" --data \
   *   '{"id":1, "budgetSubItemId":123, "registrationDate":"12-02-2022T12:34:45+02:00","expenseAmount":123.4, "description":"paid the electricity bill for november"}'
   *
   * @param body A JSON representation of the new BudgetExpense
   * @return A JSON representation of the newly created BudgetExpense
   */
  @Operation(
      summary = "${api.budget-expense.update-budget-expense.description}",
      description = "${api.budget-expense.update-budget-expense.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PutMapping(
      value = "/budget-expense",
      consumes = "application/json",
      produces = "application/json")
  BudgetExpense updateBudgetExpense(@RequestBody BudgetExpense body);

  /**
   * Sample usage: "curl $HOST:$PORT/budget-expense/1".
   *
   * @param budgetExpenseId Id of the BudgetExpense
   * @return the BudgetExpense
   */
  @Operation(
      summary = "${api.budget-expense.get-budget-expense.description}",
      description = "${api.budget-expense.get-budget-expense.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @GetMapping(
      value = "/budget-expense/{budgetExpenseId}",
      produces = "application/json")
  BudgetExpense getBudgetExpense(@PathVariable String budgetExpenseId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/budget-expense/1".
   *
   * @param budgetExpenseId Id of the BudgetExpense
   */
  @Operation(
      summary = "${api.budget-expense.delete-budget-expense.description}",
      description = "${api.budget-expense.delete-budget-expense.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}")
  })
  @DeleteMapping(
      value = "/budget-expense/{budgetExpenseId}")
  void deleteBudgetExpense(@PathVariable String budgetExpenseId);

  /**
   * Sample usage: "curl $HOST:$PORT/budget-expense/aggregate".
   *
   * curl -X POST $HOST:$PORT/budget-expense/aggregate \
   *   -H "Content-Type: application/json" --data \
   *   '{"from":"12-02-2022T12:34:45+02:00","to":"12-02-2022T12:34:45+02:00", "subItemIds":[1,2,3]}'
   *
   * @param request A JSON representation of the BudgetExpenseRequest
   * @return the list of BudgetExpenses
   */
  @Operation(
      summary = "${api.budget-expense.get-budget-expense-aggregate.description}",
      description = "${api.budget-expense.get-budget-expense-aggregate.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @PostMapping(
      value = "/budget-expense/aggregate",
      consumes = "application/json",
      produces = "application/json")
  List<BudgetExpense> getBudgetExpenses(@RequestBody BudgetExpenseRequest request);
}
