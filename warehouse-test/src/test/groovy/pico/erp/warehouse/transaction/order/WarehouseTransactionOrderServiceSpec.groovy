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
import pico.erp.warehouse.location.station.WarehouseStationId
import pico.erp.warehouse.transaction.WarehouseTransactionQuantityCorrectionPolicyKind
import pico.erp.warehouse.transaction.WarehouseTransactionTypeKind
import spock.lang.Specification

import javax.validation.ConstraintViolationException
import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionOrderServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionOrderService warehouseTransactionOrderService

  def inboundOrderId = WarehouseTransactionOrderId.from("create")

  def outboundOrderId = WarehouseTransactionOrderId.from("outbound")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

  def setup() {
  }

  def "입고지시를 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: inboundOrderId,
        dueDate: dueDate,
        type: WarehouseTransactionTypeKind.INBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    inbounded.dueDate == dueDate
    inbounded.type == WarehouseTransactionTypeKind.INBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "지난시간으로 입고지시를 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: inboundOrderId,
        dueDate: OffsetDateTime.now().minusDays(2),
        type: WarehouseTransactionTypeKind.INBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    thrown(ConstraintViolationException)
  }

  def "출고지시를 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: dueDate,
        type: WarehouseTransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    inbounded.dueDate == dueDate
    inbounded.type == WarehouseTransactionTypeKind.OUTBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "출고시간으로 입고지시를 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: OffsetDateTime.now().minusDays(2),
        type: WarehouseTransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )

    then:
    thrown(ConstraintViolationException)
  }

  def "입/출고를 생성 후 접수 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: WarehouseTransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionOrderService.accept(
      new WarehouseTransactionOrderRequests.AcceptRequest(
        id: outboundOrderId
      )
    )

    then:
    thrown(WarehouseTransactionOrderExceptions.CannotAcceptException)
  }

  def "입/출고를 생성 후 완료 할 수 없다"() {
    when:
    warehouseTransactionOrderService.create(
      new WarehouseTransactionOrderRequests.CreateRequest(
        id: outboundOrderId,
        dueDate: OffsetDateTime.now().plusDays(2),
        type: WarehouseTransactionTypeKind.OUTBOUND,
        relatedCompanyId: companyId,
        stationId: stationId,
        quantityCorrectionPolicy: WarehouseTransactionQuantityCorrectionPolicyKind.NEGATIVE
      )
    )
    warehouseTransactionOrderService.complete(
      new WarehouseTransactionOrderRequests.CompleteRequest(
        id: outboundOrderId
      )
    )

    then:
    thrown(WarehouseTransactionOrderExceptions.CannotCompleteException)
  }

}
