package pico.erp.warehouse.transaction.order;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseTransactionOrderEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-event-listener";

  @Autowired
  private WarehouseTransactionOrderRepository orderRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + WarehouseTransactionOrderEvents.MemberChangedEvent.CHANNEL)
  public void onWarehouseTransactionOrderMemberChanged(
    WarehouseTransactionOrderEvents.MemberChangedEvent event) {
    val aggregator = orderRepository
      .findAggregatorBy(event.getWarehouseTransactionOrderId()).get();
    if (aggregator.isModifiable()) {
      val response = aggregator.apply(new WarehouseTransactionOrderMessages.VerifyRequest());
      orderRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }

}
