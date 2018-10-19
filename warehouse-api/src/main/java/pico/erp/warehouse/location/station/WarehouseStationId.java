package pico.erp.warehouse.location.station;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@ToString
public class WarehouseStationId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @NotNull
  private UUID value;

  public static WarehouseStationId from(@NonNull String value) {
    try {
      return new WarehouseStationId(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return new WarehouseStationId(UUID.nameUUIDFromBytes(value.getBytes()));
    }
  }

  public static WarehouseStationId from(@NonNull UUID value) {
    return new WarehouseStationId(value);
  }

  public static WarehouseStationId generate() {
    return from(UUID.randomUUID());
  }

}
