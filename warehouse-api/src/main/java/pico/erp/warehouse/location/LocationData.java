package pico.erp.warehouse.location;

import java.io.Serializable;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class LocationData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  LocationId id;

  LocationCode code;

}
