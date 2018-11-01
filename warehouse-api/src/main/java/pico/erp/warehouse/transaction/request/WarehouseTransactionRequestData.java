package pico.erp.warehouse.transaction.request;

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
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionRequestData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestId id;

  OffsetDateTime dueDate;

  CompanyId relatedCompanyId;

  WarehouseStationId stationId;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  WarehouseTransactionRequestStatusKind status;

  WarehouseTransactionTypeKind type;

  WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

}
