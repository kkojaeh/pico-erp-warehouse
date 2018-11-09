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
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;
import pico.erp.warehouse.transaction.request.TransactionRequestId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionOrderData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionOrderId id;

  TransactionOrderCode code;

  OffsetDateTime dueDate;

  CompanyId relatedCompanyId;

  StationId stationId;

  TransactionTypeKind type;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  TransactionOrderStatusKind status;

  TransactionRequestId requestId;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  boolean acceptable;

  boolean cancelable;

  boolean completable;

  boolean modifiable;

  boolean committable;

}
