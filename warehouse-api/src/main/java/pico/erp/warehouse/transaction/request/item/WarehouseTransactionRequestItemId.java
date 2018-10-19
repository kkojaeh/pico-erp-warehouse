package pico.erp.warehouse.transaction.request.item;

import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
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

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@ToString
public class WarehouseTransactionRequestItemId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @Size(min = 1, max = TypeDefinitions.ID_LENGTH)
  @NotNull
  private String value;

  public static WarehouseTransactionRequestItemId from(@NonNull String value) {
    return new WarehouseTransactionRequestItemId(value);
  }

  public static WarehouseTransactionRequestItemId generate() {
    return from(UUID.randomUUID().toString());
  }

}
