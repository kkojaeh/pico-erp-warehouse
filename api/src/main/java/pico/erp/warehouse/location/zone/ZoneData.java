package pico.erp.warehouse.location.zone;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.SiteId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class ZoneData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ZoneId id;

  ZoneCode code;

  SiteId siteId;

  LocationCode locationCode;

  boolean deleted;

  LocalDateTime deletedDate;

}
