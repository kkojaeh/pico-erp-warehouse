package pico.erp.warehouse.location.level;

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
import pico.erp.warehouse.location.bay.Bay;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-level")
public class Level implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  LevelId id;

  LevelCode code;

  LocationCode locationCode;

  Bay bay;

  boolean deleted;

  OffsetDateTime deletedDate;

  public LevelMessages.CreateResponse apply(LevelMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    bay = request.getBay();
    locationCode = bay.getLocationCode().with(code);
    return new LevelMessages.CreateResponse(
      Arrays.asList(new LevelEvents.CreatedEvent(this.id)));
  }

  public LevelMessages.UpdateResponse apply(LevelMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    locationCode = bay.getLocationCode().with(code);
    return new LevelMessages.UpdateResponse(
      Arrays.asList(new LevelEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public LevelMessages.ResetLocationCodeResponse apply(
    LevelMessages.ResetLocationCodeRequest request) {
    locationCode = bay.getLocationCode().with(code);
    return new LevelMessages.ResetLocationCodeResponse(
      Arrays.asList(new LevelEvents.UpdatedEvent(this.id, true)));
  }

  public LevelMessages.DeleteResponse apply(LevelMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new LevelMessages.DeleteResponse(
      Arrays.asList(new LevelEvents.DeletedEvent(this.id)));
  }

}
