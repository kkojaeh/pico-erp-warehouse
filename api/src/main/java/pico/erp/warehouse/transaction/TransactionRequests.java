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
import pico.erp.warehouse.location.station.StationId;


public interface TransactionRequests {

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  class CreateRequest {

    TransactionId id;

    @Valid
    @NotNull
    ItemLotId itemLotId;

    @Valid
    @NotNull
    BigDecimal quantity;

    // 대상 회사(회사)
    @Valid
    @NotNull
    CompanyId transactionCompanyId;

    // 공급지 내부일때 사용
    @Valid
    StationId stationId;

    @NotNull
    TransactionTypeKind type;

  }

}
