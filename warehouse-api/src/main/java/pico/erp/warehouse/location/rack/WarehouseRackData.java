package pico.erp.warehouse.location.rack;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.zone.WarehouseZoneId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseRackData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseRackId id;

  WarehouseRackCode code;

  WarehouseZoneId zoneId;

  WarehouseLocationCode locationCode;

}
