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
import pico.erp.warehouse.transaction.request.TransactionRequest;
import pico.erp.warehouse.transaction.request.TransactionRequestEvents;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request-item")
public class TransactionRequestItem implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionRequestItemId id;

  TransactionRequest request;

  ItemData item;

  BigDecimal quantity;

  public TransactionRequestItemMessages.CreateResponse apply(
    TransactionRequestItemMessages.CreateRequest request) {
    if (!request.getRequest().isModifiable()) {
      throw new TransactionRequestItemExceptions.CannotCreateException();
    }
    id = request.getId();
    this.request = request.getRequest();
    item = request.getItem();
    quantity = request.getQuantity();
    return new TransactionRequestItemMessages.CreateResponse(
      Arrays.asList(
        new TransactionRequestItemEvents.CreatedEvent(this.id),
        new TransactionRequestEvents.MemberChangedEvent(this.request.getId())
      )
    );
  }

  public TransactionRequestItemMessages.UpdateResponse apply(
    TransactionRequestItemMessages.UpdateRequest request) {
    if (!this.request.isModifiable()) {
      throw new TransactionRequestItemExceptions.CannotUpdateException();
    }
    quantity = request.getQuantity();
    return new TransactionRequestItemMessages.UpdateResponse(
      Arrays.asList(
        new TransactionRequestItemEvents.UpdatedEvent(this.id),
        new TransactionRequestEvents.MemberChangedEvent(this.request.getId())
      )
    );
  }

  public TransactionRequestItemMessages.DeleteResponse apply(
    TransactionRequestItemMessages.DeleteRequest request) {
    if (!this.request.isModifiable()) {
      throw new TransactionRequestItemExceptions.CannotDeleteException();
    }
    return new TransactionRequestItemMessages.DeleteResponse(
      Arrays.asList(
        new TransactionRequestItemEvents.DeletedEvent(this.id),
        new TransactionRequestEvents.MemberChangedEvent(this.request.getId())
      )
    );
  }


}
