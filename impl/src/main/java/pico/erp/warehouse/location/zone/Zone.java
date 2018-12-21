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
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.Site;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-zone")
public class Zone implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ZoneId id;

  ZoneCode code;

  LocationCode locationCode;

  Site site;

  boolean deleted;

  OffsetDateTime deletedDate;

  public ZoneMessages.CreateResponse apply(ZoneMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    site = request.getSite();
    locationCode = site.getLocationCode().with(code);
    return new ZoneMessages.CreateResponse(
      Arrays.asList(new ZoneEvents.CreatedEvent(this.id)));
  }

  public ZoneMessages.UpdateResponse apply(ZoneMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = site.getLocationCode().with(code);
    return new ZoneMessages.UpdateResponse(
      Arrays.asList(new ZoneEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public ZoneMessages.ResetLocationCodeResponse apply(
    ZoneMessages.ResetLocationCodeRequest request) {
    locationCode = site.getLocationCode().with(code);
    return new ZoneMessages.ResetLocationCodeResponse(
      Arrays.asList(new ZoneEvents.UpdatedEvent(this.id, true)));
  }

  public ZoneMessages.DeleteResponse apply(ZoneMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new ZoneMessages.DeleteResponse(
      Arrays.asList(new ZoneEvents.DeletedEvent(this.id)));
  }

}
