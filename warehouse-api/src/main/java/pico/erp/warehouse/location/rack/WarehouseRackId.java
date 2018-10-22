package pico.erp.warehouse.location.rack;

import com.fasterxml.jackson.annotation.JsonCreator;
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
public class WarehouseRackId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @NotNull
  private UUID value;

  @JsonCreator
  public static WarehouseRackId from(@NonNull String value) {
    try {
      return from(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return from(UUID.nameUUIDFromBytes(value.getBytes()));
    }
  }

  public static WarehouseRackId from(@NonNull UUID value) {
    return new WarehouseRackId(value);
  }

  public static WarehouseRackId generate() {
    return from(UUID.randomUUID());
  }

}
