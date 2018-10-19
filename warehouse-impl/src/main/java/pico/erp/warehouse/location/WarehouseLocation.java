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
import pico.erp.audit.annotation.Audit;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-location")
public class WarehouseLocation implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseLocationId id;

  WarehouseLocationCode code;

  boolean deleted;

  OffsetDateTime deletedDate;

  public WarehouseLocationMessages.CreateResponse apply(
    WarehouseLocationMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    return new WarehouseLocationMessages.CreateResponse(
      Arrays.asList(new WarehouseLocationEvents.CreatedEvent(this.id)));
  }

  public WarehouseLocationMessages.UpdateResponse apply(
    WarehouseLocationMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    return new WarehouseLocationMessages.UpdateResponse(
      Arrays.asList(new WarehouseLocationEvents.UpdatedEvent(this.id, codeChanged)), codeChanged);
  }

  public WarehouseLocationMessages.DeleteResponse apply(
    WarehouseLocationMessages.DeleteRequest request) {
    deleted = true;
    deletedDate = OffsetDateTime.now();
    return new WarehouseLocationMessages.DeleteResponse(
      Arrays.asList(new WarehouseLocationEvents.DeletedEvent(this.id)));
  }

}
