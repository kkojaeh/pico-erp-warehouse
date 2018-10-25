package pico.erp.warehouse.transaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.company.CompanyId;
import pico.erp.item.lot.ItemLotId;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStationId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionData implements Serializable {

  private static final long serialVersionUID = 1L;

  WarehouseTransactionId id;

  ItemLotId itemLotId;

  BigDecimal quantity;

  Auditor transactedBy;

  OffsetDateTime transactedDate;

  CompanyId relatedCompanyId;

  WarehouseStationId stationId;

  WarehouseTransactionTypeKind type;

}
