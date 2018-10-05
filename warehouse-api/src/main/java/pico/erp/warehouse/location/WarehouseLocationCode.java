package pico.erp.warehouse.location;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pico.erp.shared.TypeDefinitions;
import pico.erp.warehouse.location.bay.WarehouseBayCode;
import pico.erp.warehouse.location.level.WarehouseLevelCode;
import pico.erp.warehouse.location.rack.WarehouseRackCode;
import pico.erp.warehouse.location.site.WarehouseSiteCode;
import pico.erp.warehouse.location.zone.WarehouseZoneCode;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@ToString
public class WarehouseLocationCode implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @Size(min = 1, max = TypeDefinitions.ID_LENGTH)
  @NotNull
  private String value;

  public static WarehouseLocationCode from(@NonNull String value) {
    return new WarehouseLocationCode(value);
  }

  public static WarehouseLocationCode from(@NonNull WarehouseSiteCode code) {
    return new WarehouseLocationCode(code.getValue());
  }

  public WarehouseLocationCode with(@NonNull WarehouseZoneCode code) {
    return new WarehouseLocationCode(
      String.format("%s-%s", value, code.getValue())
    );
  }

  public WarehouseLocationCode with(@NonNull WarehouseRackCode code) {
    return new WarehouseLocationCode(
      String.format("%s-%02d", value, code.getValue())
    );
  }

  public WarehouseLocationCode with(@NonNull WarehouseBayCode code) {
    return new WarehouseLocationCode(
      String.format("%s-%02d", value, code.getValue())
    );
  }

  public WarehouseLocationCode with(@NonNull WarehouseLevelCode code) {
    return new WarehouseLocationCode(
      String.format("%s-%02d", value, code.getValue())
    );
  }
}
