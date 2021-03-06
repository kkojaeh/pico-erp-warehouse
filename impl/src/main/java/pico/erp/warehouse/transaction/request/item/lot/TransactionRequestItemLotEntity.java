package pico.erp.warehouse.transaction.request.item.lot;


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
import javax.persistence.Index;
import javax.persistence.PrePersist;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.item.lot.ItemLotId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.transaction.request.TransactionRequestId;
import pico.erp.warehouse.transaction.request.item.TransactionRequestItemId;

@Entity(name = "TransactionRequestItemLot")
@Table(name = "WAH_WAREHOUSE_TRANSACTION_REQUEST_ITEM_LOT", indexes = {
  @Index(columnList = "REQUEST_ITEM_ID, ITEM_LOT_ID", unique = true),
  @Index(columnList = "REQUEST_ITEM_ID"),
  @Index(columnList = "REQUEST_ID")
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestItemLotEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  TransactionRequestItemLotId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ITEM_LOT_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  ItemLotId itemLotId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "REQUEST_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  TransactionRequestId requestId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "REQUEST_ITEM_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  TransactionRequestItemId requestItemId;

  @Column(precision = 19, scale = 2)
  BigDecimal quantity;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @Column(updatable = false)
  OffsetDateTime createdDate;

  @PrePersist
  private void onCreate() {
    createdDate = OffsetDateTime.now();
  }

}
