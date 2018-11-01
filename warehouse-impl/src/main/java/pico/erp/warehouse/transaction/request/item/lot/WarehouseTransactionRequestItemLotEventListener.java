package pico.erp.warehouse.transaction.request.item.lot;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItemEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseTransactionRequestItemLotEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-request-item-lot-event-listener";

  @Autowired
  private WarehouseTransactionRequestItemLotRepository requestItemLotRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + WarehouseTransactionRequestItemEvents.DeletedEvent.CHANNEL)
  public void onWarehouseTransactionRequestItemDeleted(
    WarehouseTransactionRequestItemEvents.DeletedEvent event) {

    requestItemLotRepository
      .findAllBy(event.getWarehouseTransactionRequestItemId())
      .forEach(itemLot -> {
        val response = itemLot.apply(
          new WarehouseTransactionRequestItemLotMessages.DeleteRequest()
        );
        requestItemLotRepository.deleteBy(itemLot.getId());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
