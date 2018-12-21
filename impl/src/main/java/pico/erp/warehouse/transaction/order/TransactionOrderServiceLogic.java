package pico.erp.warehouse.transaction.order;

import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
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
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemRequests;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemService;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotId;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotRequests;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotService;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackId;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackRepository;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackRequests;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPackService;
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.TransactionRequestService;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemService;
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotService;

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

  @Autowired
  private TransactionRequestService requestService;

  @Autowired
  private TransactionRequestItemService requestItemService;

  @Autowired
  private TransactionRequestItemLotService requestItemLotService;

  @Autowired
  private TransactionOrderItemService orderItemService;

  @Autowired
  private TransactionOrderItemLotService orderItemLotService;

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
    if (orderRepository.exists(order.getCode())) {
      throw new TransactionOrderExceptions.CodeAlreadyExistsException();
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

  public TransactionOrderData generateBy(GenerateByRequestRequest req) {
    val requestId = req.getRequestId();
    val orderId = TransactionOrderId.generate();
    val request = requestService.get(requestId);
    val orderCreateRequest = TransactionOrderRequests.CreateRequest.builder()
      .id(orderId)
      .dueDate(request.getDueDate())
      .transactionCompanyId(request.getTransactionCompanyId())
      .stationId(request.getStationId())
      .type(request.getType())
      .transactionRequestId(requestId)
      .quantityCorrectionPolicy(request.getQuantityCorrectionPolicy())
      .build();
    val created = create(orderCreateRequest);

    val itemLots = requestItemLotService.getAll(requestId).stream()
      .collect(Collectors.groupingBy(requestItemLot -> requestItemLot.getRequestItemId()));

    requestItemService.getAll(requestId)
      .forEach(requestItem -> {
        val orderItemId = TransactionOrderItemId.generate();
        orderItemService.create(
          TransactionOrderItemRequests.CreateRequest.builder()
            .id(orderItemId)
            .itemId(requestItem.getItemId())
            .orderId(orderId)
            .quantity(requestItem.getQuantity())
            .build()
        );
        if (itemLots.containsKey(requestItem.getId())) {
          val lots = itemLots.get(requestItem.getId());
          lots.forEach(lot -> {
            val requestItemLotId = TransactionOrderItemLotId.generate();
            orderItemLotService.create(
              TransactionOrderItemLotRequests.CreateRequest.builder()
                .id(requestItemLotId)
                .itemLotId(lot.getItemLotId())
                .orderItemId(orderItemId)
                .quantity(lot.getQuantity())
                .build()
            );
          });
        }
      });
    return created;
  }

  public void verify(VerifyRequest request) {
    val aggregator = orderRepository
      .findAggregatorBy(request.getId()).get();
    if (aggregator.isModifiable()) {
      val response = aggregator.apply(new TransactionOrderMessages.VerifyRequest());
      orderRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }

  @Getter
  @Builder
  public static class GenerateByRequestRequest {

    @Valid
    @NotNull
    TransactionRequestId requestId;

  }

  @Getter
  @Builder
  public static class VerifyRequest {

    @Valid
    @NotNull
    TransactionOrderId id;

  }

}
