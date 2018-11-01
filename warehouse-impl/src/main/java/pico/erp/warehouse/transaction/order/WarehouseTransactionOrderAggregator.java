package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pico.erp.company.CompanyData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;
import pico.erp.warehouse.transaction.order.item.WarehouseTransactionOrderItem;
import pico.erp.warehouse.transaction.order.item.lot.WarehouseTransactionOrderItemLot;
import pico.erp.warehouse.transaction.order.pack.WarehouseTransactionOrderPack;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequest;

@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class WarehouseTransactionOrderAggregator extends WarehouseTransactionOrder {

  private static final long serialVersionUID = 1L;

  List<WarehouseTransactionOrderItem> items;

  List<WarehouseTransactionOrderItemLot> itemLots;

  List<WarehouseTransactionOrderPack> packs;

  @Builder(builderMethodName = "aggregatorBuilder")
  public WarehouseTransactionOrderAggregator(boolean committable,
    WarehouseTransactionOrderId id, OffsetDateTime dueDate,
    CompanyData relatedCompany, WarehouseStation station,
    WarehouseTransactionOrderStatusKind status,
    WarehouseTransactionTypeKind type, Auditor acceptedBy, OffsetDateTime acceptedDate,
    Auditor completedBy, OffsetDateTime completedDate, Auditor committedBy,
    OffsetDateTime committedDate, Auditor canceledBy, OffsetDateTime canceledDate,
    WarehouseTransactionRequest request,
    List<WarehouseTransactionOrderItem> items,
    List<WarehouseTransactionOrderItemLot> itemLots,
    List<WarehouseTransactionOrderPack> packs) {
    super(committable, id, dueDate, relatedCompany, station, status, type, acceptedBy, acceptedDate,
      completedBy, completedDate, committedBy, committedDate, canceledBy, canceledDate, request);
    this.items = items;
    this.itemLots = itemLots;
    this.packs = packs;
  }

  public WarehouseTransactionOrderMessages.CommitResponse apply(
    WarehouseTransactionOrderMessages.CommitRequest request) {
    if (!isCancelable()) {
      throw new WarehouseTransactionOrderExceptions.CannotCommitException();
    }
    status = WarehouseTransactionOrderStatusKind.COMMITTED;
    canceledBy = request.getCommittedBy();
    canceledDate = OffsetDateTime.now();
    return new WarehouseTransactionOrderMessages.CommitResponse(
      Arrays.asList(new WarehouseTransactionOrderEvents.CommittedEvent(this.id))
    );
  }

  /**
   */
  public WarehouseTransactionOrderMessages.VerifyResponse apply(
    WarehouseTransactionOrderMessages.VerifyRequest request) {
    /*BigDecimal total = items.stream()
      .map(item -> item.getQuantity())
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (itemLots.isEmpty()) {
      committable = total.compareTo(BigDecimal.ZERO) > 0; // total > 0
    } else {
      val itemQuantities = items.stream().collect(Collectors
        .toMap(WarehouseTransactionOrderPack::getId,
          WarehouseTransactionOrderPack::getQuantity));
      itemLots.forEach(itemLot -> {
        val requestItemId = itemLot.getTransactionRequestItem().getId();
        val remained = itemQuantities.get(requestItemId).subtract(itemLot.getQuantity());
        itemQuantities.put(requestItemId, remained);
      });
      val remained = itemQuantities.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      committable = remained.compareTo(BigDecimal.ZERO) == 0; // remained == 0
    }*/
    return new WarehouseTransactionOrderMessages.VerifyResponse(
      Collections.emptyList()
    );
  }

}
