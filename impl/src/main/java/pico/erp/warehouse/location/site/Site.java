package pico.erp.warehouse.location.site;

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
import pico.erp.shared.data.Address;
import pico.erp.warehouse.location.LocationCode;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Site implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  SiteId id;

  SiteCode code;

  LocationCode locationCode;

  String name;

  boolean deleted;

  OffsetDateTime deletedDate;

  Address address;

  public SiteMessages.CreateResponse apply(SiteMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    name = request.getName();
    locationCode = LocationCode.from(code);
    address = request.getAddress();
    return new SiteMessages.CreateResponse(
      Arrays.asList(new SiteEvents.CreatedEvent(this.id)));
  }

  public SiteMessages.UpdateResponse apply(SiteMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    name = request.getName();
    locationCode = LocationCode.from(code);
    address = request.getAddress();
    return new SiteMessages.UpdateResponse(
      Arrays.asList(new SiteEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public SiteMessages.DeleteResponse apply(SiteMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new SiteMessages.DeleteResponse(
      Arrays.asList(new SiteEvents.DeletedEvent(this.id)));
  }

}
