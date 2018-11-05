package pico.erp.warehouse.transaction.order;

import java.time.OffsetDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import pico.erp.company.CompanyData;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.event.Event;
import pico.erp.warehouse.location.station.Station;
import pico.erp.warehouse.pack.Pack;
import pico.erp.warehouse.pack.PackSelector;
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind;
import pico.erp.warehouse.transaction.TransactionTypeKind;

public interface TransactionOrderMessages {

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CreateRequest {

    @Valid
    @NotNull
    TransactionOrderId id;

    // 예정일
    @NotNull
    OffsetDateTime dueDate;

    // 대상 회사(회사)
    @NotNull
    CompanyData relatedCompany;

    // 공급지 내부일때 사용
    Station station;

    @NotNull
    TransactionTypeKind type;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class UpdateRequest {

    // 예정일
    @NotNull
    OffsetDateTime dueDate;

    // 대상 회사(회사)
    @NotNull
    CompanyData relatedCompany;

    // 공급지 내부일때 사용
    Station station;

    @NotNull
    TransactionQuantityCorrectionPolicyKind quantityCorrectionPolicy;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CommitRequest {

    @NotNull
    PackSelector packSelector;

    @NotNull
    Auditor committedBy;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CancelRequest {

    @NotNull
    Auditor canceledBy;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class AcceptRequest {

    @NotNull
    Auditor acceptedBy;

  }

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  class CompleteRequest {

    @NotNull
    Auditor completedBy;

  }

  @Data
  class VerifyRequest {

  }

  @Value
  class VerifyResponse {

    Collection<Event> events;

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Value
  class CommitResponse {

    Collection<Event> events;

    Collection<Pack> selectedPacks;

  }

  @Value
  class CancelResponse {

    Collection<Event> events;

  }

  @Value
  class AcceptResponse {

    Collection<Event> events;

  }

  @Value
  class CompleteResponse {

    Collection<Event> events;

  }

}
