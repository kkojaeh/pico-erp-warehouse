package pico.erp.warehouse.transaction.request.item.lot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class TransactionRequestItemLotEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-request-item-lot-event-listener";

  @Autowired
  private TransactionRequestItemLotServiceLogic requestItemLotService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionRequestItemEvents.DeletedEvent.CHANNEL)
  public void onTransactionRequestItemDeleted(
    TransactionRequestItemEvents.DeletedEvent event) {

    requestItemLotService.deleteBy(
      TransactionRequestItemLotServiceLogic.DeleteByRequestItemRequest.builder()
        .requestItemId(event.getTransactionRequestItemId())
        .build()
    );

  }

}
