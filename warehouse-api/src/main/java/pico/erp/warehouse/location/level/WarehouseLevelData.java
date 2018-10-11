package pico.erp.warehouse.location.level;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.bay.WarehouseBayId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseLevelData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseLevelId id;

  WarehouseLevelCode code;

  WarehouseBayId bayId;

  WarehouseLocationCode locationCode;

  boolean deleted;

  OffsetDateTime deletedDate;

}
