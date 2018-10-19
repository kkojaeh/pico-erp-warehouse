package pico.erp.warehouse.location.station;

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
public class WarehouseStationData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseStationId id;

  WarehouseStationCode code;

  WarehouseSiteId siteId;

  WarehouseLocationCode locationCode;

  boolean deleted;

  OffsetDateTime deletedDate;

}
