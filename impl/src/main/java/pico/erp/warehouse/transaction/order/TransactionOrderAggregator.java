package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.pack.PackSelector.ItemLotSelectOptions;
import pico.erp.warehouse.pack.PackSelector.ItemSelectOptions;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;
import pico.erp.warehouse.transaction.order.item.TransactionOrderItem;
import pico.erp.warehouse.transaction.order.item.lot.TransactionOrderItemLot;
import pico.erp.warehouse.transaction.order.pack.TransactionOrderPack;
import pico.erp.warehouse.transaction.request.TransactionRequest;

@Getter
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PROTECTED)
public class TransactionOrderAggregator extends TransactionOrder {

  private static final long serialVersionUID = 1L;

  List<TransactionOrderItem> items;

  List<TransactionOrderItemLot> itemLots;

  List<TransactionOrderPack> packs;

  @Builder(builderMethodName = "aggregatorBuilder")
  public TransactionOrderAggregator(boolean committable,
    TransactionOrderId id, TransactionOrderCode code, OffsetDateTime dueDate,
    CompanyData relatedCompany, Station station,
    TransactionOrderStatusKind status, TransactionTypeKind type,
    Auditor acceptedBy, OffsetDateTime acceptedDate, Auditor completedBy,
    OffsetDateTime completedDate, Auditor committedBy, OffsetDateTime committedDate,
    Auditor canceledBy, OffsetDateTime canceledDate,
    TransactionRequest request,
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy,
    List<TransactionOrderItem> items,
    List<TransactionOrderItemLot> itemLots,
    List<TransactionOrderPack> packs) {
    super(committable, id, code, dueDate, relatedCompany, station, status, type, acceptedBy,
      acceptedDate, completedBy, completedDate, committedBy, committedDate, canceledBy,
      canceledDate, request, quantityCorrectionPolicy);
    this.items = items;
    this.itemLots = itemLots;
    this.packs = packs;
  }

  public TransactionOrderMessages.CommitResponse apply(
    TransactionOrderMessages.CommitRequest request) {
    if (!isCommittable()) {
      throw new TransactionOrderExceptions.CannotCommitException();
    }
    val packs = new LinkedList<Pack>();
    if (type == TransactionTypeKind.OUTBOUND && packs.isEmpty()) {
      if (itemLots.isEmpty()) {
        for (val item : items) {
          val options = ItemSelectOptions.builder()
            .itemId(item.getItem().getId())
            .quantity(item.getQuantity())
            .quantityCorrectionPolicy(quantityCorrectionPolicy)
            .build();
          packs.addAll(
            request.getPackSelector().select(options).collect(Collectors.toList())
          );
        }
      } else {
        for (val itemLot : itemLots) {
          val options = ItemLotSelectOptions.builder()
            .itemLotId(itemLot.getItemLot().getId())
            .quantity(itemLot.getQuantity())
            .quantityCorrectionPolicy(quantityCorrectionPolicy)
            .build();
          packs.addAll(
            request.getPackSelector().select(options).collect(Collectors.toList())
          );
        }
      }
    }
    status = TransactionOrderStatusKind.COMMITTED;
    committedBy = request.getCommittedBy();
    committedDate = OffsetDateTime.now();
    return new TransactionOrderMessages.CommitResponse(
      Arrays.asList(new TransactionOrderEvents.CommittedEvent(this.id)),
      packs
    );
  }

  public boolean isCommittable() {
    return status.isCommittable() && (!items.isEmpty() || !itemLots.isEmpty());
  }

  /**
   */
  public TransactionOrderMessages.VerifyResponse apply(
    TransactionOrderMessages.VerifyRequest request) {
    /*BigDecimal total = items.stream()
      .map(item -> item.getQuantity())
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (itemLots.isEmpty()) {
      committable = total.compareTo(BigDecimal.ZERO) > 0; // total > 0
    } else {
      val itemQuantities = items.stream().collect(Collectors
        .toMap(TransactionOrderPack::getId,
          TransactionOrderPack::getQuantity));
      itemLots.forEach(itemLot -> {
        val requestItemId = itemLot.getTransactionRequestItem().getId();
        val remained = itemQuantities.get(requestItemId).subtract(itemLot.getQuantity());
        itemQuantities.put(requestItemId, remained);
      });
      val remained = itemQuantities.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
      committable = remained.compareTo(BigDecimal.ZERO) == 0; // remained == 0
    }*/
    return new TransactionOrderMessages.VerifyResponse(
      Collections.emptyList()
    );
  }

}
