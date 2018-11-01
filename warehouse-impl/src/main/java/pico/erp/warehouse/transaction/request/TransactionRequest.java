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
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@Audit(alias = "warehouse-transaction-request")
public class TransactionRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  protected boolean committable;

  @Id
  TransactionRequestId id;

  OffsetDateTime dueDate;

  CompanyData relatedCompany;

  Station station;

  TransactionRequestStatusKind status;

  TransactionTypeKind type;

  Auditor committedBy;

  OffsetDateTime committedDate;

  Auditor canceledBy;

  OffsetDateTime canceledDate;

  Auditor acceptedBy;

  OffsetDateTime acceptedDate;

  Auditor completedBy;

  OffsetDateTime completedDate;

  TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  public TransactionRequestMessages.CreateResponse apply(
    TransactionRequestMessages.CreateRequest request) {
    id = request.getId();
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    status = TransactionRequestStatusKind.CREATED;
    type = request.getType();
    committable = false;
    quantityCorrectionPolicy = request.getQuantityCorrectionPolicy();
    return new TransactionRequestMessages.CreateResponse(
      Arrays.asList(new TransactionRequestEvents.CreatedEvent(this.id))
    );
  }

  public TransactionRequestMessages.UpdateResponse apply(
    TransactionRequestMessages.UpdateRequest request) {
    if (!isModifiable()) {
      throw new TransactionRequestExceptions.CannotModifyException();
    }
    dueDate = request.getDueDate();
    relatedCompany = request.getRelatedCompany();
    station = request.getStation();
    quantityCorrectionPolicy = request.getQuantityCorrectionPolicy();
    return new TransactionRequestMessages.UpdateResponse(
      Arrays.asList(new TransactionRequestEvents.UpdatedEvent(this.id))
    );
  }

  public TransactionRequestMessages.CommitResponse apply(
    TransactionRequestMessages.CommitRequest request) {
    if (!isCommittable()) {
      throw new TransactionRequestExceptions.CannotCommitException();
    }
    status = TransactionRequestStatusKind.COMMITTED;
    committedBy = request.getCommittedBy();
    committedDate = OffsetDateTime.now();
    return new TransactionRequestMessages.CommitResponse(
      Arrays.asList(new TransactionRequestEvents.CommittedEvent(this.id))
    );
  }

  public TransactionRequestMessages.CancelResponse apply(
    TransactionRequestMessages.CancelRequest request) {
    if (!isCancelable()) {
      throw new TransactionRequestExceptions.CannotCancelException();
    }
    status = TransactionRequestStatusKind.CANCELED;
    canceledBy = request.getCanceledBy();
    canceledDate = OffsetDateTime.now();
    return new TransactionRequestMessages.CancelResponse(
      Arrays.asList(new TransactionRequestEvents.CanceledEvent(this.id))
    );
  }

  public TransactionRequestMessages.AcceptResponse apply(
    TransactionRequestMessages.AcceptRequest request) {
    if (!isAcceptable()) {
      throw new TransactionRequestExceptions.CannotAcceptException();
    }
    status = TransactionRequestStatusKind.ACCEPTED;
    acceptedBy = request.getAcceptedBy();
    acceptedDate = OffsetDateTime.now();
    return new TransactionRequestMessages.AcceptResponse(
      Arrays.asList(new TransactionRequestEvents.AcceptedEvent(this.id))
    );
  }

  public TransactionRequestMessages.CompleteResponse apply(
    TransactionRequestMessages.CompleteRequest request) {
    if (!isCompletable()) {
      throw new TransactionRequestExceptions.CannotCompleteException();
    }
    status = TransactionRequestStatusKind.COMPLETED;
    completedBy = request.getCompletedBy();
    completedDate = OffsetDateTime.now();
    return new TransactionRequestMessages.CompleteResponse(
      Arrays.asList(new TransactionRequestEvents.CompletedEvent(this.id))
    );
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

  public boolean isModifiable() {
    return status.isModifiable();
  }


}
