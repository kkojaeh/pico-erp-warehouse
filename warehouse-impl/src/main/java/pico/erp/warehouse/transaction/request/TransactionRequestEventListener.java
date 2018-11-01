package pico.erp.warehouse.transaction.request;

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
public class TransactionRequestEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-request-event-listener";

  @Autowired
  private TransactionRequestRepository warehouseTransactionRequestRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionRequestEvents.MemberChangedEvent.CHANNEL)
  public void onTransactionRequestMemberChanged(
    TransactionRequestEvents.MemberChangedEvent event) {
    val aggregator = warehouseTransactionRequestRepository
      .findAggregatorBy(event.getTransactionRequestId()).get();
    if (aggregator.isModifiable()) {
      val response = aggregator.apply(new TransactionRequestMessages.VerifyRequest());
      warehouseTransactionRequestRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }

}
