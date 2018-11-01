package pico.erp.warehouse.transaction.order.item.lot;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.shared.event.EventPublisher;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItemEvents;

@SuppressWarnings("unused")
@Component
@Transactional
public class WarehouseTransactionOrderItemLotEventListener {

  private static final String LISTENER_NAME = "listener.warehouse-transaction-order-item-lot-event-listener";

  @Autowired
  private WarehouseTransactionOrderItemLotRepository warehouseTransactionRequestItemLotRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + WarehouseTransactionOrderItemEvents.DeletedEvent.CHANNEL)
  public void onWarehouseTransactionOrderItemDeleted(
    WarehouseTransactionOrderItemEvents.DeletedEvent event) {

    warehouseTransactionRequestItemLotRepository
      .findAllBy(event.getWarehouseTransactionOrderItemId())
      .forEach(itemLot -> {
        val response = itemLot.apply(
          new WarehouseTransactionOrderItemLotMessages.DeleteRequest()
        );
        warehouseTransactionRequestItemLotRepository.deleteBy(itemLot.getId());
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
