package pico.erp.warehouse.transaction.order

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
class TransactionOrderServiceSpec extends Specification {

  @Autowired
  TransactionOrderService warehouseTransactionOrderService

  def inboundOrderId = TransactionOrderId.from("create")

  def outboundOrderId = TransactionOrderId.from("outbound")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = StationId.from("S2")

  def setup() {
  }

  def "입고지시를 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: inboundOrderId,
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

  def "지난시간으로 입고지시를 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: inboundOrderId,
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

  def "출고지시를 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
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

  def "출고시간으로 입고지시를 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
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

  def "입/출고를 생성 후 접수 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionOrderService.accept(
      new TransactionOrderRequests.AcceptRequest(
        id: outboundOrderId
      )
    )

    then:
    thrown(TransactionOrderExceptions.CannotAcceptException)
  }

  def "입/출고를 생성 후 완료 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new TransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: TransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: TransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionOrderService.complete(
      new TransactionOrderRequests.CompleteRequest(
        id: outboundOrderId
      )
    )

    then:
    thrown(TransactionOrderExceptions.CannotCompleteException)
  }

}
