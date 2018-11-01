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
  private WarehouseTransactionOrderRepository orderRepository;

  @Autowired
  private WarehouseTransactionOrderMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Override
  public void accept(AcceptRequest request) {
    val transactionOrder = orderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(WarehouseTransactionOrderRequests.CancelRequest request) {
    val transactionOrder = orderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void commit(WarehouseTransactionOrderRequests.CommitRequest request) {
    val transactionOrder = orderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val transactionOrder = orderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public WarehouseTransactionOrderData create(
    WarehouseTransactionOrderRequests.CreateRequest request) {
    val transactionOrder = new WarehouseTransactionOrder();
    val response = transactionOrder.apply(mapper.map(request));
    if (orderRepository.exists(transactionOrder.getId())) {
      throw new WarehouseTransactionOrderExceptions.AlreadyExistsException();
    }
    val created = orderRepository.create(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(WarehouseTransactionOrderId id) {
    return orderRepository.exists(id);
  }

  @Override
  public WarehouseTransactionOrderData get(WarehouseTransactionOrderId id) {
    return orderRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
  }

  @Override
  public void update(WarehouseTransactionOrderRequests.UpdateRequest request) {
    val transactionOrder = orderRepository.findBy(request.getId())
      .orElseThrow(WarehouseTransactionOrderExceptions.NotFoundException::new);
    val response = transactionOrder.apply(mapper.map(request));
    orderRepository.update(transactionOrder);
    eventPublisher.publishEvents(response.getEvents());
  }

}
