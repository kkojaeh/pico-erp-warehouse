package pico.erp.warehouse.transaction.request;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.warehouse.location.station.WarehouseStationId;
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;


public interface WarehouseTransactionRequestRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;

    // 예정일
    @Future
    @NotNull
    OffsetDateTime dueDate;

    // 대상 회사(회사)
    @NotNull
    CompanyId relatedCompanyId;

    // 공급지 내부일때 사용
    WarehouseStationId stationId;

    @NotNull
    WarehouseTransactionTypeKind type;

    @NotNull
    WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;

    // 예정일
    @Future
    @NotNull
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    @NotNull
    CompanyId relatedCompanyId;

    WarehouseStationId stationId;

    @NotNull
    WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CommitRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelUncommittedRequest {

    /**
     * 지정 기준시간보다 예전 데이터를 삭제
     */
    @NotNull
    OffsetDateTime fixedDate;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class AcceptRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CompleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;

  }
}
