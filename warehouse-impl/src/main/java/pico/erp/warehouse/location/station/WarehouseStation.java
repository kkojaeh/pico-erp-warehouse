package pico.erp.warehouse.location.station;

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
@Audit(alias = "warehouse-station")
public class WarehouseStation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseStationId id;

  WarehouseStationCode code;

  WarehouseLocationCode locationCode;

  WarehouseSite site;

  boolean deleted;

  OffsetDateTime deletedDate;

  public WarehouseStationMessages.CreateResponse apply(
    WarehouseStationMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    site = request.getSite();
    locationCode = site.getLocationCode().with(code);
    return new WarehouseStationMessages.CreateResponse(
      Arrays.asList(new WarehouseStationEvents.CreatedEvent(this.id)));
  }

  public WarehouseStationMessages.UpdateResponse apply(
    WarehouseStationMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = site.getLocationCode().with(code);
    return new WarehouseStationMessages.UpdateResponse(
      Arrays.asList(new WarehouseStationEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public WarehouseStationMessages.ResetLocationCodeResponse apply(
    WarehouseStationMessages.ResetLocationCodeRequest request) {
    locationCode = site.getLocationCode().with(code);
    return new WarehouseStationMessages.ResetLocationCodeResponse(
      Arrays.asList(new WarehouseStationEvents.UpdatedEvent(this.id, true)));
  }

  public WarehouseStationMessages.DeleteResponse apply(
    WarehouseStationMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new WarehouseStationMessages.DeleteResponse(
      Arrays.asList(new WarehouseStationEvents.DeletedEvent(this.id)));
  }

}
