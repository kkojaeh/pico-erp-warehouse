package pico.erp.warehouse.transaction.order.pack;


import java.io.Serializable;
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
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.pack.WarehousePackId;
import pico.erp.warehouse.transaction.order.WarehouseTransactionOrderId;

@Entity(name = "WarehouseTransactionOrderPack")
@Table(name = "WAH_WAREHOUSE_TRANSACTION_ORDER_PACK", indexes = {
  @Index(columnList = "ORDER_ID"),
  @Index(columnList = "ORDER_ID, PACK_ID", unique = true)
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseTransactionOrderPackEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  WarehouseTransactionOrderPackId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "PACK_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  WarehousePackId packId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ORDER_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  WarehouseTransactionOrderId orderId;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  WarehouseTransactionOrderPackStatusKind status;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @CreatedDate
  @Column(updatable = false)
  OffsetDateTime createdDate;

}
