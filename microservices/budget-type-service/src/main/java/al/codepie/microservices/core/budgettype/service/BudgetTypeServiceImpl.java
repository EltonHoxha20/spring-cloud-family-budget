package al.codepie.microservices.core.budgettype.service;

import al.codepie.microservices.api.core.budgettype.BudgetType;
import al.codepie.microservices.api.core.budgettype.BudgetTypeService;
import al.codepie.microservices.api.exceptions.InvalidInputException;
import al.codepie.microservices.api.exceptions.NotFoundException;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeEntity;
import al.codepie.microservices.api.core.budgettype.BudgetTypeEnum;
import al.codepie.microservices.core.budgettype.persistence.BudgetTypeRepository;
import al.codepie.microservices.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Log4j2
public class BudgetTypeServiceImpl implements BudgetTypeService {

  private final BudgetTypeRepository repository;
  private final BudgetTypeMapper mapper;
  private final ServiceUtil serviceUtil;

  @Autowired
  public BudgetTypeServiceImpl(BudgetTypeRepository repository, BudgetTypeMapper mapper, ServiceUtil serviceUtil) {
    this.repository = repository;
    this.mapper = mapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  @Transactional(rollbackFor = {InvalidInputException.class})
  public BudgetType createBudgetType(BudgetType body) {
    try {
      BudgetTypeEntity entity = mapper.apiToEntity(body);
      entity.setType(BudgetTypeEnum.MONTHLY);
      entity.setParent(null);
      BudgetTypeEntity newEntity = repository.save(entity);

      createOrUpdateBudgetTypeChildren(newEntity);

      log.debug("createBudgetType: monthlyBudget Id: {}, monthlyBudget TotalIncome: {}", newEntity.getId(), newEntity.getTotalIncome());
      return mapper.entityToApi(newEntity);

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, monthlyBudget Id: {}" + body.getId());
    }

  }

  @Override
  @Transactional(rollbackFor = {InvalidInputException.class})
  public BudgetType updateBudgetType(BudgetType body) {
    try {
      BudgetTypeEntity updatedEntity;
      Optional<BudgetTypeEntity> foundEntity = repository.findById(body.getId());
      if (foundEntity.isPresent()) {
        BudgetTypeEntity entityToUpdate = mapper.apiToEntity(body);
        entityToUpdate.setId(foundEntity.get().getId());
        entityToUpdate.setVersion(foundEntity.get().getVersion());
        updatedEntity = repository.save(entityToUpdate);
        createOrUpdateBudgetTypeChildren(updatedEntity);
        log.debug("updateBudgetType: monthlyBudget Id: {}, monthlyBudget TotalIncome: {}", updatedEntity.getId(), updatedEntity.getTotalIncome());
        return mapper.entityToApi(updatedEntity);
      } else {
        throw new NotFoundException("No Budget Type was found with Id :" + body.getId());
      }

    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, monthlyBudget Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetType getBudgetType(Long budgetTypeId) {
    BudgetTypeEntity entity = repository.findByIdAndParentIsNullAndType(budgetTypeId, BudgetTypeEnum.MONTHLY)
        .orElseThrow(() -> new NotFoundException("No Budget Type was found with Id :" + budgetTypeId));
    BudgetType response = mapper.entityToApi(entity);

    response.setServiceAddress(serviceUtil.getServiceAddress());
    log.debug("getBudgetType: Found Budget Type with id: {}", response.getId());
    return response;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteBudgetType(Long budgetTypeId) {
    log.debug("deleteBudgetType: Budget Type with id: {}", budgetTypeId);
    repository.findById(budgetTypeId).ifPresent(b -> {
      repository.deleteAllByParentId(budgetTypeId);
      repository.delete(b);
    });
  }


  private void createOrUpdateBudgetTypeChildren(BudgetTypeEntity newEntity) {
    double dailyIncome = serviceUtil.formatDouble(newEntity.getTotalIncome() / 30);
    Double weeklyIncome = serviceUtil.formatDouble(dailyIncome * 7);
    Double yearlyIncome = serviceUtil.formatDouble(newEntity.getTotalIncome() * 12);

    // if it is an update, we first need to delete all the children of the updated parent
    repository.deleteAllByParentId(newEntity.getId());

    // create daily entity
    BudgetTypeEntity daily = new BudgetTypeEntity();
    daily.setType(BudgetTypeEnum.DAILY);
    daily.setParent(newEntity);
    daily.setTotalIncome(dailyIncome);
    daily = repository.save(daily);
    log.debug("createBudgetTypeChildren: dailyBudget Id: {}, dailyBudget TotalIncome: {}", daily.getId(), daily.getTotalIncome());

    // create weekly entity
    BudgetTypeEntity weekly = new BudgetTypeEntity();
    weekly.setType(BudgetTypeEnum.WEEKLY);
    weekly.setParent(newEntity);
    weekly.setTotalIncome(weeklyIncome);
    weekly = repository.save(weekly);
    log.debug("createBudgetTypeChildren: weeklyBudget Id: {}, weeklyBudget TotalIncome: {}", weekly.getId(), weekly.getTotalIncome());

    // create yearly entity
    BudgetTypeEntity yearly = new BudgetTypeEntity();
    yearly.setType(BudgetTypeEnum.YEARLY);
    yearly.setParent(newEntity);
    yearly.setTotalIncome(yearlyIncome);
    yearly = repository.save(yearly);
    log.debug("createBudgetTypeChildren: yearlyBudget Id: {}, yearlyBudget TotalIncome: {}", yearly.getId(), yearly.getTotalIncome());

  }
}
