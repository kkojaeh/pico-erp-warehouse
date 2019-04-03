package pico.erp.warehouse.transaction.request;

import java.io.Serializable;
import java.time.LocalDateTime;
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

  LocalDateTime dueDate;

  CompanyId transactionCompanyId;

  StationId stationId;

  Auditor committedBy;

  LocalDateTime committedDate;

  Auditor canceledBy;

  LocalDateTime canceledDate;

  Auditor acceptedBy;

  LocalDateTime acceptedDate;

  Auditor completedBy;

  LocalDateTime completedDate;

  TransactionRequestStatusKind status;

  TransactionTypeKind type;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  boolean acceptable;

  boolean cancelable;

  boolean completable;

  boolean modifiable;

  boolean committable;

}
