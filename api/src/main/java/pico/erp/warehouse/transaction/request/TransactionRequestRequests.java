package pico.erp.warehouse.transaction.request;

import java.time.LocalDateTime;
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


public interface TransactionRequestRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    TransactionRequestId id;

    // 예정일
    @Future
    @NotNull
    LocalDateTime dueDate;

    // 대상 회사(회사)
    @NotNull
    CompanyId transactionCompanyId;

    // 공급지 내부일때 사용
    StationId stationId;

    @NotNull
    TransactionTypeKind type;

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
    TransactionRequestId id;

    // 예정일
    @Future
    @NotNull
    LocalDateTime dueDate;

    // 공급처 회사(회사)
    @NotNull
    CompanyId transactionCompanyId;

    StationId stationId;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CommitRequest {

    @Valid
    @NotNull
    TransactionRequestId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelRequest {

    @Valid
    @NotNull
    TransactionRequestId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelUncommittedRequest {

    /**
     * 지정 기준시간보다 예전 데이터를 삭제
     */
    @NotNull
    LocalDateTime fixedDate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class AcceptRequest {

    @Valid
    @NotNull
    TransactionRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CompleteRequest {

    @Valid
    @NotNull
    TransactionRequestId id;

  }
}
