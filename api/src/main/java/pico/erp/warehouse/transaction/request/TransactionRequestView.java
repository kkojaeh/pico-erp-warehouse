package pico.erp.warehouse.transaction.request;

import java.time.OffsetDateTime;
import java.util.List;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.ItemId;
import pico.erp.shared.data.Auditor;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionRequestView {

  @Id
  TransactionRequestId id;

  TransactionRequestCode code;

  OffsetDateTime dueDate;

  CompanyId transactionCompanyId;

  StationId stationId;

  TransactionRequestStatusKind status;

  TransactionTypeKind type;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String code;

    ItemId itemId;

    CompanyId transactionCompanyId;

    List<TransactionRequestStatusKind> statuses;

    TransactionTypeKind type;

    UserId createBy;

    OffsetDateTime startCreatedDate;

    OffsetDateTime endCreatedDate;
  }

}
