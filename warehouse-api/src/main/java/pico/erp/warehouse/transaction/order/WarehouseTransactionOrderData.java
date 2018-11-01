package pico.erp.warehouse.transaction.order;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.company.CompanyId;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStationId;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionOrderData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderId id;

  OffsetDateTime dueDate;

  CompanyId relatedCompanyId;

  WarehouseStationId stationId;

  WarehouseTransactionTypeKind type;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  WarehouseTransactionOrderStatusKind status;

  WarehouseTransactionRequestId requestId;

}
