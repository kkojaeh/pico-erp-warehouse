package pico.erp.warehouse.transaction.order;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderRequests.AcceptRequest;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderRequests.CompleteRequest;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class WarehouseTransactionOrderServiceLogic implements WarehouseTransactionOrderService {

  @Autowired
  private WarehouseTransactionOrderRepository warehouseTransactionOrderRepository;

  @Autowired
  private WarehouseTransactionOrderMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public void accept(AcceptRequest request) {
    val transactionOrder = warehouseTransactionOrderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    warehouseTransactionOrderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(WarehouseTransactionOrderRequests.CancelRequest request) {
    val transactionOrder = warehouseTransactionOrderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    warehouseTransactionOrderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void commit(WarehouseTransactionOrderRequests.CommitRequest request) {
    val transactionOrder = warehouseTransactionOrderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    warehouseTransactionOrderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val transactionOrder = warehouseTransactionOrderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    warehouseTransactionOrderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public WarehouseTransactionOrderData create(
    WarehouseTransactionOrderRequests.CreateRequest request) {
    val transactionOrder = new WarehouseTransactionOrder();
    val response = transactionOrder.apply(mapper.map(request));
    if (warehouseTransactionOrderRepository.exists(transactionOrder.getId())) {
      throw new WarehouseTransactionOrderExceptions.AlreadyExistsException();
    }
    val created = warehouseTransactionOrderRepository.create(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(WarehouseTransactionOrderId id) {
    return warehouseTransactionOrderRepository.exists(id);
  }

  @Override
  public WarehouseTransactionOrderData get(WarehouseTransactionOrderId id) {
    return warehouseTransactionOrderRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
  }

  @Override
  public void update(WarehouseTransactionOrderRequests.UpdateRequest request) {
    val transactionOrder = warehouseTransactionOrderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    warehouseTransactionOrderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

}
