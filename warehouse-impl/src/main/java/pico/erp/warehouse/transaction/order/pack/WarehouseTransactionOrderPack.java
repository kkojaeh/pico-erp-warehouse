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
import pico.erp.audit.annotation.Audit;
import pico.erp.warehouse.pack.WarehousePack;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrder;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderEvents;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-order-pack")
public class WarehouseTransactionOrderPack implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderPackId id;

  WarehouseTransactionOrder order;

  WarehousePack pack;

  WarehouseTransactionOrderPackStatusKind status;

  public WarehouseTransactionOrderPackMessages.CreateResponse apply(
    WarehouseTransactionOrderPackMessages.CreateRequest request) {
    if (!request.getOrder().isModifiable()) {
      throw new WarehouseTransactionOrderPackExceptions.CannotCreateException();
    }
    id = request.getId();
    order = request.getOrder();
    pack = request.getPack();
    status = WarehouseTransactionOrderPackStatusKind.ready(request.getOrder().getType());
    return new WarehouseTransactionOrderPackMessages.CreateResponse(
      Arrays.asList(
        new WarehouseTransactionOrderPackEvents.CreatedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public WarehouseTransactionOrderPackMessages.UpdateResponse apply(
    WarehouseTransactionOrderPackMessages.UpdateRequest request) {
    if (!order.isModifiable()) {
      throw new WarehouseTransactionOrderPackExceptions.CannotUpdateException();
    }
    return new WarehouseTransactionOrderPackMessages.UpdateResponse(
      Arrays.asList(
        new WarehouseTransactionOrderPackEvents.UpdatedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }

  public WarehouseTransactionOrderPackMessages.DeleteResponse apply(
    WarehouseTransactionOrderPackMessages.DeleteRequest request) {
    if (!order.isModifiable()) {
      throw new WarehouseTransactionOrderPackExceptions.CannotDeleteException();
    }
    return new WarehouseTransactionOrderPackMessages.DeleteResponse(
      Arrays.asList(
        new WarehouseTransactionOrderPackEvents.DeletedEvent(this.id),
        new WarehouseTransactionOrderEvents.MemberChangedEvent(this.order.getId())
      )
    );
  }


}
