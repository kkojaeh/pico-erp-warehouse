package pico.erp.warehouse.transaction.order;

import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemId;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemRequests;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemService;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotId;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotRequests;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLotService;
import pico.erp.warehouse.transaction.request.TransactionRequestEvents;
import pico.erp.warehouse.transaction.request.TransactionRequestRepository;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemRepository;
import pico.erp.warehouse.transaction.request.item.lot.TransactionRequestItemLotRepository;

@SuppressWarnings("unused")
@Component
public class TransactionOrderEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-event-listener";

  @Autowired
  private TransactionOrderRepository orderRepository;

  @Autowired
  private TransactionRequestRepository requestRepository;

  @Autowired
  private TransactionRequestItemRepository requestItemRepository;

  @Autowired
  private TransactionRequestItemLotRepository requestItemLotRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private TransactionOrderService orderService;

  @Autowired
  private TransactionOrderItemService orderItemService;

  @Autowired
  private TransactionOrderItemLotService orderItemLotService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionOrderEvents.MemberChangedEvent.CHANNEL)
  public void onTransactionOrderMemberChanged(
    TransactionOrderEvents.MemberChangedEvent event) {
    val aggregator = orderRepository
      .findAggregatorBy(event.getTransactionOrderId()).get();
    if (aggregator.isModifiable()) {
      val response = aggregator.apply(new TransactionOrderMessages.VerifyRequest());
      orderRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionRequestEvents.CommittedEvent.CHANNEL)
  public void onTransactionRequestCommitted(
    TransactionRequestEvents.CommittedEvent event) {
    val requestId = event.getTransactionRequestId();
    val orderId = TransactionOrderId.generate();
    val request = requestRepository.findBy(requestId).get();
    val orderCreateRequest = TransactionOrderRequests.CreateRequest.builder()
      .id(orderId)
      .dueDate(request.getDueDate())
      .relatedCompanyId(request.getRelatedCompany().getId())
      .stationId(request.getStation() != null ? request.getStation().getId() : null)
      .type(request.getType())
      .transactionRequestId(requestId)
      .quantityCorrectionPolicy(request.getQuantityCorrectionPolicy())
      .build();
    orderService.create(orderCreateRequest);

    //val itemLots = new HashMap<TransactionRequestItemId, List<TransactionRequestItemLot>>();

    val itemLots = requestItemLotRepository.findAllBy(requestId)
      .collect(Collectors.groupingBy(requestItemLot -> requestItemLot.getRequestItem().getId()));

    requestItemRepository.findAllBy(requestId)
      .forEach(requestItem -> {
        val orderItemId = TransactionOrderItemId.generate();
        orderItemService.create(
          TransactionOrderItemRequests.CreateRequest.builder()
            .id(orderItemId)
            .itemId(requestItem.getItem().getId())
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
                .itemLotId(lot.getItemLot().getId())
                .orderItemId(orderItemId)
                .quantity(lot.getQuantity())
                .build()
            );
          });
          orderService.commit(
            new TransactionOrderRequests.CommitRequest(orderId)
          );
        }

      });
  }
}
