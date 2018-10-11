package pico.erp.warehouse.location.rack;

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
import pico.erp.warehouse.location.zone.WarehouseZone;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-rack")
public class WarehouseRack implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseRackId id;

  WarehouseRackCode code;

  WarehouseLocationCode locationCode;

  WarehouseZone zone;

  public WarehouseRackMessages.CreateResponse apply(WarehouseRackMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    zone = request.getZone();
    locationCode = zone.getLocationCode().with(code);
    return new WarehouseRackMessages.CreateResponse(
      Arrays.asList(new WarehouseRackEvents.CreatedEvent(this.id)));
  }

  public WarehouseRackMessages.UpdateResponse apply(WarehouseRackMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = zone.getLocationCode().with(code);
    return new WarehouseRackMessages.UpdateResponse(
      Arrays.asList(new WarehouseRackEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public WarehouseRackMessages.ResetLocationCodeResponse apply(
    WarehouseRackMessages.ResetLocationCodeRequest request) {
    locationCode = zone.getLocationCode().with(code);
    return new WarehouseRackMessages.ResetLocationCodeResponse(
      Arrays.asList(new WarehouseRackEvents.UpdatedEvent(this.id, true)));
  }

  public WarehouseRackMessages.DeleteResponse apply(WarehouseRackMessages.DeleteRequest request) {
    return new WarehouseRackMessages.DeleteResponse(
      Arrays.asList(new WarehouseRackEvents.DeletedEvent(this.id)));
  }

}
