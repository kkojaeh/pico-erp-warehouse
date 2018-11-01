package pico.erp.warehouse.transaction.order.item;

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
import pico.erp.item.ItemData;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrder;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderEvents;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-order-item")
public class WarehouseTransactionOrderItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderItemId id;

  WarehouseTransactionOrder order;

  ItemData item;

  BigDecimal quantity;

  public WarehouseTransactionOrderItemMessages.CreateResponse apply(
    WarehouseTransactionOrderItemMessages.CreateRequest request) {
    if (!request.getOrder().isModifiable()) {
      throw new WarehouseTransactionOrderItemExceptions.CannotCreateException();
    }
    id = request.getId();
    order = request.getOrder();
    item = request.getItem();
    quantity = request.getQuantity();
    return new WarehouseTransactionOrderItemMessages.CreateResponse(
      Arrays.asList(
        new WarehouseTransactionOrderItemEvents.CreatedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public WarehouseTransactionOrderItemMessages.UpdateResponse apply(
    WarehouseTransactionOrderItemMessages.UpdateRequest request) {
    if (!order.isModifiable()) {
      throw new WarehouseTransactionOrderItemExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    return new WarehouseTransactionOrderItemMessages.UpdateResponse(
      Arrays.asList(
        new WarehouseTransactionOrderItemEvents.UpdatedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public WarehouseTransactionOrderItemMessages.DeleteResponse apply(
    WarehouseTransactionOrderItemMessages.DeleteRequest request) {
    if (!order.isModifiable()) {
      throw new WarehouseTransactionOrderItemExceptions.CannotDeleteException();
    }
    return new WarehouseTransactionOrderItemMessages.DeleteResponse(
      Arrays.asList(
        new WarehouseTransactionOrderItemEvents.DeletedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }


}
