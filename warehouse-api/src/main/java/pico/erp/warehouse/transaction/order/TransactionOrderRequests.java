package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;
import pico.erp.warehouse.transaction.request.TransactionRequestId;


public interface TransactionOrderRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    TransactionOrderId id;

    // 예정일
    @NotNull
    @Future
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    @NotNull
    CompanyId relatedCompanyId;

    // 공급지 내부일때 사
    StationId stationId;

    @NotNull
    TransactionTypeKind type;

    TransactionRequestId transactionRequestId;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    TransactionOrderId id;

    // 예정일
    @NotNull
    @Future
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    @NotNull
    CompanyId relatedCompanyId;

    StationId stationId;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  /*@Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    TransactionOrderId id;
  }
*/
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CommitRequest {

    @Valid
    @NotNull
    TransactionOrderId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelRequest {

    @Valid
    @NotNull
    TransactionOrderId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class AcceptRequest {

    @Valid
    @NotNull
    TransactionOrderId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CompleteRequest {

    @Valid
    @NotNull
    TransactionOrderId id;

  }
}
