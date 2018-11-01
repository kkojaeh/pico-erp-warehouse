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
import pico.erp.warehouse.location.station.WarehouseStationId;
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;
import pico.erp.warehouse.transaction.request.WarehouseTransactionRequestId;


public interface WarehouseTransactionOrderRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;

    // 예정일
    @NotNull
    @Future
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    @NotNull
    CompanyId relatedCompanyId;

    // 공급지 내부일때 사
    WarehouseStationId stationId;

    @NotNull
    WarehouseTransactionTypeKind type;

    WarehouseTransactionRequestId transactionRequestId;

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
    WarehouseTransactionOrderId id;

    // 예정일
    @NotNull
    @Future
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    @NotNull
    CompanyId relatedCompanyId;

    WarehouseStationId stationId;

    @NotNull
    WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  /*@Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;
  }
*/
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CommitRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CancelRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class AcceptRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CompleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;

  }
}
