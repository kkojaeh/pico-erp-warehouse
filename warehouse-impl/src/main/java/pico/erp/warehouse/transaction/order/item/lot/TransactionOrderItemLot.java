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
import pico.erp.warehouse.transaction.order.TransactionOrderEvents;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItem;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-order-item-lot")
public class TransactionOrderItemLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionOrderItemLotId id;

  TransactionOrderItem orderItem;

  ItemLotData itemLot;

  BigDecimal quantity;

  BigDecimal remainedQuantity;

  public TransactionOrderItemLotMessages.CreateResponse apply(
    TransactionOrderItemLotMessages.CreateRequest request) {
    if (!request.getOrderItem().getOrder().isModifiable()) {
      throw new TransactionOrderItemLotExceptions.CannotCreateException();
    }
    if (!request.getOrderItem().getItem().getId()
      .equals(request.getItemLot().getItemId())) {
      throw new TransactionOrderItemLotExceptions.CannotCreateException();
    }
    id = request.getId();
    orderItem = request.getOrderItem();
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    remainedQuantity = request.getQuantity();
    return new TransactionOrderItemLotMessages.CreateResponse(
      Arrays.asList(
        new TransactionOrderItemLotEvents.CreatedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(
          this.orderItem.getOrder().getId())
      )
    );
  }

  public TransactionOrderItemLotMessages.UpdateResponse apply(
    TransactionOrderItemLotMessages.UpdateRequest request) {
    if (!orderItem.getOrder().isModifiable()) {
      throw new TransactionOrderItemLotExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    remainedQuantity = request.getQuantity();
    return new TransactionOrderItemLotMessages.UpdateResponse(
      Arrays.asList(
        new TransactionOrderItemLotEvents.UpdatedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(
          this.orderItem.getOrder().getId())
      )
    );
  }

  public TransactionOrderItemLotMessages.DeleteResponse apply(
    TransactionOrderItemLotMessages.DeleteRequest request) {
    if (!orderItem.getOrder().isModifiable()) {
      throw new TransactionOrderItemLotExceptions.CannotDeleteException();
    }
    return new TransactionOrderItemLotMessages.DeleteResponse(
      Arrays.asList(
        new TransactionOrderItemLotEvents.DeletedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(
          this.orderItem.getOrder().getId())
      )
    );
  }


}
