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
import pico.erp.warehouse.location.station.Station;

@Builder
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction")
public class Transaction implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionId id;

  ItemLotData itemLot;

  BigDecimal quantity;

  TransactionTypeKind type;

  Auditor transactedBy;

  OffsetDateTime transactedDate;

  CompanyData relatedCompany;

  Station station;

  public Transaction() {

  }

  public TransactionMessages.CreateResponse apply(
    TransactionMessages.CreateRequest request) {
    id = request.getId();
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    type = request.getType();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    transactedBy = request.getTransactedBy();
    transactedDate = OffsetDateTime.now();
    return new TransactionMessages.CreateResponse(
      Arrays.asList(new TransactionEvents.InboundedEvent(this.id))
    );
  }

}
