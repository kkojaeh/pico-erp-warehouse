package pico.erp.warehouse.transaction;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.item.ItemId;
import pico.erp.item.lot.ItemLotId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;

@Entity(name = "WarehouseTransaction")
@Table(name = "WAH_WAREHOUSE_TRANSACTION", indexes = {
  @Index(name = "WAH_WAREHOUSE_TRANSACTION_ITEM_ID_IDX", columnList = "ITEM_ID")
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseTransactionEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  WarehouseTransactionId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ITEM_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  ItemId itemId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ITEM_LOT_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  ItemLotId itemLotId;

  BigDecimal quantity;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  WarehouseTransactionTypeKind type;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "TRANSACTED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "TRANSACTED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor transactedBy;

  @CreatedDate
  @Column(updatable = false)
  OffsetDateTime transactedDate;

}
