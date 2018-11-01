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
public class TransactionOrderEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-event-listener";

  @Autowired
  private TransactionOrderRepository orderRepository;

  @Autowired
  private EventPublisher eventPublisher;

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

}
