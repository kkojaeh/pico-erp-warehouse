package pico.erp.warehouse.location.bay;

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
import pico.erp.warehouse.location.rack.Rack;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-bay")
public class Bay implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  BayId id;

  BayCode code;

  LocationCode locationCode;

  Rack rack;

  boolean deleted;

  OffsetDateTime deletedDate;

  public BayMessages.CreateResponse apply(BayMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    rack = request.getRack();
    locationCode = rack.getLocationCode().with(code);
    return new BayMessages.CreateResponse(
      Arrays.asList(new BayEvents.CreatedEvent(this.id)));
  }

  public BayMessages.UpdateResponse apply(BayMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = rack.getLocationCode().with(code);
    return new BayMessages.UpdateResponse(
      Arrays.asList(new BayEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public BayMessages.ResetLocationCodeResponse apply(
    BayMessages.ResetLocationCodeRequest request) {
    locationCode = rack.getLocationCode().with(code);
    return new BayMessages.ResetLocationCodeResponse(
      Arrays.asList(new BayEvents.UpdatedEvent(this.id, true)));
  }

  public BayMessages.DeleteResponse apply(BayMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new BayMessages.DeleteResponse(
      Arrays.asList(new BayEvents.DeletedEvent(this.id)));
  }

}
