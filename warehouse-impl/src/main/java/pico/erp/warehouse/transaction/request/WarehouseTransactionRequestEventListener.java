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
public class WarehouseTransactionRequestEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-request-event-listener";

  @Autowired
  private WarehouseTransactionRequestRepository warehouseTransactionRequestRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + WarehouseTransactionRequestEvents.MemberChangedEvent.CHANNEL)
  public void onWarehouseTransactionRequestMemberChanged(
    WarehouseTransactionRequestEvents.MemberChangedEvent event) {
    val aggregator = warehouseTransactionRequestRepository
      .findAggregatorBy(event.getWarehouseTransactionRequestId()).get();
    if (aggregator.isModifiable()) {
      val response = aggregator.apply(new WarehouseTransactionRequestMessages.VerifyRequest());
      warehouseTransactionRequestRepository.update(aggregator);
      eventPublisher.publishEvents(response.getEvents());
    }
  }

}
