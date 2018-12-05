package pico.erp.warehouse.transaction.order.item.lot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemEvents;

@SuppressWarnings("unused")
@Component
public class TransactionOrderItemLotEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-item-lot-event-listener";

  @Autowired
  private TransactionOrderItemLotServiceLogic orderItemLotService;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionOrderItemEvents.DeletedEvent.CHANNEL)
  public void onTransactionOrderItemDeleted(
    TransactionOrderItemEvents.DeletedEvent event) {

    orderItemLotService.deleteBy(
      TransactionOrderItemLotServiceLogic.DeleteByOrderItemRequest.builder()
        .orderItemId(event.getTransactionOrderItemId())
        .build()
    );

  }

}
