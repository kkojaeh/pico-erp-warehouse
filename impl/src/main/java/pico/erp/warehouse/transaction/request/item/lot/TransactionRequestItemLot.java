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
import pico.erp.warehouse.transaction.request.TransactionRequestEvents;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItem;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request-item-lot")
public class TransactionRequestItemLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionRequestItemLotId id;

  TransactionRequestItem requestItem;

  ItemLotData itemLot;

  BigDecimal quantity;

  public TransactionRequestItemLotMessages.CreateResponse apply(
    TransactionRequestItemLotMessages.CreateRequest request) {
    if (!request.getRequestItem().getRequest().isModifiable()) {
      throw new TransactionRequestItemLotExceptions.CannotCreateException();
    }
    if (!request.getRequestItem().getItem().getId()
      .equals(request.getItemLot().getItemId())) {
      throw new TransactionRequestItemLotExceptions.CannotCreateException();
    }
    id = request.getId();
    requestItem = request.getRequestItem();
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    return new TransactionRequestItemLotMessages.CreateResponse(
      Arrays.asList(
        new TransactionRequestItemLotEvents.CreatedEvent(this.id),
        new TransactionRequestEvents.MemberChangedEvent(
          this.requestItem.getRequest().getId())
      )
    );
  }

  public TransactionRequestItemLotMessages.UpdateResponse apply(
    TransactionRequestItemLotMessages.UpdateRequest request) {
    if (!requestItem.getRequest().isModifiable()) {
      throw new TransactionRequestItemLotExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    return new TransactionRequestItemLotMessages.UpdateResponse(
      Arrays.asList(
        new TransactionRequestItemLotEvents.UpdatedEvent(this.id),
        new TransactionRequestEvents.MemberChangedEvent(
          this.requestItem.getRequest().getId())
      )
    );
  }

  public TransactionRequestItemLotMessages.DeleteResponse apply(
    TransactionRequestItemLotMessages.DeleteRequest request) {
    if (!requestItem.getRequest().isModifiable()) {
      throw new TransactionRequestItemLotExceptions.CannotDeleteException();
    }
    return new TransactionRequestItemLotMessages.DeleteResponse(
      Arrays.asList(
        new TransactionRequestItemLotEvents.DeletedEvent(this.id),
        new TransactionRequestEvents.MemberChangedEvent(
          this.requestItem.getRequest().getId())
      )
    );
  }


}
