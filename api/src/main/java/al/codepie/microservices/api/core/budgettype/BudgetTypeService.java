package al.codepie.microservices.api.core.budgettype;

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

@Tag(name = "BudgetType", description = "REST API for budget type information.")
public interface BudgetTypeService {

  /** Sample usage, see below.
   *
   * curl -X POST $HOST:$PORT/budget-type \
   *   -H "Content-Type: application/json" --data \
   *   '{"type":"Monthly","totalAmount":123.4}'
   *
   * @param body A JSON representation of the new budgetType
   * @return A JSON representation of the newly created budgetType
   */
  @Operation(
      summary = "${api.budget-type.create-budget-type.description}",
      description = "${api.budget-type.create-budget-type.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PostMapping(
      value = "/budget-type",
      consumes = "application/json",
      produces = "application/json")
  BudgetType createBudgetType(@RequestBody BudgetType body);

  /** Sample usage, see below.
   *
   * curl -X PUT $HOST:$PORT/budget-type \
   *   -H "Content-Type: application/json" --data \
   *   '{"id":123,"type":"Monthly","totalAmount":432.1}'
   *
   * @param body A JSON representation of the new budgetType
   * @return A JSON representation of the newly updated budgetType
   */
  @Operation(
      summary = "${api.budget-type.update-budget-type.description}",
      description = "${api.budget-type.update-budget-type.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
      @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
  })
  @PutMapping(
      value = "/budget-type",
      consumes = "application/json",
      produces = "application/json")
  BudgetType updateBudgetType(@RequestBody BudgetType body);

  /**
   * Sample usage: "curl $HOST:$PORT/budget-type/1".
   *
   * @param budgetTypeId Id of the budgetType
   * @return the budgetType
   */
  @Operation(
      summary = "${api.budget-type.get-budget-type.description}",
      description = "${api.budget-type.get-budget-type.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @GetMapping(
      value = "/budget-type/{budgetTypeId}",
      produces = "application/json")
  BudgetType getBudgetType(@PathVariable Long budgetTypeId);

  /**
   * Sample usage: "curl -X DELETE $HOST:$PORT/budget-type/1".
   *
   * @param budgetTypeId Id of the budgetType
   */
  @Operation(
      summary = "${api.budget-type.delete-budget-type.description}",
      description = "${api.budget-type.delete-budget-type.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}")
  })
  @DeleteMapping(
      value = "/budget-type/{budgetTypeId}")
  void deleteBudgetType(@PathVariable Long budgetTypeId);

}
