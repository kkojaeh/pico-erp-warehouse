package pico.erp.warehouse.transaction.order;

import java.time.LocalDateTime;
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
public class TransactionOrderView {

  @Id
  TransactionOrderId id;

  TransactionOrderCode code;

  LocalDateTime dueDate;

  CompanyId transactionCompanyId;

  StationId stationId;

  TransactionOrderStatusKind status;

  TransactionTypeKind type;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  Auditor committedBy;

  LocalDateTime committedDate;

  Auditor canceledBy;

  LocalDateTime canceledDate;

  Auditor acceptedBy;

  LocalDateTime acceptedDate;

  Auditor completedBy;

  LocalDateTime completedDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String code;

    ItemId itemId;

    CompanyId transactionCompanyId;

    List<TransactionOrderStatusKind> statuses;

    TransactionTypeKind type;

    UserId createBy;

    LocalDateTime startCreatedDate;

    LocalDateTime endCreatedDate;
  }

}
