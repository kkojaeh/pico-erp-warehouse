package pico.erp.warehouse.location.site;

import java.io.Serializable;
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

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-site")
public class WarehouseSite implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseSiteId id;

  WarehouseSiteCode code;

  WarehouseLocationCode locationCode;

  String name;

  public WarehouseSiteMessages.CreateResponse apply(WarehouseSiteMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCode();
    name = request.getName();
    locationCode = WarehouseLocationCode.from(code);
    return new WarehouseSiteMessages.CreateResponse(
      Arrays.asList(new WarehouseSiteEvents.CreatedEvent(this.id)));
  }

  public WarehouseSiteMessages.UpdateResponse apply(WarehouseSiteMessages.UpdateRequest request) {
    val codeChanged = !code.equals(request.getCode());
    code = request.getCode();
    name = request.getName();
    locationCode = WarehouseLocationCode.from(code);
    return new WarehouseSiteMessages.UpdateResponse(
      Arrays.asList(new WarehouseSiteEvents.UpdatedEvent(this.id, codeChanged)));
  }

  public WarehouseSiteMessages.DeleteResponse apply(WarehouseSiteMessages.DeleteRequest request) {
    return new WarehouseSiteMessages.DeleteResponse(
      Arrays.asList(new WarehouseSiteEvents.DeletedEvent(this.id)));
  }

}
