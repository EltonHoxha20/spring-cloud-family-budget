package al.codepie.microservices.api.aggregator.budget;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Tag(name = "BudgetAggregate", description = "REST API for budget aggregate information.")
public interface BudgetAggregateService {

  /**
   * Sample usage: "curl $HOST:$PORT/budget-aggregate?from=21-02-2022T01:01:01+02:00&to=22-02-2022T01:01:01+02:00&itemId=123".
   *
   * @param from   fromDate
   * @param to     toDate
   * @param itemId id of the BudgetItem
   * @return the BudgetAggregate
   */
  @Operation(
      summary = "${api.budget-aggregate.get-budget-aggregate.description}",
      description = "${api.budget-aggregate.get-budget-aggregate.notes}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
      @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}")
  })
  @GetMapping(
      value = "/budget-aggregate",
      produces = "application/json")
  BudgetAggregate getBudgetAggregateExpense(@RequestParam(value = "from", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                            LocalDateTime from,
                                            @RequestParam(value = "to", required = false)
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                            LocalDateTime to,
                                            @RequestParam(value = "itemId") Long itemId);

}
