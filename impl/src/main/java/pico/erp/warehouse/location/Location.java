package pico.erp.warehouse.location;

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

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Location implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  LocationId id;

  LocationCode code;

  boolean deleted;

  OffsetDateTime deletedDate;

  public LocationMessages.CreateResponse apply(
    LocationMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    return new LocationMessages.CreateResponse(
      Arrays.asList(new LocationEvents.CreatedEvent(this.id)));
  }

  public LocationMessages.UpdateResponse apply(
    LocationMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    return new LocationMessages.UpdateResponse(
      Arrays.asList(new LocationEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public LocationMessages.DeleteResponse apply(
    LocationMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new LocationMessages.DeleteResponse(
      Arrays.asList(new LocationEvents.DeletedEvent(this.id)));
  }

}
