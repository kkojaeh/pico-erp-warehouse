package pico.erp.warehouse.transaction.request;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.warehouse.location.station.WarehouseStationId;


public interface WarehouseTransactionRequestRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class InboundRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;

    // 예정일
    @NotNull
    OffsetDateTime dueDate;

    // 대상 회사(회사)
    CompanyId relatedCompanyId;

    // 공급지 내부일때 사용
    WarehouseStationId stationId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class OutboundRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;

    // 예정일
    @NotNull
    OffsetDateTime dueDate;

    // 대상 회사(회사)
    CompanyId relatedCompanyId;

    // 공급지 내부일때 사용
    WarehouseStationId stationId;

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
    @NotNull
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    CompanyId relatedCompanyId;

    WarehouseStationId stationId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionRequestId id;
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
}
