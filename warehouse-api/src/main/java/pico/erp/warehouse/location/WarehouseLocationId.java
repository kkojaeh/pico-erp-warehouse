package pico.erp.warehouse.location;

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
public class WarehouseLocationId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @NotNull
  private UUID value;

  public static WarehouseLocationId from(@NonNull String value) {
    try {
      return new WarehouseLocationId(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return new WarehouseLocationId(UUID.nameUUIDFromBytes(value.getBytes()));
    }
  }

  public static WarehouseLocationId from(@NonNull UUID value) {
    return new WarehouseLocationId(value);
  }

  public static WarehouseLocationId generate() {
    return from(UUID.randomUUID());
  }

}
