package pico.erp.warehouse.location.bay;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.rack.WarehouseRackId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseBayData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseBayId id;

  WarehouseBayCode code;

  WarehouseRackId rackId;

  WarehouseLocationCode locationCode;

}
