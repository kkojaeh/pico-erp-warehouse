package pico.erp.warehouse.location.zone;

import java.io.Serializable;
import java.time.OffsetDateTime;
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
import pico.erp.warehouse.location.site.WarehouseSite;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-zone")
public class WarehouseZone implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseZoneId id;

  WarehouseZoneCode code;

  WarehouseLocationCode locationCode;

  WarehouseSite site;

  boolean deleted;

  OffsetDateTime deletedDate;

  public WarehouseZoneMessages.CreateResponse apply(WarehouseZoneMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    site = request.getSite();
    locationCode = site.getLocationCode().with(code);
    return new WarehouseZoneMessages.CreateResponse(
      Arrays.asList(new WarehouseZoneEvents.CreatedEvent(this.id)));
  }

  public WarehouseZoneMessages.UpdateResponse apply(WarehouseZoneMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = site.getLocationCode().with(code);
    return new WarehouseZoneMessages.UpdateResponse(
      Arrays.asList(new WarehouseZoneEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public WarehouseZoneMessages.ResetLocationCodeResponse apply(
    WarehouseZoneMessages.ResetLocationCodeRequest request) {
    locationCode = site.getLocationCode().with(code);
    return new WarehouseZoneMessages.ResetLocationCodeResponse(
      Arrays.asList(new WarehouseZoneEvents.UpdatedEvent(this.id, true)));
  }

  public WarehouseZoneMessages.DeleteResponse apply(WarehouseZoneMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new WarehouseZoneMessages.DeleteResponse(
      Arrays.asList(new WarehouseZoneEvents.DeletedEvent(this.id)));
  }

}
