package pico.erp.warehouse.location.level;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.audit.annotation.Audit;
import pico.erp.warehouse.location.WarehouseLocationCode;
import pico.erp.warehouse.location.bay.WarehouseBay;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-level")
public class WarehouseLevel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseLevelId id;

  WarehouseLevelCode code;

  WarehouseLocationCode locationCode;

  WarehouseBay bay;

  public WarehouseLevelMessages.CreateResponse apply(WarehouseLevelMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    bay = request.getBay();
    locationCode = bay.getLocationCode().with(code);
    return new WarehouseLevelMessages.CreateResponse(
      Arrays.asList(new WarehouseLevelEvents.CreatedEvent(this.id)));
  }

  public WarehouseLevelMessages.UpdateResponse apply(WarehouseLevelMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = bay.getLocationCode().with(code);
    return new WarehouseLevelMessages.UpdateResponse(
      Arrays.asList(new WarehouseLevelEvents.UpdatedEvent(this.id, codeChanged)));
  }

  public WarehouseLevelMessages.ResetLocationCodeResponse apply(
    WarehouseLevelMessages.ResetLocationCodeRequest request) {
    locationCode = bay.getLocationCode().with(code);
    return new WarehouseLevelMessages.ResetLocationCodeResponse(
      Arrays.asList(new WarehouseLevelEvents.UpdatedEvent(this.id, true)));
  }

  public WarehouseLevelMessages.DeleteResponse apply(WarehouseLevelMessages.DeleteRequest request) {
    return new WarehouseLevelMessages.DeleteResponse(
      Arrays.asList(new WarehouseLevelEvents.DeletedEvent(this.id)));
  }

}
