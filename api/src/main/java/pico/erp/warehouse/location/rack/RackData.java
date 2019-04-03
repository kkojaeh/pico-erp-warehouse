package pico.erp.warehouse.location.rack;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.zone.ZoneId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class RackData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  RackId id;

  RackCode code;

  ZoneId zoneId;

  LocationCode locationCode;

  boolean deleted;

  LocalDateTime deletedDate;

}
