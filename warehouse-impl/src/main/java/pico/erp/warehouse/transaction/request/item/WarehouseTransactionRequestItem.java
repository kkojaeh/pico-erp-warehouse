package pico.erp.warehouse.transaction.request.item;

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequest;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestEvents;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request-item")
public class WarehouseTransactionRequestItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestItemId id;

  WarehouseTransactionRequest transactionRequest;

  ItemData item;

  BigDecimal quantity;

  public WarehouseTransactionRequestItemMessages.CreateResponse apply(
    WarehouseTransactionRequestItemMessages.CreateRequest request) {
    if (!request.getTransactionRequest().isModifiable()) {
      throw new WarehouseTransactionRequestItemExceptions.CannotCreateException();
    }
    id = request.getId();
    transactionRequest = request.getTransactionRequest();
    item = request.getItem();
    quantity = request.getQuantity();
    return new WarehouseTransactionRequestItemMessages.CreateResponse(
      Arrays.asList(
        new WarehouseTransactionRequestItemEvents.CreatedEvent(this.id),
        new WarehouseTransactionRequestEvents.MemberChangedEvent(this.transactionRequest.getId())
      )
    );
  }

  public WarehouseTransactionRequestItemMessages.UpdateResponse apply(
    WarehouseTransactionRequestItemMessages.UpdateRequest request) {
    if (!transactionRequest.isModifiable()) {
      throw new WarehouseTransactionRequestItemExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    return new WarehouseTransactionRequestItemMessages.UpdateResponse(
      Arrays.asList(
        new WarehouseTransactionRequestItemEvents.UpdatedEvent(this.id),
        new WarehouseTransactionRequestEvents.MemberChangedEvent(this.transactionRequest.getId())
      )
    );
  }

  public WarehouseTransactionRequestItemMessages.DeleteResponse apply(
    WarehouseTransactionRequestItemMessages.DeleteRequest request) {
    if (!transactionRequest.isModifiable()) {
      throw new WarehouseTransactionRequestItemExceptions.CannotDeleteException();
    }
    return new WarehouseTransactionRequestItemMessages.DeleteResponse(
      Arrays.asList(
        new WarehouseTransactionRequestItemEvents.DeletedEvent(this.id),
        new WarehouseTransactionRequestEvents.MemberChangedEvent(this.transactionRequest.getId())
      )
    );
  }


}
