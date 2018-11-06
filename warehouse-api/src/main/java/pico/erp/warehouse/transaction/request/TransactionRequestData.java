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
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class TransactionRequestData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  TransactionRequestId id;

  TransactionRequestCode code;

  OffsetDateTime dueDate;

  CompanyId relatedCompanyId;

  StationId stationId;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  TransactionRequestStatusKind status;

  TransactionTypeKind type;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

}
