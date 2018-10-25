package pico.erp.warehouse.transaction

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
import pico.erp.warehouse.transaction.request.*
import spock.lang.Specification

import javax.validation.ConstraintViolationException
import java.time.OffsetDateTime

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class WarehouseTransactionRequestServiceSpec extends Specification {

  @Autowired
  WarehouseTransactionRequestService warehouseTransactionRequestService

  def inboundRequestId = WarehouseTransactionRequestId.from("create")

  def outboundRequestId = WarehouseTransactionRequestId.from("outbound")

  def itemLotId = ItemLotId.from("item-1-lot-1")

  def companyId = CompanyId.from("CUST2")

  def stationId = WarehouseStationId.from("S2")

  def setup() {
  }

  def "입고요청을 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: inboundRequestId,
      dueDate: dueDate,
      type: WarehouseTransactionTypeKind.INBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))

    then:
    inbounded.dueDate == dueDate
    inbounded.type == WarehouseTransactionTypeKind.INBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "지난시간으로 입고요청을 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: inboundRequestId,
      dueDate: OffsetDateTime.now().minusDays(2),
      type: WarehouseTransactionTypeKind.INBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))

    then:
    thrown(ConstraintViolationException)
  }

  def "출고요청을 처리 한다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    def inbounded = warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: outboundRequestId,
      dueDate: dueDate,
      type: WarehouseTransactionTypeKind.OUTBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))

    then:
    inbounded.dueDate == dueDate
    inbounded.type == WarehouseTransactionTypeKind.OUTBOUND
    inbounded.relatedCompanyId == companyId
    inbounded.stationId == stationId
  }

  def "출고시간으로 입고요청을 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: outboundRequestId,
      dueDate: OffsetDateTime.now().minusDays(2),
      type: WarehouseTransactionTypeKind.OUTBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))

    then:
    thrown(ConstraintViolationException)
  }

  def "예정시간을 지나도 제출하지 않은 입/출고요청은 취소 된다"() {
    when:
    def dueDate = OffsetDateTime.now().plusDays(2)
    warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: outboundRequestId,
      dueDate: dueDate,
      type: WarehouseTransactionTypeKind.OUTBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))
    warehouseTransactionRequestService.cancelUncommitted(
      new WarehouseTransactionRequestRequests.CancelUncommittedRequest(
        fixedDate: dueDate.plusMinutes(1)
      )
    )

    def request = warehouseTransactionRequestService.get(outboundRequestId)
    then:
    request.status == WarehouseTransactionRequestStatusKind.CANCELED

  }

  def "입/출고를 생성 후 접수 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: outboundRequestId,
      dueDate: OffsetDateTime.now().plusDays(2),
      type: WarehouseTransactionTypeKind.OUTBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))
    warehouseTransactionRequestService.accept(
      new WarehouseTransactionRequestRequests.AcceptRequest(
        id: outboundRequestId
      )
    )

    then:
    thrown(WarehouseTransactionRequestExceptions.CannotAcceptException)
  }

  def "입/출고를 생성 후 완료 할 수 없다"() {
    when:
    warehouseTransactionRequestService.create(new WarehouseTransactionRequestRequests.CreateRequest(
      id: outboundRequestId,
      dueDate: OffsetDateTime.now().plusDays(2),
      type: WarehouseTransactionTypeKind.OUTBOUND,
      relatedCompanyId: companyId,
      stationId: stationId
    ))
    warehouseTransactionRequestService.complete(
      new WarehouseTransactionRequestRequests.CompleteRequest(
        id: outboundRequestId
      )
    )

    then:
    thrown(WarehouseTransactionRequestExceptions.CannotCompleteException)
  }

}
