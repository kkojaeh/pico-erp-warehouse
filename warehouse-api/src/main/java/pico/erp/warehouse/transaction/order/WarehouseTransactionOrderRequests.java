package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.station.WarehouseStationId;


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
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    CompanyId supplierId;

    // 공급지 내부일때 사
    WarehouseStationId stationId;

    UserId requesterId;

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
    OffsetDateTime dueDate;

    // 공급처 회사(회사)
    CompanyId supplierId;

    WarehouseStationId stationId;

  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  class CommitRequest {

    @Valid
    @NotNull
    WarehouseTransactionOrderId id;
  }
}
