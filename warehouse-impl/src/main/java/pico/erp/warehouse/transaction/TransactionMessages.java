package pico.erp.warehouse.transaction;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.company.CompanyData;
import pico.erp.item.lot.ItemLotData;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.station.Station;

public interface TransactionMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    TransactionId id;

    @NotNull
    ItemLotData itemLot;

    @Valid
    @NotNull
    BigDecimal quantity;

    @Valid
    @NotNull
    CompanyData relatedCompany;

    @Valid
    Station station;

    @NotNull
    TransactionTypeKind type;

    @NotNull
    Auditor transactedBy;

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

}
