package pico.erp.warehouse.location.rack;

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
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.zone.Zone;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-rack")
public class Rack implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  RackId id;

  RackCode code;

  LocationCode locationCode;

  Zone zone;

  boolean deleted;

  OffsetDateTime deletedDate;

  public RackMessages.CreateResponse apply(RackMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    zone = request.getZone();
    locationCode = zone.getLocationCode().with(code);
    return new RackMessages.CreateResponse(
      Arrays.asList(new RackEvents.CreatedEvent(this.id)));
  }

  public RackMessages.UpdateResponse apply(RackMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = zone.getLocationCode().with(code);
    return new RackMessages.UpdateResponse(
      Arrays.asList(new RackEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public RackMessages.ResetLocationCodeResponse apply(
    RackMessages.ResetLocationCodeRequest request) {
    locationCode = zone.getLocationCode().with(code);
    return new RackMessages.ResetLocationCodeResponse(
      Arrays.asList(new RackEvents.UpdatedEvent(this.id, true)));
  }

  public RackMessages.DeleteResponse apply(RackMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new RackMessages.DeleteResponse(
      Arrays.asList(new RackEvents.DeletedEvent(this.id)));
  }

}
