package pico.erp.warehouse.transaction.order.pack;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.transaction.order.TransactionOrder;
import pico.erp.warehouse.transaction.order.TransactionOrderEvents;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionOrderPack implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionOrderPackId id;

  TransactionOrder order;

  Pack pack;

  TransactionOrderPackStatusKind status;

  public TransactionOrderPackMessages.CreateResponse apply(
    TransactionOrderPackMessages.CreateRequest request) {
    if (!request.getOrder().isModifiable()) {
      throw new TransactionOrderPackExceptions.CannotCreateException();
    }
    id = request.getId();
    order = request.getOrder();
    pack = request.getPack();
    status = TransactionOrderPackStatusKind.ready(request.getOrder().getType());
    return new TransactionOrderPackMessages.CreateResponse(
      Arrays.asList(
        new TransactionOrderPackEvents.CreatedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public TransactionOrderPackMessages.UpdateResponse apply(
    TransactionOrderPackMessages.UpdateRequest request) {
    if (!order.isModifiable()) {
      throw new TransactionOrderPackExceptions.CannotUpdateException();
    }
    return new TransactionOrderPackMessages.UpdateResponse(
      Arrays.asList(
        new TransactionOrderPackEvents.UpdatedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public TransactionOrderPackMessages.DeleteResponse apply(
    TransactionOrderPackMessages.DeleteRequest request) {
    if (!order.isModifiable()) {
      throw new TransactionOrderPackExceptions.CannotDeleteException();
    }
    return new TransactionOrderPackMessages.DeleteResponse(
      Arrays.asList(
        new TransactionOrderPackEvents.DeletedEvent(this.id),
        new TransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }


}
