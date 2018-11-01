package pico.erp.warehouse.transaction.request;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.company.CompanyData;
import pico.erp.shared.data.Auditor;
import pico.erp.warehouse.location.station.WarehouseStation;
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request")
public class WarehouseTransactionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  WarehouseTransactionRequestId id;

  OffsetDateTime dueDate;

  CompanyData relatedCompany;

  WarehouseStation station;

  WarehouseTransactionRequestStatusKind status;

  WarehouseTransactionTypeKind type;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  protected boolean committable;

  WarehouseTransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  public WarehouseTransactionRequestMessages.CreateResponse apply(
    WarehouseTransactionRequestMessages.CreateRequest request) {
    id = request.getId();
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    status = WarehouseTransactionRequestStatusKind.CREATED;
    type = request.getType();
    committable = false;
    quantityCorrectionPolicy = request.getQuantityCorrectionPolicy();
    return new WarehouseTransactionRequestMessages.CreateResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CreatedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.UpdateResponse apply(
    WarehouseTransactionRequestMessages.UpdateRequest request) {
    if (!isModifiable()) {
      throw new WarehouseTransactionRequestExceptions.CannotModifyException();
    }
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    quantityCorrectionPolicy = request.getQuantityCorrectionPolicy();
    return new WarehouseTransactionRequestMessages.UpdateResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.UpdatedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.CommitResponse apply(
    WarehouseTransactionRequestMessages.CommitRequest request) {
    if (!isCommittable()) {
      throw new WarehouseTransactionRequestExceptions.CannotCommitException();
    }
    status = WarehouseTransactionRequestStatusKind.COMMITTED;
    committedBy = request.getCommittedBy();
    committedDate = OffsetDateTime.now();
    return new WarehouseTransactionRequestMessages.CommitResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CommittedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.CancelResponse apply(
    WarehouseTransactionRequestMessages.CancelRequest request) {
    if (!isCancelable()) {
      throw new WarehouseTransactionRequestExceptions.CannotCancelException();
    }
    status = WarehouseTransactionRequestStatusKind.CANCELED;
    canceledBy = request.getCanceledBy();
    canceledDate = OffsetDateTime.now();
    return new WarehouseTransactionRequestMessages.CancelResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CanceledEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.AcceptResponse apply(
    WarehouseTransactionRequestMessages.AcceptRequest request) {
    if (!isAcceptable()) {
      throw new WarehouseTransactionRequestExceptions.CannotAcceptException();
    }
    status = WarehouseTransactionRequestStatusKind.ACCEPTED;
    acceptedBy = request.getAcceptedBy();
    acceptedDate = OffsetDateTime.now();
    return new WarehouseTransactionRequestMessages.AcceptResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.AcceptedEvent(this.id))
    );
  }

  public WarehouseTransactionRequestMessages.CompleteResponse apply(
    WarehouseTransactionRequestMessages.CompleteRequest request) {
    if (!isCompletable()) {
      throw new WarehouseTransactionRequestExceptions.CannotCompleteException();
    }
    status = WarehouseTransactionRequestStatusKind.COMPLETED;
    completedBy = request.getCompletedBy();
    completedDate = OffsetDateTime.now();
    return new WarehouseTransactionRequestMessages.CompleteResponse(
      Arrays.asList(new WarehouseTransactionRequestEvents.CompletedEvent(this.id))
    );
  }

  public boolean isModifiable() {
    return status.isModifiable();
  }

  public boolean isAcceptable() {
    return status.isAcceptable();
  }

  public boolean isCancelable() {
    return status.isCancelable();
  }

  public boolean isCompletable() {
    return status.isCompletable();
  }


}
