package al.codepie.microservices.core.budgetitem.service;

import al.codepie.microservices.api.core.budgetitem.BudgetItem;
import al.codepie.microservices.api.core.budgetitem.BudgetItemService;
import al.codepie.microservices.api.core.budgetitem.BudgetSubItem;
import al.codepie.microservices.api.exceptions.InvalidInputException;
import al.codepie.microservices.api.exceptions.NotFoundException;
import al.codepie.microservices.core.budgetitem.persistence.BudgetItemEntity;
import al.codepie.microservices.core.budgetitem.persistence.BudgetItemRepository;
import al.codepie.microservices.core.budgetitem.persistence.BudgetSubItemEntity;
import al.codepie.microservices.core.budgetitem.persistence.BudgetSubItemRepository;
import al.codepie.microservices.util.http.ServiceUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
public class BudgetItemServiceImpl implements BudgetItemService {

  // private final BudgetTypeService budgetTypeService;
  private final BudgetItemRepository budgetItemRepository;

  private final BudgetSubItemRepository budgetSubItemRepository;

  private final BudgetItemMapper budgetItemMapper;

  private final BudgetSubItemMapper budgetSubItemMapper;

  private final ServiceUtil serviceUtil;

  @Autowired
  public BudgetItemServiceImpl(/*BudgetTypeService budgetTypeService,*/ BudgetItemRepository budgetItemRepository, BudgetSubItemRepository budgetSubItemRepository,
                                                                        BudgetItemMapper budgetItemMapper, BudgetSubItemMapper budgetSubItemMapper,
                                                                        ServiceUtil serviceUtil) {
    // this.budgetTypeService = budgetTypeService;
    this.budgetItemRepository = budgetItemRepository;
    this.budgetSubItemRepository = budgetSubItemRepository;
    this.budgetItemMapper = budgetItemMapper;
    this.budgetSubItemMapper = budgetSubItemMapper;
    this.serviceUtil = serviceUtil;
  }

  @Override
  @Transactional(rollbackFor = {InvalidInputException.class})
  public BudgetItem createBudgetItem(BudgetItem body) {
    try {
      //  if (budgetTypeService.getBudgetType(body.getBudgetTypeId()) != null) {
      BudgetItemEntity entity = budgetItemMapper.apiToEntity(body);
      BudgetItemEntity newEntity = budgetItemRepository.save(entity);
      return budgetItemMapper.entityToApi(newEntity);
//      }
//      throw new NotFoundException("No Budget Type was found with Id :" + body.getBudgetTypeId());
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, budgetItem Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetItem updateBudgetItem(BudgetItem body) {
    try {
      //    if (budgetTypeService.getBudgetType(body.getBudgetTypeId()) != null) {
      Optional<BudgetItemEntity> foundEntity = budgetItemRepository.findById(body.getId());
      if (foundEntity.isPresent()) {
        BudgetItemEntity entityToUpdate = budgetItemMapper.apiToEntity(body);
        entityToUpdate.setId(foundEntity.get().getId());
        entityToUpdate.setVersion(foundEntity.get().getVersion());
        BudgetItemEntity updatedEntity = budgetItemRepository.save(entityToUpdate);
        return budgetItemMapper.entityToApi(updatedEntity);
      }
      throw new NotFoundException("No Budget Item was found with Id :" + body.getId());
//      }
//      throw new NotFoundException("No Budget Type was found with Id :" + body.getBudgetTypeId());
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, budgetItem Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetItem getBudgetItem(Long budgetItemId) {
    BudgetItemEntity entity = budgetItemRepository.findById(budgetItemId)
        .orElseThrow(() -> new NotFoundException("No Budget Item was found with Id :" + budgetItemId));
    BudgetItem response = budgetItemMapper.entityToApi(entity);

    response.setServiceAddress(serviceUtil.getServiceAddress());
    log.debug("getBudgetItem: Found Budget Item with id: {}", response.getId());
    return response;
  }

  @Override
  public void deleteBudgetItem(Long budgetItemId) {
    log.debug("deleteBudgetItem: Budget Item with id: {}", budgetItemId);
    budgetItemRepository.findById(budgetItemId).ifPresent(budgetItemRepository::delete);
  }

  @Override
  @Transactional(rollbackFor = {InvalidInputException.class})
  public BudgetSubItem createBudgetSubItem(BudgetSubItem body) {
    try {
      Optional<BudgetItemEntity> budgetItemEntity = budgetItemRepository.findById(body.getBudgetItemId());
      if (budgetItemEntity.isPresent()) {
        BudgetSubItemEntity entity = budgetSubItemMapper.apiToEntity(body);
        entity.setBudgetItem(budgetItemEntity.get());
        BudgetSubItemEntity newEntity = budgetSubItemRepository.save(entity);
        return budgetSubItemMapper.entityToApi(newEntity);
      }
      throw new NotFoundException("No Budget Item was found with Id :" + body.getBudgetItemId());
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, budgetItem Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetSubItem updateBudgetSubItem(BudgetSubItem body) {
    try {
      Optional<BudgetItemEntity> budgetItemEntity = budgetItemRepository.findById(body.getBudgetItemId());
      if (budgetItemEntity.isPresent()) {
        Optional<BudgetSubItemEntity> foundEntity = budgetSubItemRepository.findById(body.getId());
        if (foundEntity.isPresent()) {
          BudgetSubItemEntity entityToUpdate = budgetSubItemMapper.apiToEntity(body);
          entityToUpdate.setBudgetItem(budgetItemEntity.get());
          entityToUpdate.setId(foundEntity.get().getId());
          entityToUpdate.setVersion(foundEntity.get().getVersion());
          BudgetSubItemEntity updatedEntity = budgetSubItemRepository.save(entityToUpdate);
          return budgetSubItemMapper.entityToApi(updatedEntity);
        }
        throw new NotFoundException("No Budget Sub Item was found with Id :" + body.getId());
      }
      throw new NotFoundException("No Budget Item was found with Id :" + body.getBudgetItemId());
    } catch (DataIntegrityViolationException dive) {
      throw new InvalidInputException("Duplicate key, budgetItem Id: {}" + body.getId());
    }
  }

  @Override
  public BudgetSubItem getBudgetSubItem(Long budgetSubItemId) {
    BudgetSubItemEntity entity = budgetSubItemRepository.findById(budgetSubItemId)
        .orElseThrow(() -> new NotFoundException("No Budget Sub Item was found with Id :" + budgetSubItemId));
    BudgetSubItem response = budgetSubItemMapper.entityToApi(entity);

    response.setServiceAddress(serviceUtil.getServiceAddress());
    log.debug("getBudgetSubItem: Found Budget Sub Item with id: {}", response.getId());
    return response;
  }

  @Override
  public void deleteBudgetSubItem(Long budgetSubItemId) {
    log.debug("deleteBudgetSubItem: Budget Item with id: {}", budgetSubItemId);
    budgetSubItemRepository.findById(budgetSubItemId).ifPresent(budgetSubItemRepository::delete);
  }

  @Override
  public List<BudgetSubItem> getBudgetSubItemsByItemId(Long budgetItemId) {
    List<BudgetSubItemEntity> list = budgetSubItemRepository.findAllByBudgetItemId(budgetItemId);
    if (list.isEmpty()) {
      throw new NotFoundException("No Budget Sub Items were found with Item Id :" + budgetItemId);
    }
    return budgetSubItemMapper.entityListToApiList(list);
  }

  @Override
  public BudgetSubItem createBudgetItemAndBudgetSubItem(BudgetSubItem body) {
    throw new NotImplementedException();
  }
}
