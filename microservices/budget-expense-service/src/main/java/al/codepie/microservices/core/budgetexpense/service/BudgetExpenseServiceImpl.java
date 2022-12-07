package al.codepie.microservices.core.budgetexpense.service;

import al.codepie.microservices.api.core.budgetexpense.BudgetExpense;
import al.codepie.microservices.api.core.budgetexpense.BudgetExpenseRequest;
import al.codepie.microservices.api.core.budgetexpense.BudgetExpenseService;
import al.codepie.microservices.api.exceptions.InvalidInputException;
import al.codepie.microservices.api.exceptions.NotFoundException;
import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseEntity;
import al.codepie.microservices.core.budgetexpense.persistence.BudgetExpenseRepository;
import al.codepie.microservices.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
public class BudgetExpenseServiceImpl implements BudgetExpenseService {

  // private final BudgetItemService budgetItemService;
  private final BudgetExpenseRepository repository;

  private final BudgetExpenseMapper mapper;

  private final ServiceUtil serviceUtil;

  @Autowired
  public BudgetExpenseServiceImpl(/*BudgetItemService budgetItemService,*/ BudgetExpenseRepository repository,
                                                                           BudgetExpenseMapper mapper, ServiceUtil serviceUtil) {
    //  this.budgetItemService = budgetItemService;
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  @Transactional(rollbackFor = {InvalidInputException.class})
  public BudgetExpense createBudgetExpense(BudgetExpense body) {
    try {
      //   if (budgetItemService.getBudgetSubItem(body.getBudgetSubItemId()) != null) {
      BudgetExpenseEntity entity = mapper.apiToEntity(body);
      entity.setRegistrationDate(LocalDateTime.now());
      BudgetExpenseEntity newEntity = repository.save(entity);
      return mapper.entityToApi(newEntity);
//      }
//      throw new NotFoundException("No Budget Sub Item was found with Id :" + body.getBudgetSubItemId());
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, budgetExpense Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetExpense updateBudgetExpense(BudgetExpense body) {
    try {
      //     if (budgetItemService.getBudgetSubItem(body.getBudgetSubItemId()) != null) {
      Optional<BudgetExpenseEntity> foundEntity = repository.findById(body.getId());
      if (foundEntity.isPresent()) {
        BudgetExpenseEntity entityToUpdate = mapper.apiToEntity(body);
        entityToUpdate.setId(foundEntity.get().getId());
        entityToUpdate.setVersion(foundEntity.get().getVersion());
        entityToUpdate.setRegistrationDate(LocalDateTime.now());
        BudgetExpenseEntity updatedEntity = repository.save(entityToUpdate);
        return mapper.entityToApi(updatedEntity);
      }
      throw new NotFoundException("No Budget Expense was found with Id :" + body.getId());
//      }
//      throw new NotFoundException("No Budget Sub Item was found with Id :" + body.getBudgetSubItemId());
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, budgetExpense Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetExpense getBudgetExpense(String budgetExpenseId) {
    BudgetExpenseEntity entity = repository.findById(budgetExpenseId)
        .orElseThrow(() -> new NotFoundException("No Budget Expense was found with Id :" + budgetExpenseId));
    BudgetExpense response = mapper.entityToApi(entity);

    response.setServiceAddress(serviceUtil.getServiceAddress());
    log.debug("getBudgetExpense: Found Budget Expense with id: {}", response.getId());
    return response;
  }

  @Override
  public void deleteBudgetExpense(String budgetExpenseId) {
    log.debug("deleteBudgetExpense: Budget Expense with id: {}", budgetExpenseId);
    repository.findById(budgetExpenseId).ifPresent(repository::delete);
  }

  @Override
  public List<BudgetExpense> getBudgetExpenses(BudgetExpenseRequest request) {
    log.debug("getBudgetExpenses: Budget Expense with from: {}, to: {}, ids: {}", request.getFrom(), request.getTo(), request.getSubItemIds());
    List<BudgetExpenseEntity> list = repository.findAllByRegistrationDateBetweenAndBudgetSubItemIdIn(request.getFrom(), request.getTo(), request.getSubItemIds());
    if (list.isEmpty()) {
      throw new NotFoundException("No Budget Expenses were found with ids:" + request.getSubItemIds());
    }
    return mapper.entityListToApiList(list);
  }
}
