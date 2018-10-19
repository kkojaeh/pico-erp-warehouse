package pico.erp.warehouse.transaction;

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
public class WarehouseTransactionId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @NotNull
  private UUID value;

  public static WarehouseTransactionId from(@NonNull String value) {
    try {
      return new WarehouseTransactionId(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return new WarehouseTransactionId(UUID.nameUUIDFromBytes(value.getBytes()));
    }
  }

  public static WarehouseTransactionId from(@NonNull UUID value) {
    return new WarehouseTransactionId(value);
  }

  public static WarehouseTransactionId generate() {
    return from(UUID.randomUUID());
  }


}