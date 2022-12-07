package al.codepie.microservices.api.core.budgetitem;

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
@Tag(name = "BudgetItem", description = "REST API for budget item and sub item information.")

public interface BudgetItemService {

  /** Sample usage, see below.
   *
   * curl -X POST $HOST:$PORT/budget-item \
   *   -H "Content-Type: application/json" --data \
   *   '{"name":"Home Expenses","budgetTypeId":1, "projectedExpenseAmount":123.4}'
   *
   * @param body A JSON representation of the new budgetItem
   * @return A JSON representation of the newly created budgetItem
   */
  @Operation(
      summary = "${api.budget-item.create-budget-item.description}",
      description = "${api.budget-item.create-budget-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PostMapping(
      value = "/budget-item",
      consumes = "application/json",
      produces = "application/json")
  BudgetItem createBudgetItem(@RequestBody BudgetItem body);

  /** Sample usage, see below.
   *
   * curl -X PUT $HOST:$PORT/budget-item \
   *   -H "Content-Type: application/json" --data \
   *   '{"id":123, "name":"Home Expenses","budgetTypeId":1, "projectedExpenseAmount":123.4}'
   *
   * @param body A JSON representation of the new budgetItem
   * @return A JSON representation of the newly created budgetItem
   */
  @Operation(
      summary = "${api.budget-item.update-budget-item.description}",
      description = "${api.budget-item.update-budget-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PutMapping(
      value = "/budget-item",
      consumes = "application/json",
      produces = "application/json")
  BudgetItem updateBudgetItem(@RequestBody BudgetItem body);

  /**
   * Sample usage: "curl $HOST:$PORT/budget-item/1".
   *
   * @param budgetItemId Id of the budgetItem
   * @return the BudgetItem
   */
  @Operation(
      summary = "${api.budget-item.get-budget-item.description}",
      description = "${api.budget-item.get-budget-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @GetMapping(
      value = "/budget-item/{budgetItemId}",
      produces = "application/json")
  BudgetItem getBudgetItem(@PathVariable Long budgetItemId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/budget-item/1".
   *
   * @param budgetItemId Id of the BudgetItem
   */
  @Operation(
      summary = "${api.budget-item.delete-budget-item.description}",
      description = "${api.budget-item.delete-budget-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}")
  })
  @DeleteMapping(
      value = "/budget-item/{budgetItemId}")
  void deleteBudgetItem(@PathVariable Long budgetItemId);

  /** Sample usage, see below.
   *
   * curl -X POST $HOST:$PORT/budget-item/sub-item \
   *   -H "Content-Type: application/json" --data \
   *   '{"name":"Food","budgetItemId":1, }'
   *
   * @param body A JSON representation of the new budgetSubItem
   * @return A JSON representation of the newly created budgetSubItem
   */
  @Operation(
      summary = "${api.budget-item.create-budget-sub-item.description}",
      description = "${api.budget-item.create-budget-sub-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PostMapping(
      value = "/budget-item/sub-item",
      consumes = "application/json",
      produces = "application/json")
  BudgetSubItem createBudgetSubItem(@RequestBody BudgetSubItem body);

  /** Sample usage, see below.
   *
   * curl -X PUT $HOST:$PORT/budget-item/sub-item \
   *   -H "Content-Type: application/json" --data \
   *   '{"id":123, "name":"Food","budgetItemId":1, }'
   *
   * @param body A JSON representation of the new budgetSubItem
   * @return A JSON representation of the newly created budgetSubItem
   */
  @Operation(
      summary = "${api.budget-item.update-budget-sub-item.description}",
      description = "${api.budget-item.update-budget-sub-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PutMapping(
      value = "/budget-item/sub-item",
      consumes = "application/json",
      produces = "application/json")
  BudgetSubItem updateBudgetSubItem(@RequestBody BudgetSubItem body);

  /**
   * Sample usage: "curl $HOST:$PORT/budget-item/sub-item/1".
   *
   * @param budgetSubItemId Id of the budgetSubItem
   * @return the BudgetSubItem
   */
  @Operation(
      summary = "${api.budget-item.get-budget-sub-item.description}",
      description = "${api.budget-item.get-budget-sub-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @GetMapping(
      value = "/budget-item/sub-item/{budgetSubItemId}",
      produces = "application/json")
  BudgetSubItem getBudgetSubItem(@PathVariable Long budgetSubItemId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/budget-item/sub-item/1".
   *
   * @param budgetSubItemId Id of the BudgetItem
   */
  @Operation(
      summary = "${api.budget-item.delete-budget-sub-item.description}",
      description = "${api.budget-item.delete-budget-sub-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}")
  })
  @DeleteMapping(
      value = "/budget-item/sub-item/{budgetSubItemId}")
  void deleteBudgetSubItem(@PathVariable Long budgetSubItemId);

  /**
   * Sample usage: "curl $HOST:$PORT/budget-item/sub-items-by-item/1".
   *
   * @param budgetItemId Id of the budgetItem
   * @return the list of BudgetSubItems by ItemId
   */
  @Operation(
      summary = "${api.budget-item.get-budget-sub-items-by-item.description}",
      description = "${api.budget-item.get-budget-sub-items-by-item.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @GetMapping(
      value = "/budget-item/sub-items-by-item/{budgetItemId}",
      produces = "application/json")
  List<BudgetSubItem> getBudgetSubItemsByItemId(@PathVariable Long budgetItemId);


  /** Sample usage, see below.
   *
   * curl -X POST $HOST:$PORT/create-budget-item-and-sub-item \
   *   -H "Content-Type: application/json" --data \
   *   '{"budgetItemName":"Home Expenses","budgetTypeId":1, "projectedExpenseAmount":123.4, "name":"Food"}'
   *
   * @param body A JSON representation of the new budgetItem
   * @return A JSON representation of the newly created budgetItem
   */
  @PostMapping(
      value = "/create-budget-item-and-sub-item",
      consumes = "application/json",
      produces = "application/json")
  BudgetSubItem createBudgetItemAndBudgetSubItem(@RequestBody BudgetSubItem body);

}
