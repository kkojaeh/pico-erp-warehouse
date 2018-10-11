package pico.erp.warehouse.location.bay;

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
import pico.erp.warehouse.location.rack.WarehouseRack;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-bay")
public class WarehouseBay implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseBayId id;

  WarehouseBayCode code;

  WarehouseLocationCode locationCode;

  WarehouseRack rack;

  public WarehouseBayMessages.CreateResponse apply(WarehouseBayMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    rack = request.getRack();
    locationCode = rack.getLocationCode().with(code);
    return new WarehouseBayMessages.CreateResponse(
      Arrays.asList(new WarehouseBayEvents.CreatedEvent(this.id)));
  }

  public WarehouseBayMessages.UpdateResponse apply(WarehouseBayMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = rack.getLocationCode().with(code);
    return new WarehouseBayMessages.UpdateResponse(
      Arrays.asList(new WarehouseBayEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public WarehouseBayMessages.ResetLocationCodeResponse apply(
    WarehouseBayMessages.ResetLocationCodeRequest request) {
    locationCode = rack.getLocationCode().with(code);
    return new WarehouseBayMessages.ResetLocationCodeResponse(
      Arrays.asList(new WarehouseBayEvents.UpdatedEvent(this.id, true)));
  }

  public WarehouseBayMessages.DeleteResponse apply(WarehouseBayMessages.DeleteRequest request) {
    return new WarehouseBayMessages.DeleteResponse(
      Arrays.asList(new WarehouseBayEvents.DeletedEvent(this.id)));
  }

}
