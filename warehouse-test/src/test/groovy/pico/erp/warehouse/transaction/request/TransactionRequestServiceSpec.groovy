package pico.erp.warehouse.transaction.request

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.lot.ItemLotId
import pico.erp.shared.IntegrationConfiguration
import pico.erp.warehouse.location.station.StationId
import pico.erp.warehouse.transaction.TransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.TransactionTypeKind
import spock.lang.Specification

import javax.validation.ConstraintViolationException
import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class TransactionRequestServiceSpec extends Specification {

  @Autowired
  TransactionRequestService warehouseTransactionRequestService

  def inboundRequestId = TransactionRequestId.from("create")

  def outboundRequestId = TransactionRequestId.from("outbound")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def setup() {
  }

  def "입고요청을 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: inboundRequestId,
        dueDate: dueDate,
        type: TransactionTypeKind.INBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    inbounded.dueDate == dueDate
    inbounded.type == TransactionTypeKind.INBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "지난시간으로 입고요청을 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: inboundRequestId,
        dueDate: OffsetDateTime.now().minusDays(2),
        type: TransactionTypeKind.INBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    thrown(ConstraintViolationException)
  }

  def "출고요청을 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: outboundRequestId,
        dueDate: dueDate,
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    inbounded.dueDate == dueDate
    inbounded.type == TransactionTypeKind.OUTBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "출고시간으로 입고요청을 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: outboundRequestId,
        dueDate: OffsetDateTime.now().minusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    thrown(ConstraintViolationException)
  }

  def "예정시간을 지나도 제출하지 않은 입/출고요청은 취소 된다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: outboundRequestId,
        dueDate: dueDate,
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionRequestService.cancelUncommitted(
      new TransactionRequestRequests.CancelUncommittedRequest(
        fixedDate: dueDate.plusMinutes(1)
      )
    )

    def request = warehouseTransactionRequestService.get(outboundRequestId)
    then:
    request.status == TransactionRequestStatusKind.CANCELED

  }

  def "입/출고를 생성 후 접수 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: outboundRequestId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionRequestService.accept(
      new TransactionRequestRequests.AcceptRequest(
        id: outboundRequestId
      )
    )

    then:
    thrown(TransactionRequestExceptions.CannotAcceptException)
  }

  def "입/출고를 생성 후 완료 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(
      new TransactionRequestRequests.CreateRequest(
        id: outboundRequestId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionRequestService.complete(
      new TransactionRequestRequests.CompleteRequest(
        id: outboundRequestId
      )
    )

    then:
    thrown(TransactionRequestExceptions.CannotCompleteException)
  }

}