package pico.erp.warehouse.transaction.request;


import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import pico.erp.company.CompanyId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.StationId;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;

@Entity(name = "TransactionRequest")
@Table(name = "WAH_WAREHOUSE_TRANSACTION_REQUEST")
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRequestEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  TransactionRequestId id;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CODE", length = TypeDefinitions.CODE_LENGTH))
  })
  TransactionRequestCode code;

  LocalDateTime dueDate;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "TRANSACTION_COMPANY_ID", length = TypeDefinitions.ID_LENGTH))
  })
  CompanyId transactionCompanyId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "STATION_ID", length = TypeDefinitions.UUID_BINARY_LENGTH))
  })
  StationId stationId;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  TransactionRequestStatusKind status;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  TransactionTypeKind type;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @CreatedDate
  @Column(updatable = false)
  LocalDateTime createdDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "COMMITTED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "COMMITTED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor committedBy;

  @Column
  LocalDateTime committedDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CANCELED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CANCELED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor canceledBy;

  @Column
  LocalDateTime canceledDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "ACCEPTED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "ACCEPTED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor acceptedBy;

  @Column
  LocalDateTime acceptedDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "COMPLETED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "COMPLETED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  Auditor completedBy;

  @Column
  LocalDateTime completedDate;

  boolean committable;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

}
