package pico.erp.warehouse.transaction.order;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.company.CompanyId;
import pico.erp.user.UserId;
import pico.erp.warehouse.location.station.WarehouseStationId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseTransactionOrderData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionOrderId id;

  OffsetDateTime dueDate;

  CompanyId supplierId;

  WarehouseStationId stationId;

  UserId requesterId;

}
