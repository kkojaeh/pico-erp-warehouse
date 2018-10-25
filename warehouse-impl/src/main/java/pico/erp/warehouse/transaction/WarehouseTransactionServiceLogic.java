package pico.erp.warehouse.transaction;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.WarehouseTransactionRequests.CreateRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionServiceLogic implements WarehouseTransactionService {

  @Autowired
  private WarehouseTransactionRepository warehouseTransactionRepository;

  @Autowired
  private WarehouseTransactionMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Override
  public WarehouseTransactionData create(CreateRequest request) {
    val transaction = new WarehouseTransaction();
    val response = transaction.apply(mapper.map(request));
    if (warehouseTransactionRepository.exists(transaction.getId())) {
      throw new WarehouseTransactionExceptions.AlreadyExistsException();
    }
    val created = warehouseTransactionRepository.create(transaction);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

}
