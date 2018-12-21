package pico.erp.warehouse.location.level;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.bay.BayId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class LevelData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  LevelId id;

  LevelCode code;

  BayId bayId;

  LocationCode locationCode;

  boolean deleted;

  OffsetDateTime deletedDate;

}
