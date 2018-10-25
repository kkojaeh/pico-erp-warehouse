package pico.erp.warehouse.transaction;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.lot.ItemLotId;
import pico.erp.warehouse.location.station.WarehouseStationId;


public interface WarehouseTransactionRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    WarehouseTransactionId id;

    @Valid
    @NotNull
    ItemLotId itemLotId;

    @Valid
    @NotNull
    BigDecimal quantity;

    // 대상 회사(회사)
    @Valid
    @NotNull
    CompanyId relatedCompanyId;

    // 공급지 내부일때 사용
    @Valid
    WarehouseStationId stationId;

    @NotNull
    WarehouseTransactionTypeKind type;

  }

}
