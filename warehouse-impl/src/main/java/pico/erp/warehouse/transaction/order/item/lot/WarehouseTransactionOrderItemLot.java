package pico.erp.warehouse.transaction.order.item.lot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.item.lot.ItemLotData;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderEvents;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItem;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-order-item-lot")
public class WarehouseTransactionOrderItemLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderItemLotId id;

  WarehouseTransactionOrderItem orderItem;

  ItemLotData itemLot;

  BigDecimal quantity;

  public WarehouseTransactionOrderItemLotMessages.CreateResponse apply(
    WarehouseTransactionOrderItemLotMessages.CreateRequest request) {
    if (!request.getOrderItem().getOrder().isModifiable()) {
      throw new WarehouseTransactionOrderItemLotExceptions.CannotCreateException();
    }
    if (!request.getOrderItem().getItem().getId()
      .equals(request.getItemLot().getItemId())) {
      throw new WarehouseTransactionOrderItemLotExceptions.CannotCreateException();
    }
    id = request.getId();
    orderItem = request.getOrderItem();
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    return new WarehouseTransactionOrderItemLotMessages.CreateResponse(
      Arrays.asList(
        new WarehouseTransactionOrderItemLotEvents.CreatedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(
          this.orderItem.getOrder().getId())
      )
    );
  }

  public WarehouseTransactionOrderItemLotMessages.UpdateResponse apply(
    WarehouseTransactionOrderItemLotMessages.UpdateRequest request) {
    if (!orderItem.getOrder().isModifiable()) {
      throw new WarehouseTransactionOrderItemLotExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    return new WarehouseTransactionOrderItemLotMessages.UpdateResponse(
      Arrays.asList(
        new WarehouseTransactionOrderItemLotEvents.UpdatedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(
          this.orderItem.getOrder().getId())
      )
    );
  }

  public WarehouseTransactionOrderItemLotMessages.DeleteResponse apply(
    WarehouseTransactionOrderItemLotMessages.DeleteRequest request) {
    if (!orderItem.getOrder().isModifiable()) {
      throw new WarehouseTransactionOrderItemLotExceptions.CannotDeleteException();
    }
    return new WarehouseTransactionOrderItemLotMessages.DeleteResponse(
      Arrays.asList(
        new WarehouseTransactionOrderItemLotEvents.DeletedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(
          this.orderItem.getOrder().getId())
      )
    );
  }


}
