package pico.erp.warehouse.pack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.item.lot.ItemLotId;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.LocationId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class PackData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  PackId id;

  PackCode code;

  LocationId locationId;

  ItemLotId itemLotId;

  BigDecimal quantity;

  PackStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

}
