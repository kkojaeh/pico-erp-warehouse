package pico.erp.warehouse.location.zone;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.site.WarehouseSiteId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseZoneData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseZoneId id;

  WarehouseZoneCode code;

  WarehouseSiteId siteId;

  WarehouseLocationCode locationCode;

  boolean deleted;

  OffsetDateTime deletedDate;

}
