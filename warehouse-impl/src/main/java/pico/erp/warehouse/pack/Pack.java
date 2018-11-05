package pico.erp.warehouse.pack;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.item.lot.ItemLotData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.Location;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-pack")
public class Pack implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  PackId id;

  PackCode code;

  Location location;

  ItemLotData itemLot;

  BigDecimal quantity;

  PackStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

  public Pack() {
    status = PackStatusKind.EMPTY;
  }

  public PackMessages.CreateResponse apply(
    PackMessages.CreateRequest request) {
    id = request.getId();
    code = request.getCodeGenerator().generate(this);
    return new PackMessages.CreateResponse(
      Arrays.asList(new PackEvents.CreatedEvent(this.id))
    );
  }

  public PackMessages.PackResponse apply(
    PackMessages.PackRequest request) {
    itemLot = request.getItemLot();
    quantity = request.getQuantity();
    return new PackMessages.PackResponse(
      Arrays.asList(new PackEvents.PackedEvent(this.id))
    );
  }

  public PackMessages.PutResponse apply(
    PackMessages.PutRequest request) {
    location = request.getLocation();
    return new PackMessages.PutResponse(
      Arrays.asList(new PackEvents.PutEvent(this.id))
    );
  }

  public PackMessages.DeleteResponse apply(
    PackMessages.DeleteRequest request) {
    return new PackMessages.DeleteResponse(
      Arrays.asList(new PackEvents.DeletedEvent(this.id))
    );
  }

}
