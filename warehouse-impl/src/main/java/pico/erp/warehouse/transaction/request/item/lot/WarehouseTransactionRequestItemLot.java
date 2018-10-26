package pico.erp.warehouse.transaction.request.item.lot;

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
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestEvents;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItem;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request-item-lot")
public class WarehouseTransactionRequestItemLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestItemLotId id;

  WarehouseTransactionRequestItem transactionRequestItem;

  ItemLotData itemLot;

  BigDecimal quantity;

  public WarehouseTransactionRequestItemLotMessages.CreateResponse apply(
    WarehouseTransactionRequestItemLotMessages.CreateRequest request) {
    if (!request.getTransactionRequestItem().getTransactionRequest().isModifiable()) {
      throw new WarehouseTransactionRequestItemLotExceptions.CannotCreateException();
    }
    if (!request.getTransactionRequestItem().getItem().getId()
      .equals(request.getItemLot().getItemId())) {
      throw new WarehouseTransactionRequestItemLotExceptions.CannotCreateException();
    }
    id = request.getId();
    transactionRequestItem = request.getTransactionRequestItem();
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    return new WarehouseTransactionRequestItemLotMessages.CreateResponse(
      Arrays.asList(
        new WarehouseTransactionRequestItemLotEvents.CreatedEvent(this.id),
        new WarehouseTransactionRequestEvents.MemberChangedEvent(
          this.transactionRequestItem.getTransactionRequest().getId())
      )
    );
  }

  public WarehouseTransactionRequestItemLotMessages.UpdateResponse apply(
    WarehouseTransactionRequestItemLotMessages.UpdateRequest request) {
    if (!transactionRequestItem.getTransactionRequest().isModifiable()) {
      throw new WarehouseTransactionRequestItemLotExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    return new WarehouseTransactionRequestItemLotMessages.UpdateResponse(
      Arrays.asList(
        new WarehouseTransactionRequestItemLotEvents.UpdatedEvent(this.id),
        new WarehouseTransactionRequestEvents.MemberChangedEvent(
          this.transactionRequestItem.getTransactionRequest().getId())
      )
    );
  }

  public WarehouseTransactionRequestItemLotMessages.DeleteResponse apply(
    WarehouseTransactionRequestItemLotMessages.DeleteRequest request) {
    if (!transactionRequestItem.getTransactionRequest().isModifiable()) {
      throw new WarehouseTransactionRequestItemLotExceptions.CannotDeleteException();
    }
    return new WarehouseTransactionRequestItemLotMessages.DeleteResponse(
      Arrays.asList(
        new WarehouseTransactionRequestItemLotEvents.DeletedEvent(this.id),
        new WarehouseTransactionRequestEvents.MemberChangedEvent(
          this.transactionRequestItem.getTransactionRequest().getId())
      )
    );
  }


}
