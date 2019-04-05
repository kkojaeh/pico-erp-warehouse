package pico.erp.warehouse.location.bay;


import java.io.Serializable;
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
import javax.persistence.PreUpdate;
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
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.LocationCode;
import pico.erp.warehouse.location.rack.RackId;

@Entity(name = "Bay")
@Table(name = "WAH_WAREHOUSE_BAY", indexes = {
  @Index(columnList = "RACK_ID")
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BayEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  BayId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CODE", length = TypeDefinitions.CODE_LENGTH))
  })
  BayCode code;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "LOCATION_CODE", length = TypeDefinitions.CODE_LENGTH))
  })
  LocationCode locationCode;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "RACK_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  RackId rackId;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @Column(updatable = false)
  OffsetDateTime createdDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "LAST_MODIFIED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "LAST_MODIFIED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  @LastModifiedBy
  Auditor lastModifiedBy;

  OffsetDateTime lastModifiedDate;

  boolean deleted;

  OffsetDateTime deletedDate;

  @PrePersist
  private void onCreate() {
    createdDate = OffsetDateTime.now();
    lastModifiedDate = OffsetDateTime.now();
  }

  @PreUpdate
  private void onUpdate() {
    lastModifiedDate = OffsetDateTime.now();
  }

}
