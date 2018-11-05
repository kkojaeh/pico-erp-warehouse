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
import pico.erp.warehouse.transaction.order.TransactionOrderRequests.AcceptRequest;
import pico.erp.warehouse.transaction.order.TransactionOrderRequests.CompleteRequest;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackId;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackRepository;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackRequests;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackService;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class TransactionOrderServiceLogic implements TransactionOrderService {

  @Autowired
  private TransactionOrderRepository orderRepository;

  @Autowired
  private TransactionOrderPackRepository orderPackRepository;

  @Autowired
  private TransactionOrderMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private AuditorAware<Auditor> auditorAware;

  @Autowired
  private TransactionOrderPackService orderPackService;

  @Override
  public void accept(AcceptRequest request) {
    val order = orderRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderExceptions.NotFoundException::new);
    val response = order.apply(mapper.map(request));
    orderRepository.update(order);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void cancel(TransactionOrderRequests.CancelRequest request) {
    val order = orderRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderExceptions.NotFoundException::new);
    val response = order.apply(mapper.map(request));
    orderRepository.update(order);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void commit(TransactionOrderRequests.CommitRequest request) {
    val order = orderRepository.findAggregatorBy(request.getId())
      .orElseThrow(TransactionOrderExceptions.NotFoundException::new);
    val response = order.apply(mapper.map(request));
    response.getSelectedPacks().forEach(pack -> {
      orderPackService.create(
        TransactionOrderPackRequests.CreateRequest.builder()
          .id(TransactionOrderPackId.generate())
          .orderId(order.getId())
          .packId(pack.getId())
          .build()
      );
    });
    orderRepository.update(order);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void complete(CompleteRequest request) {
    val order = orderRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderExceptions.NotFoundException::new);
    val response = order.apply(mapper.map(request));
    orderRepository.update(order);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public TransactionOrderData create(
    TransactionOrderRequests.CreateRequest request) {
    val order = new TransactionOrder();
    val response = order.apply(mapper.map(request));
    if (orderRepository.exists(order.getId())) {
      throw new TransactionOrderExceptions.AlreadyExistsException();
    }
    val created = orderRepository.create(order);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public boolean exists(TransactionOrderId id) {
    return orderRepository.exists(id);
  }

  @Override
  public TransactionOrderData get(TransactionOrderId id) {
    return orderRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(TransactionOrderExceptions.NotFoundException::new);
  }

  @Override
  public void update(TransactionOrderRequests.UpdateRequest request) {
    val order = orderRepository.findBy(request.getId())
      .orElseThrow(TransactionOrderExceptions.NotFoundException::new);
    val response = order.apply(mapper.map(request));
    orderRepository.update(order);
    eventPublisher.publishEvents(response.getEvents());
  }

}
