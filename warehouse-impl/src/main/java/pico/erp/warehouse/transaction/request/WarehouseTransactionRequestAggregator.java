package pico.erp.warehouse.transaction.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.company.CompanyData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;
import pico.erp.warehouse.transaction.request.item.WarehouseTransactionRequestItem;
import pico.erp.warehouse.transaction.request.item.lot.WarehouseTransactionRequestItemLot;

@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class WarehouseTransactionRequestAggregator extends WarehouseTransactionRequest {

  private static final long serialVersionUID = 1L;

  List<WarehouseTransactionRequestItem> items;

  List<WarehouseTransactionRequestItemLot> itemLots;

  @Builder(builderMethodName = "aggregatorBuilder")
  public WarehouseTransactionRequestAggregator(
    WarehouseTransactionRequestId id, OffsetDateTime dueDate,
    CompanyData relatedCompany,
    WarehouseStation station,
    WarehouseTransactionRequestStatusKind status,
    WarehouseTransactionTypeKind type,
    Auditor committedBy, OffsetDateTime committedDate,
    Auditor canceledBy, OffsetDateTime canceledDate,
    Auditor acceptedBy, OffsetDateTime acceptedDate,
    Auditor completedBy, OffsetDateTime completedDate,
    boolean committable,
    List<WarehouseTransactionRequestItem> items,
    List<WarehouseTransactionRequestItemLot> itemLots) {
    super(id, dueDate, relatedCompany, station, status, type, committedBy, committedDate,
      canceledBy,
      canceledDate, acceptedBy, acceptedDate, completedBy, completedDate, committable);
    this.items = items;
    this.itemLots = itemLots;
  }

  /**
   * 품목의 수량이 0을 초과하고 품목 LOT 가 존재 하지 않거나 품목 LOT 수량이 일치하면 제출가능 상태로 변경
   */
  public WarehouseTransactionRequestMessages.VerifyResponse apply(
    WarehouseTransactionRequestMessages.VerifyRequest request) {
    BigDecimal total = items.stream()
      .map(item -> item.getQuantity())
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (itemLots.isEmpty()) {
      committable = total.compareTo(BigDecimal.ZERO) > 0; // total > 0
    } else {
      val itemQuantities = items.stream().collect(Collectors
        .toMap(WarehouseTransactionRequestItem::getId,
          WarehouseTransactionRequestItem::getQuantity));
      itemLots.forEach(itemLot -> {
        val requestItemId = itemLot.getTransactionRequestItem().getId();
        val remained = itemQuantities.get(requestItemId).subtract(itemLot.getQuantity());
        itemQuantities.put(requestItemId, remained);
      });
      val remained = itemQuantities.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      committable = remained.compareTo(BigDecimal.ZERO) == 0; // remained == 0
    }
    return new WarehouseTransactionRequestMessages.VerifyResponse(
      Collections.emptyList()
    );
  }

}
