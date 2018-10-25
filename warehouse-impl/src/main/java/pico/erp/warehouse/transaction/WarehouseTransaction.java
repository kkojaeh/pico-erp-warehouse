package pico.erp.warehouse.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.company.CompanyData;
import pico.erp.item.lot.ItemLotData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;

@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction")
public class WarehouseTransaction implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionId id;

  ItemLotData itemLot;

  BigDecimal quantity;

  WarehouseTransactionTypeKind type;

  Auditor transactedBy;

  OffsetDateTime transactedDate;

  CompanyData relatedCompany;

  WarehouseStation station;

  public WarehouseTransaction() {

  }

  public WarehouseTransactionMessages.CreateResponse apply(
    WarehouseTransactionMessages.CreateRequest request) {
    id = request.getId();
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    type = request.getType();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    transactedBy = request.getTransactedBy();
    transactedDate = OffsetDateTime.now();
    return new WarehouseTransactionMessages.CreateResponse(
      Arrays.asList(new WarehouseTransactionEvents.InboundedEvent(this.id))
    );
  }

}
