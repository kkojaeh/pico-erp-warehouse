package pico.erp.warehouse.transaction.order.item.lot;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItemEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class TransactionOrderItemLotEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-item-lot-event-listener";

  @Autowired
  private TransactionOrderItemLotRepository warehouseTransactionRequestItemLotRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + TransactionOrderItemEvents.DeletedEvent.CHANNEL)
  public void onTransactionOrderItemDeleted(
    TransactionOrderItemEvents.DeletedEvent event) {

    warehouseTransactionRequestItemLotRepository
      .findAllBy(event.getTransactionOrderItemId())
      .forEach(itemLot -> {
        val response = itemLot.apply(
          new TransactionOrderItemLotMessages.DeleteRequest()
        );
        warehouseTransactionRequestItemLotRepository.deleteBy(itemLot.getId());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
