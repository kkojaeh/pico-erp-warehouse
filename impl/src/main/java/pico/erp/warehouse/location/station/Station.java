package pico.erp.warehouse.location.station;

import java.io.Serializable;
import java.time.LocalDateTime;
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
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.site.Site;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Station implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  StationId id;

  StationCode code;

  LocationCode locationCode;

  Site site;

  String name;

  boolean deleted;

  LocalDateTime deletedDate;

  public StationMessages.CreateResponse apply(
    StationMessages.CreateRequest request) {
    id = request.getId();
    name = request.getName();
    code = request.getCode();
    site = request.getSite();
    locationCode = site.getLocationCode().with(code);
    return new StationMessages.CreateResponse(
      Arrays.asList(new StationEvents.CreatedEvent(this.id)));
  }

  public StationMessages.UpdateResponse apply(
    StationMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    name = request.getName();
    locationCode = site.getLocationCode().with(code);
    return new StationMessages.UpdateResponse(
      Arrays.asList(new StationEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public StationMessages.ResetLocationCodeResponse apply(
    StationMessages.ResetLocationCodeRequest request) {
    locationCode = site.getLocationCode().with(code);
    return new StationMessages.ResetLocationCodeResponse(
      Arrays.asList(new StationEvents.UpdatedEvent(this.id, true)));
  }

  public StationMessages.DeleteResponse apply(
    StationMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = LocalDateTime.now();
    return new StationMessages.DeleteResponse(
      Arrays.asList(new StationEvents.DeletedEvent(this.id)));
  }

}
