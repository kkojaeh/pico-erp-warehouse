package pico.erp.warehouse.location.site;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.WarehouseLocationCode;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehouseSiteData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseSiteId id;

  WarehouseSiteCode code;

  WarehouseLocationCode locationCode;

  String name;

  boolean deleted;

  OffsetDateTime deletedDate;

}
