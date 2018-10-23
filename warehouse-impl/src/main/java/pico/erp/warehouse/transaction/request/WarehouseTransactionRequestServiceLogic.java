package pico.erp.warehouse.transaction.request;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.pack.WarehousePackExceptions;
import pico.erp.warehouse.transaction.WarehouseTransactionData;
import pico.erp.warehouse.transaction.WarehouseTransactionRequests.InboundRequest;
import pico.erp.warehouse.transaction.WarehouseTransactionRequests.OutboundRequest;
import pico.erp.warehouse.transaction.WarehouseTransactionService;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionRequestServiceLogic implements WarehouseTransactionService {

  @Autowired
  private WarehouseTransactionRequestRepository warehouseTransactionRequestRepository;

  @Autowired
  private WarehouseTransactionRequestMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Override
  public WarehouseTransactionData inbound(InboundRequest request) {
    val transaction = new WarehouseTransactionRequest();
    val response = transaction.apply(mapper.map(request));
    if (warehouseTransactionRequestRepository.exists(transaction.getId())) {
      throw new WarehousePackExceptions.AlreadyExistsException();
    }
    val created = warehouseTransactionRequestRepository.create(transaction);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public WarehouseTransactionData outbound(OutboundRequest request) {
    val transaction = new WarehouseTransactionRequest();
    val response = transaction.apply(mapper.map(request));
    if (warehouseTransactionRequestRepository.exists(transaction.getId())) {
      throw new WarehousePackExceptions.AlreadyExistsException();
    }
    val created = warehouseTransactionRequestRepository.create(transaction);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }


}
