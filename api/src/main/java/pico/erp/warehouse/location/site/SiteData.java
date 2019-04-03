package pico.erp.warehouse.location.site;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.shared.data.Address;
import pico.erp.warehouse.location.LocationCode;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class SiteData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  SiteId id;

  SiteCode code;

  LocationCode locationCode;

  String name;

  Address address;

  boolean deleted;

  LocalDateTime deletedDate;

}
