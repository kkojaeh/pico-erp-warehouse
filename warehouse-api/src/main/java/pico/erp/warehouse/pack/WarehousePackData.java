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
import pico.erp.warehouse.location.WarehouseLocationId;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class WarehousePackData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehousePackId id;

  WarehousePackCode code;

  WarehouseLocationId locationId;

  ItemLotId itemLotId;

  BigDecimal quantity;

  WarehousePackStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

}
