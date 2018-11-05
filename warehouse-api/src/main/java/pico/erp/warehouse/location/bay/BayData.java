package pico.erp.warehouse.location.bay;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.rack.RackId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class BayData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  BayId id;

  BayCode code;

  RackId rackId;

  LocationCode locationCode;

  boolean deleted;

  OffsetDateTime deletedDate;

}
