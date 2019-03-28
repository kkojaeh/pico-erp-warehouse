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
import pico.erp.item.ItemData;
import pico.erp.warehouse.transaction.order.TransactionOrder;
import pico.erp.warehouse.transaction.order.TransactionOrderEvents;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionOrderItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionOrderItemId id;

  TransactionOrder order;

  ItemData item;

  BigDecimal quantity;

  BigDecimal remainedQuantity;

  public TransactionOrderItemMessages.CreateResponse apply(
    TransactionOrderItemMessages.CreateRequest request) {
    if (!request.getOrder().isModifiable()) {
      throw new TransactionOrderItemExceptions.CannotCreateException();
    }
    id = request.getId();
    order = request.getOrder();
    item = request.getItem();
    quantity = request.getQuantity();
    remainedQuantity = request.getQuantity();
    return new TransactionOrderItemMessages.CreateResponse(
      Arrays.asList(
        new TransactionOrderItemEvents.CreatedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public TransactionOrderItemMessages.UpdateResponse apply(
    TransactionOrderItemMessages.UpdateRequest request) {
    if (!order.isModifiable()) {
      throw new TransactionOrderItemExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    remainedQuantity = request.getQuantity();
    return new TransactionOrderItemMessages.UpdateResponse(
      Arrays.asList(
        new TransactionOrderItemEvents.UpdatedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public TransactionOrderItemMessages.DeleteResponse apply(
    TransactionOrderItemMessages.DeleteRequest request) {
    if (!order.isModifiable()) {
      throw new TransactionOrderItemExceptions.CannotDeleteException();
    }
    return new TransactionOrderItemMessages.DeleteResponse(
      Arrays.asList(
        new TransactionOrderItemEvents.DeletedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }


}
